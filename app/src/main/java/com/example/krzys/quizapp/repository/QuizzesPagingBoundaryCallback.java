package com.example.krzys.quizapp.repository;

import android.arch.paging.PagedList;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.example.krzys.quizapp.data.dto.quizzes.QuizzesItem;
import com.example.krzys.quizapp.data.dto.quizzes.QuizzesListData;
import com.example.krzys.quizapp.data.api.ApiEndpointInterface;
import com.example.krzys.quizapp.utils.Utils;

import java.util.Objects;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class QuizzesPagingBoundaryCallback extends PagedList.BoundaryCallback<QuizzesItem> {
    private static final String TAG = Utils.getLogTag(QuizzesPagingBoundaryCallback.class);

    private final Executor mExecutor;

    private final PagingRequestHelper mHelper;

    // Quiz API service, using Retrofit
    private final ApiEndpointInterface mQuizApi;

    private final ApiResponseCallback mApiResponseCallback;

    private final int mNetworkPageSize;

    @FunctionalInterface
    public interface ApiResponseCallback {
        void saveToDB(@NonNull QuizzesListData body, @Nullable QuizzesItem itemAtEnd);
    }

    public QuizzesPagingBoundaryCallback(int networkPageSize, Executor executor,
                                         ApiEndpointInterface quizApi, PagingRequestHelper helper,
                                         ApiResponseCallback apiResponseCallback) {
        mNetworkPageSize = networkPageSize;
        mExecutor = executor;
        mQuizApi = quizApi;
        mApiResponseCallback = apiResponseCallback;

        mHelper = helper;
    }


    @MainThread
    @Override
    public void onZeroItemsLoaded() {
        Log.d(TAG, "onZeroItemsLoaded");
        mHelper.runIfNotRunning(PagingRequestHelper.RequestType.BEFORE,
                helperCallback -> {
                    Log.d(TAG, "onZeroItemsLoaded run");
                    mQuizApi.getQuizListData(0, mNetworkPageSize).
                            enqueue(createWebserviceCallback(helperCallback, null));
                });
    }

    @MainThread
    @Override
    public void onItemAtFrontLoaded(@NonNull QuizzesItem itemAtFront) {
        //Log.d(TAG, "onItemAtFrontLoaded itemAtFront: " + itemAtFront);
        // ignored, since we only ever append to what's in the DB
        /*mHelper.runIfNotRunning(PagingRequestHelper.RequestType.BEFORE,
                helperCallback -> {
                    Log.d(TAG, "onItemAtFrontLoaded run");
                    mQuizApi.getQuizListData(0, mNetworkPageSize).
                            enqueue(createWebserviceCallback(helperCallback));
                });*/
    }

    @MainThread
    @Override
    public void onItemAtEndLoaded(@NonNull QuizzesItem itemAtEnd) {
        Log.d(TAG, "onItemAtEndLoaded itemAtEnd: " + itemAtEnd);
        mHelper.runIfNotRunning(PagingRequestHelper.RequestType.BEFORE,
                helperCallback -> {
                    Log.d(TAG, "onItemAtEndLoaded run");
                    mQuizApi.getQuizListData(itemAtEnd.getIndexInResponse() + 1, mNetworkPageSize).
                            enqueue(createWebserviceCallback(helperCallback, itemAtEnd));
                });
    }

    @WorkerThread
    @NonNull
    private Callback<QuizzesListData> createWebserviceCallback(@NonNull PagingRequestHelper
            .Request.Callback helperCallback, @Nullable QuizzesItem itemAtEnd) {
        return new Callback<QuizzesListData>() {
            @Override
            public void onResponse(@NonNull Call<QuizzesListData> call,
                                   @NonNull Response<QuizzesListData> response) {
                Log.d(TAG, "successful got Quizzes from webservice");
                insertItemsIntoDb(response, itemAtEnd, helperCallback);
            }

            @Override
            public void onFailure(@NonNull Call<QuizzesListData> call, @NonNull Throwable t) {
                Log.w(TAG, "error getting Quizzes from webservice : " + t);
                helperCallback.recordFailure(t);
            }
        };
    }

    /**
     * every time it gets new items, boundary callback simply inserts them into the database and
     * paging library takes care of refreshing the list if necessary.
     */
    @WorkerThread
    private void insertItemsIntoDb(@NonNull Response<QuizzesListData> response, @Nullable
            QuizzesItem itemAtEnd, @NonNull PagingRequestHelper.Request.Callback helperCallback) {
        mExecutor.execute(() -> {
            if (response.isSuccessful()) {
                try {
                    mApiResponseCallback.saveToDB(Objects.requireNonNull(response.body()), itemAtEnd);
                    helperCallback.recordSuccess();
                } catch (NullPointerException e) {
                    helperCallback.recordFailure(new Throwable(e));
                }
            } else {
                helperCallback.recordFailure(new Throwable());
            }
        });
    }
}