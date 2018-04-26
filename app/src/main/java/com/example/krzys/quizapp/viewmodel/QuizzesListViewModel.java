package com.example.krzys.quizapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import android.arch.paging.PagedList;
import android.util.Log;

import com.example.krzys.quizapp.data.db.QuizAppRoomDatabase;
import com.example.krzys.quizapp.data.dto.quizzes.QuizzesItem;
import com.example.krzys.quizapp.repository.Listing;
import com.example.krzys.quizapp.repository.NetworkState;
import com.example.krzys.quizapp.repository.QuizAppRepository;
import com.example.krzys.quizapp.utils.Constants;
import com.example.krzys.quizapp.utils.Utils;

import java.util.List;

public class QuizzesListViewModel extends AndroidViewModel {

    private static final String TAG = Utils.getLogTag(QuizzesListViewModel.class);

    private final QuizAppRepository mRepository;

    private final LiveData<PagedList<QuizzesItem>> mQuizzesItemList;
    private final LiveData<NetworkState> mNetworkState;

    private final LiveData<NetworkState> mRefreshState;

    public QuizzesListViewModel(Application application) {
        super(application);
        Log.d(TAG, "QuizzesListViewModel");
        mRepository = QuizAppRepository.getInstance(QuizAppRoomDatabase.getDatabase(application));
        Listing<QuizzesItem> result = mRepository.getAllQuizzesItems(Constants.INITIAL_QUIZZES_GET_COUNT);
        mQuizzesItemList = result.mPagedList;
        mNetworkState = result.mNetworkState;
        mRefreshState = result.mRefreshState;
    }

    public LiveData<PagedList<QuizzesItem>> getQuizzesItemsListLiveData() {
        return mQuizzesItemList;
    }

    public LiveData<NetworkState> getNetworkStateLiveData() {
        return mNetworkState;
    }

    public LiveData<NetworkState> getRefreshStateLiveData() {
        return mRefreshState;
    }

    public void refreshQuizzes() {
        mRepository.refreshLoadAllQuizzes();
    }

    public void retryLoadQuizzes() {
        mRepository.retryLoadQuizzes();
    }

    // Currently only for logging issues
    public LiveData<List<String>> getAllQuizzesListTypes() {
        return mRepository.getAllQuizzesItemsTypes();
    }
}