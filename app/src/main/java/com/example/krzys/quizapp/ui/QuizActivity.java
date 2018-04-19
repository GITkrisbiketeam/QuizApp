package com.example.krzys.quizapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.transition.Fade;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.krzys.quizapp.R;
import com.example.krzys.quizapp.data.model.common.Category;
import com.example.krzys.quizapp.data.model.quizzes.QuizzesItem;
import com.example.krzys.quizapp.utils.Utils;
import com.example.krzys.quizapp.viewmodel.QuizViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = Utils.getLogTag(QuizActivity.class.getSimpleName());
    public static final String EXTRA_QUIZ = "extra_quiz";

    private QuizViewModel mQuizViewModel;

    QuizActivityContent mQuizActivityContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate " + savedInstanceState);
        setContentView(R.layout.activity_quiz);

        final Toolbar toolbar = findViewById(R.id.activity_quiz_toolbar);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Enable the Up button
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Initialize ViewModel
        mQuizViewModel = ViewModelProviders.of(this).get(QuizViewModel.class);

        Intent intent = getIntent();
        if (intent != null) {
            processIntent(intent, Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                    savedInstanceState == null);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent");
        if (intent != null) {
            processIntent(intent, false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // show back image share transition animation
                supportFinishAfterTransition();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * Process incoming Intent due to QuizActivity Created or recreated
     * This will trigger download selected QuizData based on input intent EXTRA_QUIZ
     * We will delay show of this Activity waiting for Quiz Item picture to downloaded
     *
     * @param intent Input {@link Intent} that should contain EXTRA_QUIZ with QuizzesItem
     * @param postponeEnterTransition should we pospone enter transition waiting for image to
     *                                download
     */
    private void processIntent(@NonNull Intent intent, boolean postponeEnterTransition) {
        QuizzesItem quizzesItem = intent.getParcelableExtra(EXTRA_QUIZ);
        Log.d(TAG, "processIntent quizzesItem: " + quizzesItem.toString());
        if (quizzesItem != null) {
            ImageView appBarQuizImage = findViewById(R.id.activity_quiz_toolbar_image_view);
            mQuizActivityContent = new QuizActivityContentQuiz(this, quizzesItem);

            if (postponeEnterTransition) {
                // Delay Enter transition animation until Image is loaded
                supportPostponeEnterTransition();
            } else {
                updateAppBarContent(quizzesItem);
            }

            // Prepare/download Quiz Toolbar image
            if (appBarQuizImage != null) {
                GlideApp.with(this).load(quizzesItem.getMainPhoto().getUrl()).listener(new RequestListener<Drawable>() {

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        scheduleStartPostponedTransition(appBarQuizImage);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable>
                            target, DataSource dataSource, boolean isFirstResource) {
                        // Call the "scheduleStartPostponedTransition()" method
                        // below when you know for certain that the shared element is
                        // ready for the transition to begin.
                        scheduleStartPostponedTransition(appBarQuizImage);
                        return false;
                    }
                }).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).fallback(R.mipmap
                        .ic_launcher).centerCrop().dontAnimate().into(appBarQuizImage);
            }

            // Prepare and set ProgressBar
            ProgressBar appBarProgressBar = findViewById(R.id.activity_quiz_toolbar_progress);

            // set ProgressBar color on pre Lolipop
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                appBarProgressBar.getProgressDrawable().setColorFilter(getResources().getColor(R
                        .color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
            }
            if (appBarProgressBar != null) {
                appBarProgressBar.setMax(quizzesItem.getQuestions());
                updateAppBarProgress(appBarProgressBar, quizzesItem);
            }
            mQuizViewModel.loadQuizzesItem(this, quizzesItem.getId()).observe(this,
                    quizzesItem1 -> {
                        Log.d(TAG, "processIntent quizzesItem changed: " + quizzesItem.toString());
                        if (appBarProgressBar != null) {
                            updateAppBarProgress(appBarProgressBar, quizzesItem);
                        }
                        mQuizActivityContent.updateUI();
                    });
        } else {
            // No QuizzesItem was provided in this intent so leave this Activity
            finish();
        }
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        Log.e(TAG, "onEnterAnimationComplete");
        Intent intent = getIntent();
        if (intent != null) {
            QuizzesItem quizzesItem = getIntent().getParcelableExtra(EXTRA_QUIZ);
            if (quizzesItem != null) {
                updateAppBarContent(quizzesItem);
            }
        }
    }

    private void updateAppBarContent(@NonNull QuizzesItem quizzesItem) {

        ConstraintLayout constraintToolbarLayout = findViewById(R.id
                .activity_quiz_toolbar_layout_title);
        Transition trans = new Fade();
        trans.setDuration(500);
        TransitionManager.beginDelayedTransition(constraintToolbarLayout, trans);

        TextView appBarTitle = constraintToolbarLayout.findViewById(R.id
                .activity_quiz_toolbar_title);
        TextView appBarCategories = constraintToolbarLayout.findViewById(R.id
                .activity_quiz_toolbar_categories);
        TextView appBarCreatedAt = constraintToolbarLayout.findViewById(R.id
                .activity_quiz_toolbar_created_at);
        View appBarImageScrim = constraintToolbarLayout.findViewById(R.id
                .activity_quiz_toolbar_image_scrim);

        // Set AppBar Title
        if (appBarTitle != null) {
            appBarTitle.setText(quizzesItem.getTitle());
            appBarTitle.setVisibility(View.VISIBLE);
        }

        // Set AppBar created at field
        if (appBarCreatedAt != null) {
            try {
                String createTime = quizzesItem.getCreatedAt();
                if (createTime == null) {
                    throw new NullPointerException();
                }

                SimpleDateFormat quizDataTimeFormat = new SimpleDateFormat
                        ("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
                Date date = quizDataTimeFormat.parse(createTime);
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy h:mm", Locale.US);
                appBarCreatedAt.setVisibility(View.VISIBLE);
                appBarCreatedAt.setText(getString(R.string.quiz_app_bar_create_at_text,
                        formatter.format(date)));
            } catch (Exception e) {
                Log.w(TAG, "updateAppBarContent could not parse created time value");
                appBarCreatedAt.setVisibility(View.GONE);
            }
        }

        // Set AppBar categories field
        if (appBarCategories != null){
            Category category = quizzesItem.getCategory();
            if(category == null || TextUtils.isEmpty(category.getName())){
                appBarCategories.setVisibility(View.GONE);
            } else {
                appBarCategories.setText(getString(R.string.quiz_app_bar_categories_text,category
                        .getName()));
                appBarCategories.setVisibility(View.VISIBLE);
            }
        }

        // Set AppBar gradient scrim for better TextView readability on ImageView
        if (appBarImageScrim != null){
            appBarImageScrim.setVisibility(View.VISIBLE);
        }
    }

    private void updateAppBarProgress(@NonNull ProgressBar progressBar, @NonNull QuizzesItem
            quizzesItem) {
        progressBar.setProgress(quizzesItem.getMyAnswers()!= null ? quizzesItem
                .getMyAnswers().size() : 0);
    }

    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver
                .OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                supportStartPostponedEnterTransition();
                return true;
            }
        });
    }

}
