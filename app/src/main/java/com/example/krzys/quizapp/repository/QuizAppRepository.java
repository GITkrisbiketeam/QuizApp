package com.example.krzys.quizapp.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.krzys.quizapp.AppExecutors;
import com.example.krzys.quizapp.data.db.QuizAppRoomDatabase;
import com.example.krzys.quizapp.data.db.dao.QuizDataDao;
import com.example.krzys.quizapp.data.db.dao.QuizzesItemDao;
import com.example.krzys.quizapp.data.dto.quiz.QuizData;
import com.example.krzys.quizapp.data.dto.quizzes.QuizzesItem;
import com.example.krzys.quizapp.data.dto.quizzes.QuizzesListData;
import com.example.krzys.quizapp.data.api.ApiEndpointInterface;
import com.example.krzys.quizapp.data.api.RetrofitClient;
import com.example.krzys.quizapp.utils.Constants;
import com.example.krzys.quizapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository for managing DB and Network object retrieval.
 *
 * TODO: Google Architecture suggest it to be Incection Dependency (Dagger2 is preferable)
 * For now we are using singleton
 */
public class QuizAppRepository {
    private static final String TAG = Utils.getLogTag(QuizAppRepository.class.getSimpleName());

    private static final int DEFAULT_NETWORK_PAGE_SIZE = 10;

    private static final Object mLock = new Object();
    private static QuizAppRepository sInstance = null;

    QuizAppRoomDatabase mDb;
    private final QuizzesItemDao mQuizzesItemDao;
    private final QuizDataDao mQuizDataDao;

    private final ApiEndpointInterface mQuizApi;

    private final Executor mExecutor;

    private final PagingRequestHelper mPagingHelper;

    private final MutableLiveData<PagingRequestHelper.Status> mPagingRequestHelperStatus = new MutableLiveData<>();

    // should this be passe as constructor argument,  this should be bigger than the number of item that are visible on screen
    private final int mNetworkPageSize = DEFAULT_NETWORK_PAGE_SIZE;

    public QuizAppRepository(QuizAppRoomDatabase db) {
        Log.i(TAG, "QuizAppRepository created");
        mDb = db;
        mQuizzesItemDao = db.quizzesItemDao();
        mQuizDataDao = db.quizDataDao();

        //Creating an object of our api interface
        mQuizApi = RetrofitClient.getApiService();

        mExecutor = AppExecutors.getInstance().diskIO();

        mPagingHelper =  new PagingRequestHelper(mExecutor);

        // should this be passe as constructor argument
        //mNetworkPageSize;
    }

    public static QuizAppRepository getInstance(QuizAppRoomDatabase db){
        if(sInstance == null){
            synchronized (mLock) {
                if (sInstance == null) {
                    sInstance = new QuizAppRepository(db);
                }
            }
        }
        return sInstance;
    }

    /**
     * TODO:
     *
     * @return
     */
    public LiveData<PagedList<QuizzesItem>> getAllQuizzesItems(int pageSize) {
        QuizzesPagingBoundaryCallback boundaryCallback = new QuizzesPagingBoundaryCallback
                (mNetworkPageSize, mExecutor, mQuizApi, mPagingHelper, (QuizzesListData body) ->
                        insertApiResultIntoDb(body));

        LiveData<PagedList<QuizzesItem>> allQuizzesItems =
                new LivePagedListBuilder<>(
                        mQuizzesItemDao.getAllQuizzesItems(),
                        pageSize)
//                        .setFetchExecutor(myNetworkExecutor)
                        .setBoundaryCallback(boundaryCallback)
                        .build();

        return allQuizzesItems;
    }

    /**
     * See {@link QuizzesItemDao#getTypes()}
     *
     * @return
     */
    public LiveData<List<String>> getAllQuizzesItemsTypes() {
        return mQuizzesItemDao.getTypes();
    }


    /**
     * See {@link QuizzesItemDao#getQuizzesItemById(long)}
     *
     * @return
     */
    public LiveData<QuizzesItem> loadQuizzesItem(final long quizId) {
        return mQuizzesItemDao.getQuizzesItemById(quizId);
    }

    /**
     * See {@link QuizDataDao#getQuizDataById(long)}
     * Additionally performing download of given QuizData through Retorfit
     *
     * @return
     */
    public LiveData<QuizData> loadQuizData(final long quizId) {
        getQuizDataFromApi(quizId);
        return mQuizDataDao.getQuizDataById(quizId);
    }

    /**
     * Update given {@link QuizzesItem} into DB this happens after user selects given Answer
     *
     * @param quizzesItem   {@link QuizzesItem} to be updated in DB
     */
    public void updateQuizzesItem(@NonNull QuizzesItem quizzesItem) {
        mExecutor.execute(()->mQuizzesItemDao.updateQuizzesItem(quizzesItem));
    }

