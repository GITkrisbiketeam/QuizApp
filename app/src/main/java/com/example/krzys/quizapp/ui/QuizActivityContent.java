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
import com.example.krzys.quizapp.data.dto.quiz.QuizData;
import com.example.krzys.quizapp.data.dto.quizzes.QuizzesItem;
import com.example.krzys.quizapp.viewmodel.QuizViewModelQuestion;
import com.example.krzys.quizapp.viewmodel.QuizzesListViewModel;
import com.example.krzys.quizapp.utils.Constants;
import com.example.krzys.quizapp.utils.Utils;
import com.example.krzys.quizapp.viewmodel.QuizViewModel;

import java.util.ArrayList;
import java.util.List;

abstract class QuizActivityContent {
    private static final String TAG = Utils.getLogTag(QuizActivityContent.class);

    final AppCompatActivity mActivity;

    final ViewGroup mQuizContentRoot;

    private final ViewFlipper mQuizContentViewFlipper;

    private final QuizViewModel mQuizViewModel;

    private final QuizData mQuizData;
    private final QuizzesItem mQuizzesItem;

    QuizActivityContent(@NonNull AppCompatActivity activity, @NonNull QuizData quizData, @NonNull QuizzesItem quizzesItem) {
        mQuizzesItem = quizzesItem;
        mQuizData = quizData;

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

        mActivity = activity;
        updateUI();
    }

    final void updateUI() {
        if (mQuizData == null || mQuizzesItem == null){
            //No data to display yet do nothing;
            return;
        }
        QuizViewModelQuestion quizViewModel = new QuizViewModelQuestion(mQuizData, mQuizzesItem);

        Log.e(TAG, "updateUI quizViewModel: " + quizViewModel.toString());
        View newQuizContent;
        if (quizViewModel.isQuizSolved()) {
            Log.i(TAG, "updateUI, Quiz solved");
            newQuizContent = getQuizResolvedContentView(quizViewModel);
        } else {
            // prepare next question View
            newQuizContent = getQuizQuestionsContentView(quizViewModel);
        }

        if (newQuizContent != null) {
            mQuizContentViewFlipper.addView(newQuizContent);
            // Animate show next only when question changes or we initialize this Activity
            if (mQuizViewModel.getQuizActivityCurrentQuestion() != quizViewModel.getCurrentQuestionNum()) {
                mQuizContentViewFlipper.showNext();
            }
            if (mQuizContentViewFlipper.getChildCount() > 2) {
                mQuizContentViewFlipper.removeViewAt(0);
            }
        }
        mQuizViewModel.setQuizActivityCurrentQuestion(quizViewModel.getCurrentQuestionNum());
    }

    /**
     * Processes Selection of particular question answer.
     * checks if user selection is correct answer and stores  his answer in DB through
     * {@link QuizzesListViewModel}. Input checkedId is an integer of an answer as mapped in
     * getQuizQuestionsContentView() method.
     *
     * @param checkedId id of selected question answer
     */
    final void processAnswerSelected(int checkedId) {
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

    final void resetQuestion() {
        mQuizzesItem.setMyAnswers(new ArrayList<>());
        // This will also call to refresh UI through DB LiveData observer
        mQuizViewModel.updateQuizzesItem(mQuizzesItem);
    }

    final void returnFromQuestionActivity() {
        // show back image share transition animation
        mActivity.supportFinishAfterTransition();
    }

    @Nullable
    protected abstract View getQuizQuestionsContentView(@NonNull QuizViewModelQuestion quizViewModel);

    @NonNull
    protected abstract View getQuizResolvedContentView(@NonNull QuizViewModelQuestion quizViewModel);
}
