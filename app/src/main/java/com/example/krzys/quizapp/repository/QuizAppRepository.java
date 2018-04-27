package com.example.krzys.quizapp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.krzys.quizapp.AppExecutors;
import com.example.krzys.quizapp.data.api.ApiEndpointInterface;
import com.example.krzys.quizapp.data.api.RetrofitClient;
import com.example.krzys.quizapp.data.db.QuizAppRoomDatabase;
import com.example.krzys.quizapp.data.db.dao.QuizDataDao;
import com.example.krzys.quizapp.data.db.dao.QuizzesItemDao;
import com.example.krzys.quizapp.data.dto.quiz.QuizData;
import com.example.krzys.quizapp.data.dto.quizzes.QuizzesItem;
import com.example.krzys.quizapp.data.dto.quizzes.QuizzesListData;
import com.example.krzys.quizapp.utils.Constants;
import com.example.krzys.quizapp.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository for managing DB and Network object retrieval.
 *
 * TODO: Google Architecture suggest it to be Injection Dependency (Dagger2 is preferable)
 * For now we are using singleton
 */
public class QuizAppRepository {
    private static final String TAG = Utils.getLogTag(QuizAppRepository.class);

    private static final int DEFAULT_NETWORK_PAGE_SIZE = 10;

    private static final Object mLock = new Object();
    private static QuizAppRepository sInstance = null;

    private final QuizzesItemDao mQuizzesItemDao;
    private final QuizDataDao mQuizDataDao;

    private final ApiEndpointInterface mQuizApi;

    private final Executor mExecutor;

    private final PagingRequestHelper mPagingHelper;

    private MutableLiveData<Object> mRefreshTrigger;

    // should this be passe as constructor argument,  this should be bigger than the number of item that are visible on screen
    private final int mNetworkPageSize = DEFAULT_NETWORK_PAGE_SIZE;