    /**
     * Download {@link QuizzesItem} List with Retrofit and put them in DB. LiveData observer for
     * this List<QuizzesItem> will be then triggered. a number of {@link Constants#INITIAL_QUIZZES_GET_COUNT}
     * will be downloaded
     *
     * @param offset    from which offset to start download new items from API
     */
    public LiveData getQuizzesItemListFromApi(int offset, int count) {
        final MutableLiveData<NetworkState> networkState = new MutableLiveData<>();
        networkState.setValue(NetworkState.LOADING);
        Log.i(TAG, "getQuizzesItemListFromApi");

        //Calling JSON
        mQuizApi.getQuizListData(offset, /*count*/mNetworkPageSize)
                .enqueue(new Callback<QuizzesListData>() {
                    @Override
                    public void onResponse(@NonNull Call<QuizzesListData> call, @NonNull
                            Response<QuizzesListData> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getItems() != null) {
                            //Got Successfully
                            Log.i(TAG, "getQuizzesItemListFromApi RetrofitClient successful body: " + response.body());
                            mExecutor.execute(() -> {
                                insertApiResultIntoDb(response.body());
                                networkState.postValue(NetworkState.LOADED);
                            });
                        } else {
                            // Something went wrong
                            Log.w(TAG, "getQuizzesItemListFromApi RetrofitClient failure response:" + response
                                    .toString());
                            networkState.postValue(NetworkState.error(response.message()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<QuizzesListData> call, @NonNull Throwable t) {
                        Log.e(TAG, "onFailure call:" + call, t);
                        networkState.postValue(NetworkState.error(t.getMessage()));
                    }
        });
        return networkState;
    }

    /**
     * Download QuizData Item with Retrofit and put it in DB. LiveData observer for this item
     * will be then triggered
     *
     * @param id    id of QuizData to download
     */
    public LiveData getQuizDataFromApi(long id) {
        final MutableLiveData<NetworkState> networkState = new MutableLiveData<>();
        networkState.setValue(NetworkState.LOADING);

        mQuizApi.getQuizData(id).enqueue(new Callback<QuizData>() {
            @Override
            public void onResponse(@NonNull Call<QuizData> call, @NonNull Response<QuizData>
                    response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.w(TAG, "getQuizDataFromApi RetrofitClient onResponse response: " + response
                            .toString());
                    //Got Successfully
                    QuizData quizData = response.body();
                    if (quizData != null) {
                        Log.i(TAG, "got proper QuizData from RetrofitClient");
                        mExecutor.execute(() -> {
                            mQuizDataDao.insertQuizData(quizData);
                            networkState.postValue(NetworkState.LOADED);
                        });
                    }
                } else {
                    // Something went wrong
                    Log.w(TAG, "getQuizDataFromApi RetrofitClient onResponse response:" + response
                            .toString());
                    networkState.postValue(NetworkState.error(response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<QuizData> call, @NonNull Throwable t) {
                Log.e(TAG, "getQuizDataFromApi onFailure call:" + call, t);
                networkState.postValue(NetworkState.error(t.getMessage()));
            }
        });
        return networkState;
    }

    public void retryLoadQuizzes() {
        mPagingHelper.retryAllFailed();
    }

    /**
     * AsyncTask for inserting new {@link QuizzesItem}'s into DB restoring User previously given
     * myAnswers
     */
    private void insertApiResultIntoDb(@NonNull QuizzesListData body) {
        List<QuizzesItem> items = body.getItems();
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            items.forEach(quizzesItem -> {
                QuizzesItem oldItem = mQuizzesItemDao.getQuizzesItemByIdImmediate(quizzesItem
                        .getId());
                // restore my answers
                if (oldItem != null) {
                    quizzesItem.setMyAnswers(oldItem.getMyAnswers());
                }
            });
            mQuizzesItemDao.insertQuizzesItem(items.stream().toArray(QuizzesItem[]::new));
        } else {*/
        int startIdx = mQuizzesItemDao.getNextIndexInQuizzesItems();
        List<QuizzesItem> itemsToAdd = new ArrayList<>();
        for (QuizzesItem item : items) {
            QuizzesItem oldItem = mQuizzesItemDao.getQuizzesItemByIdImmediate(item.getId());
            Log.e(TAG, "insertApiResultIntoDb startIdx: " + startIdx + " old: " + (oldItem != null ? oldItem.getIndexInResponse() : "null") + " " + item.getId());
            // restore my answers
            if (oldItem != null) {
                item.setMyAnswers(oldItem.getMyAnswers());
            }
            if (oldItem == null || oldItem.getMyAnswers() != null || oldItem.getId() < startIdx){
                item.setIndexInResponse(startIdx++);
                itemsToAdd.add(item);
            }
        }
        Log.e(TAG, "insertApiResultIntoDb itemsToAdd.size(): " + itemsToAdd.size());
        // new QuizzesItem[0]  is supposed to be faster but is it working on older devices???
        mQuizzesItemDao.insertQuizzesItem(itemsToAdd.toArray(new QuizzesItem[itemsToAdd.size()]));
        //}
    }
}