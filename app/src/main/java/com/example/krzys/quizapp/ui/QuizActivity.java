package com.example.krzys.quizapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.krzys.quizapp.R;
import com.example.krzys.quizapp.data.model.quiz.LatestResult;
import com.example.krzys.quizapp.data.model.quiz.Question;
import com.example.krzys.quizapp.data.model.quiz.Rate;
import com.example.krzys.quizapp.data.viewmodel.QuizAppViewModel;
import com.example.krzys.quizapp.data.model.quizzes.QuizzesItem;
import com.example.krzys.quizapp.utils.Utils;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = Utils.getLogTag(QuizActivity.class.getName());
    public static final String EXTRA_QUIZ = "extra_quiz";

    private QuizAppViewModel mQuizAppViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        final Toolbar toolbar = findViewById(R.id.activity_quiz_toolbar);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Enable the Up button
            ab.setDisplayHomeAsUpEnabled(true);
        }

        mQuizAppViewModel = ViewModelProviders.of(this).get(QuizAppViewModel.class);


        Intent intent = getIntent();
        if (intent != null) {
            QuizzesItem item = intent.getParcelableExtra(EXTRA_QUIZ);
            Log.e(TAG, "onCreate item: " + item.toString());
            // Delay Enter transition animation until Image is loaded
            supportPostponeEnterTransition();

            //mQuizAppViewModel.getQuizData(getApplication(), item.getId());
            mQuizAppViewModel.loadQuizData(item.getId()).observe(QuizActivity.this, quizData -> {
                Log.w(TAG, "QuizActivity observer onChanged quizData:" + quizData);
                if (quizData != null) {
                    for (Question q : quizData.getQuestions()) {
                        Log.w(TAG, "QuizActivity observer onChanged Question:" + q);
                    }
                    for (Rate r : quizData.getRates()) {
                        Log.w(TAG, "QuizActivity observer onChanged Rate:" + r);
                    }
                    for (LatestResult lr : quizData.getLatestResults()) {
                        Log.w(TAG, "QuizActivity observer onChanged LatestResult:" + lr);
                    }
                    //TODO: udate UI
                }
            });
            CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id
                    .activity_quiz_collapsing_toolbar_layout);
            //collapsingToolbarLayout.setTitle(getResources().getString(R.string.user_name));

            // Prepare Quiz Toolbar image
            ImageView quizImage = collapsingToolbarLayout.findViewById(R.id
                    .activity_quiz_toolbar_image_view);
            GlideApp.with(this).load(item.getMainPhoto().getUrl()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                            Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable>
                        target, DataSource dataSource, boolean isFirstResource) {
                    // Call the "scheduleStartPostponedTransition()" method
                    // below when you know for certain that the shared element is
                    // ready for the transition to begin.
                    scheduleStartPostponedTransition(quizImage);
                    return false;
                }
            }).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).fallback(R.mipmap
                    .ic_launcher).into(quizImage);

        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_quiz, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quiz_action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;
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
}
