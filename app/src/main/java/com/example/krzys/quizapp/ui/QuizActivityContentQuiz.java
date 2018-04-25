package com.example.krzys.quizapp.ui;

import android.graphics.drawable.Drawable;
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

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.krzys.quizapp.R;
import com.example.krzys.quizapp.data.dto.quiz.Answer;
import com.example.krzys.quizapp.data.dto.quiz.Image;
import com.example.krzys.quizapp.data.dto.quiz.Question;
import com.example.krzys.quizapp.data.dto.quiz.QuizData;
import com.example.krzys.quizapp.data.dto.quizzes.QuizzesItem;
import com.example.krzys.quizapp.utils.Utils;
import com.example.krzys.quizapp.viewmodel.QuizViewModelQuestion;

import java.util.ArrayList;
import java.util.List;

public class QuizActivityContentQuiz extends QuizActivityContent {
    private static final String TAG = Utils.getLogTag(QuizActivityContentQuiz.class.getSimpleName
            ());

    QuizActivityContentQuiz(@NonNull AppCompatActivity activity, @NonNull QuizData quizData, @NonNull QuizzesItem quizzesItem) {
        super(activity, quizData, quizzesItem);
    }

    @Nullable
    protected View getQuizQuestionsContentView(@NonNull QuizViewModelQuestion quizViewModel) {
        Question currQuestion = quizViewModel.getCurrentQuestion();
        if (currQuestion == null) {
            Log.e(TAG, "getQuizQuestionsContentView questions are empty or current question " +
                    "wrong value");
            return null;
        }

        View newQuizContent = LayoutInflater.from(mActivity).inflate(R.layout.quiz_content_solve,
                mQuizContentRoot, false);

        // quiz questions content Components
        TextView questionTextView = newQuizContent.findViewById(R.id.question_title);
        questionTextView.setText(currQuestion.getText());

        ImageView imageView = newQuizContent.findViewById(R.id.question_image);
        Image image = currQuestion.getImage();
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

        setupRadioButtonAnswers(currQuestion, answersRadioGroup);

        return newQuizContent;
    }

    private void setupRadioButtonAnswers(@NonNull Question question, @NonNull RadioGroup answersRadioGroup){
        List<Answer> answers = question.getAnswers();
        int paddingSize = -1;
        if (answers.size() > 0) {
            for (int i = 0; i < answers.size(); i++) {
                RadioButton button = new RadioButton(mActivity);
                button.setText(answers.get(i).getText());

                //Check if Answer has an image
                if (answers.get(i).getImage() != null && !TextUtils.isEmpty(answers.get(i).getImage().getUrl())) {
                    if (paddingSize < 0) {
                        paddingSize = mActivity.getResources().getDimensionPixelSize(R.dimen
                                .half_text_margin);
                    }
                    button.setPaddingRelative(paddingSize, paddingSize, paddingSize, paddingSize);
                    Log.d(TAG, "getQuizQuestionsContentView RadioButton load image");
                    GlideApp.with(mActivity).load(answers.get(i).getImage().getUrl()).
                            placeholder(R.mipmap.ic_launcher).
                            error(R.mipmap.ic_launcher).
                            fallback(R.mipmap.ic_launcher).
                            into(new SimpleTarget<Drawable>() {

                                @Override
                                public void onLoadStarted(@Nullable Drawable placeholder) {
                                    button.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                            placeholder, null);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                    button.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                            placeholder, null);
                                }

                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                    button.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                            errorDrawable, null);
                                }

                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable
                                        Transition<? super Drawable> transition) {
                                    button.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                            resource, null);

                                }
                            });
                }
                button.setId(i);
                answersRadioGroup.addView(button);
            }
        } else {
            Log.e(TAG, "updateUI there are no Answers");
            // TODO show some error message to user
        }
    }

    @NonNull
    protected View getQuizResolvedContentView(@NonNull QuizViewModelQuestion quizViewModel) {
        View newQuizContent = LayoutInflater.from(mActivity).inflate(R.layout
                .quiz_content_resolved, mQuizContentRoot, false);

        // quiz result content Components
        TextView resultTextView = newQuizContent.findViewById(R.id.result_text);
        TextView userScoreTextView = newQuizContent.findViewById(R.id.result_score_title);
        TextView avgUserScoreTextView = newQuizContent.findViewById(R.id.result_avg_score_title);

        Log.d(TAG, "getQuizResolvedContentView score is: " + quizViewModel.getScore());
        String scoreText = mActivity.getString(R.string.quiz_result_score_title,
                quizViewModel.getMyCorrectAnswers(), quizViewModel.getQuestionsCount(), quizViewModel.getScore());
        userScoreTextView.setText(scoreText);

        Log.d(TAG, "getQuizResolvedContentView avgScore is: " + quizViewModel.getAvgScore());
        String avgScoreText = mActivity.getString(R.string.quiz_result_avg_score_title, quizViewModel.getAvgScore());
        avgUserScoreTextView.setText(String.valueOf(avgScoreText));

        // Get Result Text based on User score
        String resultText = quizViewModel.getResultText();
        if (resultText != null) {
            resultTextView.setText(resultText);
        }

        Button backButton = newQuizContent.findViewById(R.id.go_back_to_quiz_list_button);
        backButton.setOnClickListener(v -> returnFromQuestionActivity());

        Button redoButton = newQuizContent.findViewById(R.id.redo_quiz_button);
        redoButton.setOnClickListener(v -> resetQuestion());
        return newQuizContent;
    }
}
