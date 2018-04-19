package com.example.krzys.quizapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.krzys.quizapp.repository.QuizAppRepository;
import com.example.krzys.quizapp.repository.livedata.ConnectionLiveData;
import com.example.krzys.quizapp.data.model.quizzes.QuizzesItem;
import com.example.krzys.quizapp.utils.Constants;
import com.example.krzys.quizapp.utils.Utils;

import java.util.List;

public class QuizzesListViewModel extends AndroidViewModel {

    private static final String TAG = Utils.getLogTag(QuizzesListViewModel.class.getSimpleName());

    private final QuizAppRepository mRepository;

    ConnectionLiveData mConnectionLiveData;

    public QuizzesListViewModel(Application application) {
        super(application);
        Log.d(TAG, "QuizzesListViewModel");
        mRepository = new QuizAppRepository(application);
        Log.d(TAG, "QuizzesListViewModel QuizItems retrofit requested");

        mConnectionLiveData = new ConnectionLiveData(application);
    }

    public LiveData<PagedList<QuizzesItem>> getAllQuizzesList(@NonNull LifecycleOwner owner) {
        mConnectionLiveData.observe(owner, isConnected -> {
            if (isConnected != null && isConnected) {
                mRepository.getNewQuizzes(0, Constants.INITIAL_QUIZZES_GET_COUNT);
            }
        });
        return mRepository.getAllQuizzesItems(Constants.INITIAL_QUIZZES_GET_COUNT);
    }

    public LiveData<List<String>> getAllQuizzesListTypes() {
        return mRepository.getAllQuizzesItemsTypes();
    }

    public void updateNewQuizzes(int offset, int count) {
        mRepository.getNewQuizzes(offset, count);
    }
}