package com.example.krzys.quizapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.example.krzys.quizapp.R;
import com.example.krzys.quizapp.data.model.quiz.Question;
import com.example.krzys.quizapp.data.model.quiz.QuizData;
import com.example.krzys.quizapp.data.model.quizzes.QuizzesItem;
import com.example.krzys.quizapp.viewmodel.QuizzesListViewModel;
import com.example.krzys.quizapp.utils.Constants;
import com.example.krzys.quizapp.utils.Utils;
import com.example.krzys.quizapp.viewmodel.QuizViewModel;

import java.util.ArrayList;
import java.util.List;

abstract class QuizActivityContent {
    private static final String TAG = Utils.getLogTag(QuizActivityContent.class.getSimpleName());

    protected AppCompatActivity mActivity;

    protected ViewGroup mQuizContentRoot;
    protected ViewFlipper mQuizContentViewFlipper;

    protected QuizViewModel mQuizViewModel;

    protected QuizData mQuizData;
    protected QuizzesItem mQuizzesItem;

    QuizActivityContent(AppCompatActivity activity, QuizzesItem quizzesItem) {
        mQuizzesItem = quizzesItem;

        mQuizContentRoot = activity.findViewById(R.id.quiz_content_root);

        mQuizContentViewFlipper = activity.findViewById(R.id.quiz_content_view_flipper);
        // set slide in Animation for ViewFlipper
        mQuizContentViewFlipper.setInAnimation(AnimationUtils.loadAnimation(activity, R.anim
                .slide_in_from_right));
        // set slide out Animation for ViewFlipper
        mQuizContentViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(activity, R.anim
                .slide_out_from_left));

        // Initialize ViewModel
        mQuizViewModel = ViewModelProviders.of(activity).get(QuizViewModel.class);

        mQuizViewModel.loadQuizData(activity, mQuizzesItem.getId()).observe(activity, quizData
                -> {
            Log.w(TAG, "QuizActivityContent observer onChanged quizData: " + quizData);
            if (quizData != null) {
                mQuizData = quizData;
                updateUI();
            } else if (!Utils.checkConnection(activity.getApplicationContext())) {
                Utils.showSnackbar(mQuizContentRoot, R.string
                        .string_internet_connection_not_available);
            }
        });

        mActivity = activity;
    }

    /* package-private */
    void updateUI() {
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
        final List<Integer> myAnswers = mQuizzesItem.getMyAnswers();

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
        if (mQuizViewModel.getQuizActivityCurrentQuestion() == currentQuestion &&
                mQuizContentViewFlipper.getChildCount() > 0) {
            // there was no change in current question;
            // do nothing more;
            return;
        }
        Log.d(TAG, "mQuizData currentQuestion: " + currentQuestion);
        Log.d(TAG, "mQuizData mQuizContentViewFlipper.getChildCount(): " +
                mQuizContentViewFlipper.getChildCount());

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
            if (mQuizViewModel.getQuizActivityCurrentQuestion() != currentQuestion) {
                mQuizContentViewFlipper.showNext();
            }
            if (mQuizContentViewFlipper.getChildCount() > 2) {
                mQuizContentViewFlipper.removeViewAt(0);
            }
        }
        mQuizViewModel.setQuizActivityCurrentQuestion(currentQuestion);
    }

    /**
     * Processes Selection of particular question answer.
     * checks if user selection is correct answer and stores  his answer in DB through
     * {@link QuizzesListViewModel}. Input checkedId is an intiger of an answer as mapped in
     * getQuizQuestionsContentView() method.
     *
     * @param checkedId id of selected question answer
     */
    protected void processAnswerSelected(int checkedId) {
        Log.d(TAG, "processAnswerSelected checkedId: " + checkedId + mQuizData.getType());
        List<Integer> myAnswers = mQuizzesItem.getMyAnswers();
        if (myAnswers == null) {
            myAnswers = new ArrayList<>();
        }
        switch (mQuizData.getType()) {
            case Constants.TYPE_QUIZ:
                if (myAnswers.size() < mQuizData.getQuestions().size()) {
                    Integer isCorrect = mQuizData.getQuestions().get(myAnswers.size()).getAnswers
                            ().get(checkedId).getIsCorrect();

                    if (isCorrect != null && isCorrect > 0) {
                        Log.d(TAG, "processAnswerSelected correct answer");
                        myAnswers.add(1);
                    } else {
                        Log.d(TAG, "processAnswerSelected wrong answer");
                        myAnswers.add(0);
                    }
                    mQuizzesItem.setMyAnswers(myAnswers);

                    mQuizViewModel.updateQuizzesItem(mQuizzesItem);
                } else {
                    Log.d(TAG, "processAnswerSelected Quiz solved");
                }
                break;
            case Constants.TYPE_TEST:
                //TODO:
                break;
            case Constants.TYPE_POLL:
                //TODO:
                break;
            default:
                // Hmm this should not happen
                Log.e(TAG, "processAnswerSelected wrong quiz type");
        }
    }

    @Nullable
    protected abstract View getQuizQuestionsContentView(int currentQuestion);

    @NonNull
    protected abstract View getQuizResolvedContentView();
}
