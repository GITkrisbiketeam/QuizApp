package com.example.krzys.quizapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.krzys.quizapp.data.model.quiz.QuizData;
import com.example.krzys.quizapp.data.model.quizzes.QuizzesItem;
import com.example.krzys.quizapp.repository.QuizAppRepository;
import com.example.krzys.quizapp.repository.livedata.ConnectionLiveData;
import com.example.krzys.quizapp.utils.Utils;

public class QuizViewModel extends AndroidViewModel {

    private static final String TAG = Utils.getLogTag(QuizViewModel.class.getSimpleName());

    private final QuizAppRepository mRepository;

    ConnectionLiveData mConnectionLiveData;
    private int mQuizActivityCurrentQuestion = -1;

    public QuizViewModel(Application application) {
        super(application);
        Log.d(TAG, "QuizzesListViewModel");
        mRepository = new QuizAppRepository(application);
        Log.d(TAG, "QuizzesListViewModel QuizItems retrofit requested");

        mConnectionLiveData = new ConnectionLiveData(application);
    }

    public LiveData<QuizzesItem> loadQuizzesItem(@NonNull LifecycleOwner owner, final long quizId) {
        mConnectionLiveData.observe(owner, isConnected -> {
            if (isConnected != null && isConnected) {
                mRepository.loadQuizzesItem(quizId);
            }
        });
        return mRepository.loadQuizzesItem(quizId);
    }

   public LiveData<QuizData> loadQuizData(@NonNull LifecycleOwner owner, final long quizId) {
        mConnectionLiveData.observe(owner, isConnected -> {
            if (isConnected != null && isConnected) {
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
}