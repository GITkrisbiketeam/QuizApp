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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.krzys.quizapp.R;
import com.example.krzys.quizapp.data.model.common.Category;
import com.example.krzys.quizapp.data.model.quiz.Answer;
import com.example.krzys.quizapp.data.model.quiz.Image;
import com.example.krzys.quizapp.data.model.quiz.Question;
import com.example.krzys.quizapp.data.model.quiz.QuizData;
import com.example.krzys.quizapp.data.model.quiz.Rate;
import com.example.krzys.quizapp.data.model.quizzes.QuizzesItem;
import com.example.krzys.quizapp.data.viewmodel.QuizAppViewModel;
import com.example.krzys.quizapp.utils.Constants;
import com.example.krzys.quizapp.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = Utils.getLogTag(QuizActivity.class.getSimpleName());
    public static final String EXTRA_QUIZ = "extra_quiz";

    private QuizAppViewModel mQuizAppViewModel;

    private QuizData mQuizData;
    private QuizzesItem mQuizzesItem;

    private ProgressBar mAppBarProgressBar;

    private ViewGroup mQuizContentRoot;

    private ViewFlipper mQuizContentViewFlipper;

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

        mAppBarProgressBar = findViewById(R.id.activity_quiz_toolbar_progress);

        mQuizContentRoot = findViewById(R.id.quiz_content_root);
        mQuizContentViewFlipper = findViewById(R.id.quiz_content_view_flipper);
        // set slide in Animation for ViewFlipper
        mQuizContentViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim
                .slide_in_from_right));
        // set slide out Animation for ViewFlipper
        mQuizContentViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim
                .slide_out_from_left));

        // Initialize ViewModel
        mQuizAppViewModel = ViewModelProviders.of(this).get(QuizAppViewModel.class);

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
        mQuizzesItem = intent.getParcelableExtra(EXTRA_QUIZ);
        Log.d(TAG, "processIntent quizzesItem: " + mQuizzesItem.toString());
        if (mQuizzesItem != null) {
            ImageView appBarQuizImage = findViewById(R.id.activity_quiz_toolbar_image_view);

            if (postponeEnterTransition) {
                // Delay Enter transition animation until Image is loaded
                supportPostponeEnterTransition();
            } else {
                updateAppBarContent(mQuizzesItem);
            }

            mQuizAppViewModel.loadQuizData(this, mQuizzesItem.getId()).observe(this, quizData -> {
                Log.w(TAG, "QuizActivity observer onChanged quizData: " + quizData);
                if (quizData != null) {
                    mQuizData = quizData;
                    updateUI();
                } else if (!Utils.checkConnection(this)) {
                    scheduleStartPostponedTransition(appBarQuizImage);
                    Utils.showSnackbar(mQuizContentRoot, R.string
                            .string_internet_connection_not_available);
                }
            });

            // Prepare/download Quiz Toolbar image
            if (appBarQuizImage != null) {
                GlideApp.with(this).load(mQuizzesItem.getMainPhoto().getUrl()).listener(new RequestListener<Drawable>() {

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
            if (mAppBarProgressBar != null) {
                mAppBarProgressBar.setMax(mQuizzesItem.getQuestions());
                mAppBarProgressBar.setProgress(0);
            }
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
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy h:mm");
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

    private void updateUI() {
        if (mQuizData == null) {
            Log.w(TAG, "updateUI mQuizData is null");
            return;
        }

        final List<Question> questions = mQuizData.getQuestions();
        if (questions == null || questions.isEmpty()) {
            Log.e(TAG, "updateUI there are no answers in this quiz !!!");
            // TODO show some error message to user
            return;
        }
        final List<Boolean> myAnswers = mQuizzesItem.getMyAnswers();

        // Check previous answers and start from new question
        final int currentQuestion;
        if (myAnswers != null) {
            if (myAnswers.size() <= questions.size()) {
                currentQuestion = mQuizzesItem.getMyAnswers().size();
            } else {
                currentQuestion = questions.size();
            }
        } else {
            currentQuestion = 0;
        }
        if (mQuizAppViewModel.getQuizActivityCurrentQuestion() == currentQuestion &&
                mQuizContentViewFlipper.getChildCount() > 0) {
            // there was no change in current question;
            // do nothing more;
            return;
        }
        Log.d(TAG, "mQuizData currentQuestion: " + currentQuestion);
        Log.d(TAG, "mQuizData mQuizContentViewFlipper.getChildCount(): " +
                mQuizContentViewFlipper.getChildCount());

        if (mAppBarProgressBar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mAppBarProgressBar.setProgress(currentQuestion, true);
            } else {
                mAppBarProgressBar.setProgress(currentQuestion);
            }
        }
        View newQuizContent;
        if (currentQuestion < questions.size()) {
            // prepare next question View
            newQuizContent = getQuizQuestionsContentView(currentQuestion);
        } else {
            Log.i(TAG, "updateUI, Quiz solved");
            newQuizContent = getQuizResolvedContentView();
        }
        if (newQuizContent != null) {
            mQuizContentViewFlipper.addView(newQuizContent);
            // Animate show next only when question changes or we initialize this Activity
            if (mQuizAppViewModel.getQuizActivityCurrentQuestion() != currentQuestion) {
                mQuizContentViewFlipper.showNext();
            }
            if (mQuizContentViewFlipper.getChildCount() > 2) {
                mQuizContentViewFlipper.removeViewAt(0);
            }
        }
        mQuizAppViewModel.setQuizActivityCurrentQuestion(currentQuestion);
    }

    @Nullable
    private View getQuizQuestionsContentView(int currentQuestion) {
        List<Question> questions = mQuizData.getQuestions();
        if (questions == null || currentQuestion >= questions.size()) {
            Log.e(TAG, "getQuizQuestionsContentView questions are empty or current question " +
                    "wrong value");
            return null;
        }
        View newQuizContent = LayoutInflater.from(this).inflate(R.layout.quiz_content_solve,
                mQuizContentRoot, false);

        for (Question q : questions) {
            for (Answer a : q.getAnswers()) {
                Image aImage = a.getImage();
                if (aImage != null) {
                    String aUrl = aImage.getUrl();
                    if (!TextUtils.isEmpty(aUrl)) {
                        Log.e(TAG, "getQuizQuestionsContentView there is image in Answer url: " +
                                aUrl);
                    }
                }
            }
        }
        // quiz questions content Components
        TextView questionTextView = newQuizContent.findViewById(R.id.question_title);
        questionTextView.setText(questions.get(currentQuestion).getText());

        ImageView imageView = newQuizContent.findViewById(R.id.question_image);
        Image image = questions.get(currentQuestion).getImage();
        if (image != null && !TextUtils.isEmpty(image.getUrl())) {
            Log.e(TAG, "getQuizQuestionsContentView image.getUrl(): " + image.getUrl());
            imageView.setVisibility(View.VISIBLE);
            GlideApp.with(this).load(image.getUrl())
                    .placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).fallback(R.mipmap
                    .ic_launcher).centerCrop().dontAnimate().into(imageView);

        }
        RadioGroup answersRadioGroup = newQuizContent.findViewById(R.id.answers_radio_group);
        answersRadioGroup.setOnCheckedChangeListener((group, checkedId) -> processAnswerSelected
                (checkedId));

        answersRadioGroup.removeAllViews();
        List<Answer> answers = questions.get(currentQuestion).getAnswers();
        if (answers.size() > 0) {
            for (int i = 0; i < answers.size(); i++) {
                RadioButton button = new RadioButton(this);
                button.setText(answers.get(i).getText());
                button.setId(i);
                answersRadioGroup.addView(button);
            }
        } else {
            Log.e(TAG, "updateUI there are no Answers");
            // TODO show some error message to user
        }
        return newQuizContent;
    }

    @NonNull
    private View getQuizResolvedContentView() {
        View newQuizContent = LayoutInflater.from(this).inflate(R.layout.quiz_content_resolved,
                mQuizContentRoot, false);

        // quiz result content Components
        TextView resultTextView = newQuizContent.findViewById(R.id.result_text);
        TextView userScoreTextView = newQuizContent.findViewById(R.id.result_score_title);
        TextView avgUserScoreTextView = newQuizContent.findViewById(R.id.result_avg_score_title);
        List<Boolean> myAnswers = mQuizzesItem.getMyAnswers();

        // Count correct answers
        int myCorrectAnswers = 0;
        for (Boolean b : myAnswers) {
            if (b) {
                myCorrectAnswers++;
            }
        }

        int questionsCount = mQuizData.getQuestions().size();

        // Calculate and update User score
        int score = Math.round(myCorrectAnswers / (float) questionsCount * 100);
        Log.d(TAG, "getQuizResolvedContentView score is: " + score);
        String scoreText = getString(R.string.quiz_result_score_title, myCorrectAnswers,
                questionsCount, score);
        userScoreTextView.setText(scoreText);

        // Calculate and update avarega user score
        int avgScore = (int) Math.round(mQuizData.getAvgResult() * 100);
        Log.d(TAG, "getQuizResolvedContentView avgScore is: " + avgScore);
        String avgScoreText = getString(R.string.quiz_result_avg_score_title, avgScore);
        avgUserScoreTextView.setText(String.valueOf(avgScoreText));

        // Get Result Text based on User score
        List<Rate> rates = mQuizData.getRates();
        for (Rate r : rates) {
            if (score >= r.getFrom() && score < r.getTo()) {
                resultTextView.setText(r.getContent());
                break;
            }
        }

        Button backButton = newQuizContent.findViewById(R.id.go_back_to_quiz_list_button);
        backButton.setOnClickListener(v -> {
            // show back image share transition animation
            supportFinishAfterTransition();
        });

        Button redoButton = newQuizContent.findViewById(R.id.redo_quiz_button);
        redoButton.setOnClickListener(v -> {
            mQuizzesItem.setMyAnswers(new ArrayList<>());
            // This will also call to refresh UI through DB LiveData observer
            mQuizAppViewModel.updateQuizzesItem(mQuizzesItem);
        });
        return newQuizContent;
    }

    /**
     * Processes Selection of particular question answer.
     * checks if user selection is correct answer and stores  his answer in DB through
     * {@link QuizAppViewModel}. Input checkedId is an intiger of an answer as mapped in
     * getQuizQuestionsContentView() method.
     *
     * @param checkedId id of selected question answer
     */
    private void processAnswerSelected(int checkedId) {
        Log.d(TAG, "processAnswerSelected checkedId: " + checkedId + mQuizData.getType());
        List<Boolean> myAnswers = mQuizzesItem.getMyAnswers();
        switch (mQuizData.getType()) {
            case Constants.TYPE_QUIZ:
                if (myAnswers == null) {
                    myAnswers = new ArrayList<>();
                }
                if (myAnswers.size() < mQuizData.getQuestions().size()) {
                    Integer isCorrect = mQuizData.getQuestions().get(myAnswers.size()).getAnswers().get
                            (checkedId).getIsCorrect();

                    if (isCorrect != null && isCorrect > 0) {
                        Log.d(TAG, "processAnswerSelected correct answer");
                        myAnswers.add(Boolean.TRUE);
                    } else {
                        Log.d(TAG, "processAnswerSelected wrong answer");
                        myAnswers.add(Boolean.FALSE);
                    }
                    mQuizzesItem.setMyAnswers(myAnswers);

                    mQuizAppViewModel.updateQuizzesItem(mQuizzesItem);

                    updateUI();
                } else {
                    Log.d(TAG, "processAnswerSelected Quiz solved");
                }
            break;
            case Constants.TYPE_TEST:
                mQuizzesItem.setMyPoll(checkedId);
                updateUI();
                break;
            case Constants.TYPE_POLL:

                break;
            default:
                // Hmm this should not happen
                Log.e(TAG, "processAnswerSelected wrong quiz type");
        }
    }
}
