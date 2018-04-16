package com.example.krzys.quizapp.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.krzys.quizapp.data.QuizAppRepository;
import com.example.krzys.quizapp.data.model.quiz.QuizData;
import com.example.krzys.quizapp.data.model.quizzes.QuizzesItem;
import com.example.krzys.quizapp.utils.Utils;

import java.util.List;

public class QuizAppViewModel extends AndroidViewModel {

    private static final String TAG = Utils.getLogTag(QuizAppViewModel.class.getSimpleName());

    private final QuizAppRepository mRepository;

    private int mQuizActivityCurrentQuestion = -1;

    public QuizAppViewModel(Application application) {
        super(application);
        Log.d(TAG, "QuizAppViewModel");
        mRepository = new QuizAppRepository(application);
        Log.d(TAG, "QuizAppViewModel QuizItems retrofit requested");
    }

    public LiveData<List<QuizzesItem>> getAllQuizzesList() {
        return mRepository.getAllQuizzesItems();
    }

    public void updateNewQuizzes() {
        mRepository.getNewQuizzes();
    }

    public LiveData<QuizData> loadQuizData(final long quizId) {
        return mRepository.loadQuizData(quizId);
    }

    public void updateQuizData(QuizData quizData) {
        mRepository.updateQuizData(quizData);
    }

    public int getQuizActivityCurrentQuestion() {
        return mQuizActivityCurrentQuestion;
    }

    public void setQuizActivityCurrentQuestion(int quizActivityCurrentQuestion) {
        mQuizActivityCurrentQuestion = quizActivityCurrentQuestion;
    }


}