    private QuizAppRepository(QuizAppRoomDatabase db) {
        Log.i(TAG, "QuizAppRepository created");
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
     */
    public Listing<QuizzesItem> getAllQuizzesItems(int pageSize) {
        QuizzesPagingBoundaryCallback boundaryCallback = new QuizzesPagingBoundaryCallback
                (mNetworkPageSize, mExecutor, mQuizApi, mPagingHelper, this::insertApiResultIntoDb);

        LiveData<PagedList<QuizzesItem>> allQuizzesItems =
                new LivePagedListBuilder<>(
                        mQuizzesItemDao.getAllQuizzesItems(),
                        pageSize)
//                        .setFetchExecutor(myNetworkExecutor)
                        .setBoundaryCallback(boundaryCallback)
                        .build();

        // we are using a mutable live data to trigger refresh requests which eventually calls
        // refresh method and gets a new live data. Each refresh request by the user becomes a newly
        // dispatched data in refreshTrigger
        mRefreshTrigger = new MutableLiveData<>();
        LiveData<NetworkState> refreshState = Transformations.switchMap(mRefreshTrigger, input -> {
            Log.i(TAG, "getAllQuizzesItems switchMap apply");
            return getQuizzesItemListFromApi(0, Constants.INITIAL_QUIZZES_GET_COUNT);

        });
        return new Listing<>(allQuizzesItems, mPagingHelper.createStatusLiveData(), refreshState);
    }

    /**
     * See {@link QuizzesItemDao#getTypes()}
     *
     */
    public LiveData<List<String>> getAllQuizzesItemsTypes() {
        return mQuizzesItemDao.getTypes();
    }


    /**
     * See {@link QuizzesItemDao#getQuizzesItemById(long)}
     *
     */
    public LiveData<QuizzesItem> loadQuizzesItem(final long quizId) {
        return mQuizzesItemDao.getQuizzesItemById(quizId);
    }

    /**
     * See {@link QuizDataDao#getQuizDataById(long)}
     * Additionally performing download of given QuizData through Retorfit
     *
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
    private LiveData<NetworkState> getQuizzesItemListFromApi(int offset, int count) {
        final MutableLiveData<NetworkState> networkState = new MutableLiveData<>();
        networkState.setValue(NetworkState.LOADING);
        Log.i(TAG, "getQuizzesItemListFromApi");

        //Calling JSON
        mQuizApi.getQuizListData(offset, /*count*/mNetworkPageSize)
                .enqueue(new Callback<QuizzesListData>() {
                    @Override
                    public void onResponse(@NonNull Call<QuizzesListData> call, @NonNull
                            Response<QuizzesListData> response) {
                        if (response.isSuccessful()) {
                            //Got Successfully
                            Log.i(TAG, "getQuizzesItemListFromApi RetrofitClient successful");
                            mExecutor.execute(() -> {
                                try {
                                    insertApiResultIntoDb(Objects.requireNonNull(response.body()), null);
                                    networkState.postValue(NetworkState.LOADED);
                                } catch (NullPointerException e) {
                                    networkState.postValue(
                                            NetworkState.error("Response body is null: " +
                                                    Utils.getApiResponseErrorMsg(response)));
                                }
                            });
                        } else {
                            // Something went wrong
                            Log.w(TAG, "getQuizzesItemListFromApi RetrofitClient failure");

                            networkState.postValue(NetworkState.error(Utils
                                    .getApiResponseErrorMsg(response)));
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
    private LiveData<NetworkState> getQuizDataFromApi(long id) {
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

    public void refreshLoadAllQuizzes(){
        mRefreshTrigger.setValue(null);
    }

    public void retryLoadQuizzes() {
        mPagingHelper.retryAllFailed();
    }

    /**
     * AsyncTask for inserting new {@link QuizzesItem}'s into DB restoring User previously given
     * myAnswers
     */
    private void insertApiResultIntoDb(@NonNull QuizzesListData body, @Nullable QuizzesItem itemAtEnd) {
        List<QuizzesItem> items = body.getItems();
        QuizzesItem[] toAdd;
        //For testing only
        List<Long> ignoreList = Arrays.asList(6245636871272577L, 6245344483329665L,
                6245311326165121L, 6245091174381697L, 6244623695296641L, 6244363836683905L,
                6244239181764737L, 6243959986935425L, 6243574662629505L);

        AtomicInteger atomIdx = new AtomicInteger(itemAtEnd != null ? mQuizzesItemDao.getNextIndexInQuizzesItems() : 0);
        Log.e(TAG, "insertApiResultIntoDb startIdx: " + atomIdx + " itemAtEnd: " + itemAtEnd);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            toAdd = items.stream().
                    filter(item -> mQuizzesItemDao.getQuizzesItemByIdImmediate(item.getId()) == null).
                    //filter(item -> !ignoreList.contains(item.getId())).
                    peek(item -> item.setIndexInResponse(atomIdx.getAndIncrement())).
                    toArray(QuizzesItem[]::new);
        } else {
            List<QuizzesItem> itemsToAdd = new ArrayList<>();
            for (QuizzesItem item : items) {
                QuizzesItem oldItem = mQuizzesItemDao.getQuizzesItemByIdImmediate(item.getId());
                Log.e(TAG, "insertApiResultIntoDb startIdx: " + atomIdx + " old: " + (oldItem != null ? oldItem.getIndexInResponse() : "null") + " " + item.getId());
                // restore my answers
                if (oldItem == null/* && !ignoreList.contains(item.getId())*/){
                    item.setIndexInResponse(atomIdx.getAndIncrement());
                    itemsToAdd.add(item);
                }
            }
            // new QuizzesItem[0]  is supposed to be faster but is it working on older devices???
            toAdd = itemsToAdd.toArray(new QuizzesItem[itemsToAdd.size()]);
        }

        Log.e(TAG, "insertApiResultIntoDb toAdd.size(): " + toAdd.length);
        if (itemAtEnd == null) {
            mQuizzesItemDao.addToQuizzesItemIdx(toAdd.length);
        }

        mQuizzesItemDao.insertQuizzesItem(toAdd);
    }
}