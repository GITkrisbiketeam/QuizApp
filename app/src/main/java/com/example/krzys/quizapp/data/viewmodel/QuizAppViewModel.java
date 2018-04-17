package com.example.krzys.quizapp.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.krzys.quizapp.data.QuizAppRepository;
import com.example.krzys.quizapp.data.livedata.ConnectionLiveData;
import com.example.krzys.quizapp.data.model.quiz.QuizData;
import com.example.krzys.quizapp.data.model.quizzes.QuizzesItem;
import com.example.krzys.quizapp.utils.Utils;

import java.util.List;

public class QuizAppViewModel extends AndroidViewModel {

    private static final String TAG = Utils.getLogTag(QuizAppViewModel.class.getSimpleName());

    private final QuizAppRepository mRepository;

    ConnectionLiveData mConnectionLiveData;
    private int mQuizActivityCurrentQuestion = -1;

    public QuizAppViewModel(Application application) {
        super(application);
        Log.d(TAG, "QuizAppViewModel");
        mRepository = new QuizAppRepository(application);
        Log.d(TAG, "QuizAppViewModel QuizItems retrofit requested");

        mConnectionLiveData = new ConnectionLiveData(application);
    }

    public LiveData<List<QuizzesItem>> getAllQuizzesList(@NonNull LifecycleOwner owner) {
        mConnectionLiveData.observe(owner, isConnected -> {
            if (isConnected) {
                mRepository.getNewQuizzes(0);
            }
        });
        return mRepository.getAllQuizzesItems();
    }

    public LiveData<List<String>> getAllQuizzesListTypes() {
        return mRepository.getAllQuizzesItemsTypes();
    }

    public void updateNewQuizzes(int offset) {
        mRepository.getNewQuizzes(offset);
    }

    public LiveData<QuizData> loadQuizData(@NonNull LifecycleOwner owner, final long quizId) {
        mConnectionLiveData.observe(owner, isConnected -> {
            if (isConnected) {
                mRepository.getQuizData(quizId);
            }
        });
        return mRepository.loadQuizData(quizId);
    }

    public void updateQuizzesItem(QuizzesItem quizzesItem) {
        mRepository.updateQuizzesItem(quizzesItem);
    }

    public int getQuizActivityCurrentQuestion() {
        return mQuizActivityCurrentQuestion;
    }

    public void setQuizActivityCurrentQuestion(int quizActivityCurrentQuestion) {
        mQuizActivityCurrentQuestion = quizActivityCurrentQuestion;
    }

    public LiveData<Boolean> isNetworkConnected() {
        return mConnectionLiveData;
    }
}