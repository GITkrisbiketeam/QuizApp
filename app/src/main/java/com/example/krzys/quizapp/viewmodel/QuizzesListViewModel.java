package com.example.krzys.quizapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.example.krzys.quizapp.repository.QuizAppRepository;
import com.example.krzys.quizapp.repository.livedata.ConnectionLiveData;
import com.example.krzys.quizapp.data.dto.quizzes.QuizzesItem;
import com.example.krzys.quizapp.utils.Constants;
import com.example.krzys.quizapp.utils.Utils;

import java.util.List;

public class QuizzesListViewModel extends AndroidViewModel {

    private static final String TAG = Utils.getLogTag(QuizzesListViewModel.class.getSimpleName());

    private final QuizAppRepository mRepository;

    private final ConnectionLiveData mConnectionLiveData;

    private LiveData<PagedList<QuizzesItem>> mQuizzesItemList;

    public QuizzesListViewModel(Application application) {
        super(application);
        Log.d(TAG, "QuizzesListViewModel");
        mRepository = QuizAppRepository.getInstance(application);

        mQuizzesItemList = mRepository.getAllQuizzesItems(Constants.INITIAL_QUIZZES_GET_COUNT);
        mConnectionLiveData = new ConnectionLiveData(application);
    }

    public LiveData<PagedList<QuizzesItem>> getQuizzesItemsListLiveData() {
        return mQuizzesItemList;
    }

    public ConnectionLiveData getConnectionLiveData() {
        return mConnectionLiveData;
    }

    @VisibleForTesting
    public LiveData<List<String>> getAllQuizzesListTypes() {
        return mRepository.getAllQuizzesItemsTypes();
    }

    public void refreshQuizzes() {
        mRepository.getQuizzesItemListFromApi(0, Constants.INITIAL_QUIZZES_GET_COUNT);
    }

    public void retryLoadQuizzes() {
        mRepository.retryLoadQuizzes();
    }
}