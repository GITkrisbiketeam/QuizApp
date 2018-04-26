package com.example.krzys.quizapp.viewmodel;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.krzys.quizapp.data.dto.quiz.Question;
import com.example.krzys.quizapp.data.dto.quiz.QuizData;
import com.example.krzys.quizapp.data.dto.quiz.Rate;
import com.example.krzys.quizapp.data.dto.quizzes.QuizzesItem;

import java.util.List;

public class QuizViewModelQuestion {
    private final int mCurrentQuestion;

    private final int mMyCorrectAnswers;

    private int mScore = -1;

    private int mAvgScore = -1;

    private final List<Question> mQuestions;
    private final int mQuestionsCount;
    private final List<Integer> mMyAnswers;
    private final List<Rate> mRates;


    public QuizViewModelQuestion(@NonNull QuizData quizData, @NonNull QuizzesItem quizzesitem) {
        mQuestions = quizData.getQuestions();
        mRates = quizData.getRates();
        mMyAnswers = quizzesitem.getMyAnswers();

        mQuestionsCount = mQuestions.size();

        // Check previous answers and start from new question
        if (mMyAnswers != null && mMyAnswers.size() <= mQuestions.size()) {
            mCurrentQuestion = mMyAnswers.size();
            // Count correct answers
            int myCorrectAnswers = 0;
            for (Integer b : mMyAnswers) {
                if (b == 1) {
                    myCorrectAnswers++;
                }
            }
            mMyCorrectAnswers = myCorrectAnswers;

        } else {
            mCurrentQuestion = 0;
            mMyCorrectAnswers = 0;
        }


        if (isQuizSolved()) {
            // Calculate and update User score
            mScore = Math.round(mMyCorrectAnswers / (float) mQuestions.size() * 100);
            // Calculate and update average user score
            mAvgScore = (int) Math.round(quizData.getAvgResult() * 100);
        }

    }

    public int getCurrentQuestionNum() {
        return mCurrentQuestion;
    }

    public int getMyCorrectAnswers() {
        return mMyCorrectAnswers;
    }

    public int getScore() {
        return mScore;
    }

    public int getAvgScore() {
        return mAvgScore;
    }

    public int getQuestionsCount() {
        return mQuestionsCount;
    }

    @Nullable
    public Question getQuestion(@IntRange(from = 0) int questionNum) {
        return questionNum >= 0 && questionNum < mQuestions.size() ? mQuestions.get(questionNum)
                : null;
    }

    @Nullable
    public Question getCurrentQuestion() {
        return mCurrentQuestion < mQuestions.size() ? mQuestions.get(mCurrentQuestion) : null;
    }

    public boolean isQuizSolved() {
        return mCurrentQuestion == mQuestions.size();
    }

    @Nullable
    public String getResultText() {
        // Get Result Text based on User score
        if (mRates != null && mScore >= 0) {
            for (Rate r : mRates) {
                if (mScore >= r.getFrom() && mScore < r.getTo()) {
                    return r.getContent();
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "QuizViewModelQuestion: [mCurrentQuestion: " + mCurrentQuestion +
                ", mMyCorrectAnswers: " + mMyCorrectAnswers +
                ", mScore: " + mScore +
                ", mAvgScore: " + mAvgScore +
                ", mQuestions: " + (mQuestions != null ? String.valueOf(mQuestions.size()) :
                "null") +
                ", mMyAnswers: " + (mMyAnswers != null ? String.valueOf(mMyAnswers.size()) :
                "null") +
                ", mRates: " + (mRates != null ? String.valueOf(mRates.size()) : "null") + "]";
    }
}
