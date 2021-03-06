package com.example.krzys.quizapp.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.krzys.quizapp.R;
import com.example.krzys.quizapp.data.model.quiz.Answer;
import com.example.krzys.quizapp.data.model.quiz.Image;
import com.example.krzys.quizapp.data.model.quiz.Question;
import com.example.krzys.quizapp.data.model.quiz.Rate;
import com.example.krzys.quizapp.data.model.quizzes.QuizzesItem;
import com.example.krzys.quizapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class QuizActivityContentQuiz extends QuizActivityContent {
    private static final String TAG = Utils.getLogTag(QuizActivityContentQuiz.class.getSimpleName
            ());

    QuizActivityContentQuiz(AppCompatActivity activity, QuizzesItem quizzesItem) {
        super(activity, quizzesItem);
    }

    @Nullable
    protected View getQuizQuestionsContentView(int currentQuestion) {
        List<Question> questions = mQuizData.getQuestions();
        if (questions == null || currentQuestion >= questions.size()) {
            Log.e(TAG, "getQuizQuestionsContentView questions are empty or current question " +
                    "wrong value");
            return null;
        }
        View newQuizContent = LayoutInflater.from(mActivity).inflate(R.layout.quiz_content_solve,
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
            GlideApp.with(mActivity).load(image.getUrl()).placeholder(R.mipmap.ic_launcher).error
                    (R.mipmap.ic_launcher).fallback(R.mipmap.ic_launcher).centerCrop()
                    .dontAnimate().into(imageView);

        }
        RadioGroup answersRadioGroup = newQuizContent.findViewById(R.id.answers_radio_group);
        answersRadioGroup.setOnCheckedChangeListener((group, checkedId) -> processAnswerSelected
                (checkedId));

        answersRadioGroup.removeAllViews();
        List<Answer> answers = questions.get(currentQuestion).getAnswers();
        if (answers.size() > 0) {
            for (int i = 0; i < answers.size(); i++) {
                RadioButton button = new RadioButton(mActivity);
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
    protected View getQuizResolvedContentView() {
        View newQuizContent = LayoutInflater.from(mActivity).inflate(R.layout
                .quiz_content_resolved, mQuizContentRoot, false);

        // quiz result content Components
        TextView resultTextView = newQuizContent.findViewById(R.id.result_text);
        TextView userScoreTextView = newQuizContent.findViewById(R.id.result_score_title);
        TextView avgUserScoreTextView = newQuizContent.findViewById(R.id.result_avg_score_title);
        List<Integer> myAnswers = mQuizzesItem.getMyAnswers();

        // Count correct answers
        int myCorrectAnswers = 0;
        for (Integer b : myAnswers) {
            if (b == 1) {
                myCorrectAnswers++;
            }
        }

        int questionsCount = mQuizData.getQuestions().size();

        // Calculate and update User score
        int score = Math.round(myCorrectAnswers / (float) questionsCount * 100);
        Log.d(TAG, "getQuizResolvedContentView score is: " + score);
        String scoreText = mActivity.getString(R.string.quiz_result_score_title,
                myCorrectAnswers, questionsCount, score);
        userScoreTextView.setText(scoreText);

        // Calculate and update avarega user score
        int avgScore = (int) Math.round(mQuizData.getAvgResult() * 100);
        Log.d(TAG, "getQuizResolvedContentView avgScore is: " + avgScore);
        String avgScoreText = mActivity.getString(R.string.quiz_result_avg_score_title, avgScore);
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
            mActivity.supportFinishAfterTransition();
        });

        Button redoButton = newQuizContent.findViewById(R.id.redo_quiz_button);
        redoButton.setOnClickListener(v -> {
            mQuizzesItem.setMyAnswers(new ArrayList<>());
            // This will also call to refresh UI through DB LiveData observer
            mQuizAppViewModel.updateQuizzesItem(mQuizzesItem);
        });
        return newQuizContent;
    }


}
