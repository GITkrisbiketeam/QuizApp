package com.example.krzys.quizapp.repository;

import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.krzys.quizapp.data.dto.quizzes.QuizzesItem;
import com.example.krzys.quizapp.data.dto.quizzes.QuizzesListData;
import com.example.krzys.quizapp.data.api.ApiEndpointInterface;
import com.example.krzys.quizapp.utils.Utils;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizzesPagingBoundaryCallback extends PagedList.BoundaryCallback<QuizzesItem> {
    private static final String TAG = Utils.getLogTag(QuizzesPagingBoundaryCallback.class
            .getSimpleName());

    private final Executor mExecutor;

    private final PagingRequestHelper mHelper;

    // Quiz API service, using Retrofit
    private final ApiEndpointInterface mQuizApi;

    private final ApiResponseCallback mApiResponseCallback;

    private final int mNetworkPageSize;

    @FunctionalInterface
    public interface ApiResponseCallback {
        void saveToDB(@NonNull QuizzesListData body);
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


    @Override
    public void onZeroItemsLoaded() {
        Log.d(TAG, "onZeroItemsLoaded");
        mHelper.runIfNotRunning(PagingRequestHelper.RequestType.BEFORE,
                helperCallback -> {
                    Log.d(TAG, "onZeroItemsLoaded run");
                    mQuizApi.getQuizListData(0, mNetworkPageSize).
                            enqueue(createWebserviceCallback(helperCallback));
                });
    }

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

    @Override
    public void onItemAtEndLoaded(@NonNull QuizzesItem itemAtEnd) {
        Log.d(TAG, "onItemAtEndLoaded itemAtEnd: " + itemAtEnd);
        mHelper.runIfNotRunning(PagingRequestHelper.RequestType.BEFORE,
                helperCallback -> {
                    Log.d(TAG, "onItemAtEndLoaded run");
                    mQuizApi.getQuizListData(itemAtEnd.getIndexInResponse(), mNetworkPageSize).
                            enqueue(createWebserviceCallback(helperCallback));
                });
    }

    private Callback<QuizzesListData> createWebserviceCallback(PagingRequestHelper.Request
                                                                       .Callback helperCallback) {
        return new Callback<QuizzesListData>() {
            @Override
            public void onResponse(Call<QuizzesListData> call,
                                   Response<QuizzesListData> response) {
                Log.d(TAG, "sucessfull got Quizzes from webservice");
                insertItemsIntoDb(response, helperCallback);
            }

            @Override
            public void onFailure(Call<QuizzesListData> call, Throwable t) {
                Log.w(TAG, "error getting Quizzes from webservice : " + t);
                helperCallback.recordFailure(t);
            }
        };
    }

    /**
     * every time it gets new items, boundary callback simply inserts them into the database and
     * paging library takes care of refreshing the list if necessary.
     */
    private void insertItemsIntoDb(@NonNull Response<QuizzesListData> response,
                                   @NonNull PagingRequestHelper.Request.Callback helperCallback) {
        mExecutor.execute(() -> {
            if (response.isSuccessful() && response.body() != null && response.body().getItems() != null){
                mApiResponseCallback.saveToDB(response.body());
                helperCallback.recordSuccess();
            } else {
                helperCallback.recordFailure(new Throwable());
            }
        });
    }
}