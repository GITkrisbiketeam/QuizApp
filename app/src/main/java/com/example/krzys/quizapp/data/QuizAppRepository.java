package com.example.krzys.quizapp.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.krzys.quizapp.data.db.QuizAppRoomDatabase;
import com.example.krzys.quizapp.data.db.dao.QuizDataDao;
import com.example.krzys.quizapp.data.db.dao.QuizzesItemDao;
import com.example.krzys.quizapp.data.model.quiz.QuizData;
import com.example.krzys.quizapp.data.model.quizzes.QuizzesItem;
import com.example.krzys.quizapp.data.model.quizzes.QuizzesListData;
import com.example.krzys.quizapp.data.retro.ApiEndpointInterface;
import com.example.krzys.quizapp.data.retro.RetrofitClient;
import com.example.krzys.quizapp.utils.Constants;
import com.example.krzys.quizapp.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository for managing DB and Network object retrieval.
 */
public class QuizAppRepository {
    private static final String TAG = Utils.getLogTag(QuizAppRepository.class.getSimpleName());

    private final QuizzesItemDao mQuizzesItemDao;
    private final QuizDataDao mQuizDataDao;

    private LiveData<List<QuizzesItem>> mAllQuizzesItems;

    public QuizAppRepository(Application application) {
        QuizAppRoomDatabase db = QuizAppRoomDatabase.getDatabase(application);
        mQuizzesItemDao = db.quizzesItemDao();
        mQuizDataDao = db.quizDataDao();
    }

    /**
     * See {@link QuizzesItemDao#getAllQuizzesItems()}
     * Additionally performing download of latest QuizzezItems through Retorfit
     *
     * @return
     */
    public LiveData<List<QuizzesItem>> getAllQuizzesItems() {
        //TODO: should we download Quizzes lst all the time????
        getNewQuizzes(0, Constants.INITIAL_QUIZZES_GET_COUNT);
         if (mAllQuizzesItems == null) {
            mAllQuizzesItems = mQuizzesItemDao.getAllQuizzesItems();
        }
        return mAllQuizzesItems;
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
        getQuizData(quizId);
        return mQuizDataDao.getQuizDataById(quizId);
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
     * Update given {@link QuizzesItem} into DB this happens after user selects given Answer
     *
     * @param quizzesItem   {@link QuizzesItem} to be updated in DB
     */
    public void updateQuizzesItem(@NonNull QuizzesItem quizzesItem) {
        new UpdateQuizzesItemAsyncTask(mQuizzesItemDao).execute(quizzesItem);
    }

    /**
     * Download {@link QuizzesItem} List with Retrofit and put them in DB. LiveData observer for
     * this List<QuizzesItem> will be then triggered. a number of {@link Constants#INITIAL_QUIZZES_GET_COUNT}
     * will be downloaded
     *
     * @param offset    from which offset to start download new items from API
     */
    public void getNewQuizzes(int offset, int count) {
        //Creating an object of our api interface
        ApiEndpointInterface api = RetrofitClient.getApiService();

        //Calling JSON
        Call<QuizzesListData> call = api.getQuizListData(offset, count);

        //Enqueue Callback will be call when get response...
        call.enqueue(new Callback<QuizzesListData>() {
            @Override
            public void onResponse(@NonNull Call<QuizzesListData> call, @NonNull
                    Response<QuizzesListData> response) {
                if (response.isSuccessful() && response.body() != null && response.body()
                        .getItems() != null) {
                    //Got Successfully
                    List<QuizzesItem> quizzesList = response.body().getItems();
                    Log.i(TAG, "getNewQuizzes RetrofitClient quizzesList.size:" + quizzesList
                            .size());
                    if (quizzesList.size() > 0) {
                        Log.i(TAG, "getNewQuizzes RetrofitClient item:" + quizzesList.get(0)
                                .getCreatedAt() + " " + quizzesList.get(0).getTitle());
                    }
                    // Add all items to Database
                    new InsertQuizzesItemAsyncTask(mQuizzesItemDao).execute(quizzesList.toArray
                            (new QuizzesItem[]{}));
                } else {
                    Log.w(TAG, "getNewQuizzes RetrofitClient onResponse response:" + response
                            .toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<QuizzesListData> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure call:" + call, t);
            }
        });
    }

    /**
     * Download QuizData Item with Retrofit and put it in DB. LiveData observer for this item
     * will be then triggered
     *
     * @param id    id of QuizData to download
     */
    public void getQuizData(long id) {
        ApiEndpointInterface api = RetrofitClient.getApiService();

        //Calling JSON
        Call<QuizData> call = api.getQuizData(id);

        //Enqueue Callback will be call when get response...
        call.enqueue(new Callback<QuizData>() {
            @Override
            public void onResponse(@NonNull Call<QuizData> call, @NonNull Response<QuizData>
                    response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.w(TAG, "getQuizData RetrofitClient onResponse response: " + response
                            .toString());
                    //Got Successfully
                    QuizData quizData = response.body();
                    if (quizData != null) {
                        Log.i(TAG, "got proper QuizData from RetrofitClient");
                        new InsertQuizDataAsyncTask(mQuizDataDao).execute(quizData);
                    }

                } else {
                    Log.w(TAG, "getQuizData RetrofitClient onResponse response:" + response
                            .toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<QuizData> call, @NonNull Throwable t) {
                Log.e(TAG, "getQuizData onFailure call:" + call, t);
            }
        });
    }

    /**
     * AsyncTask for inserting new {@link QuizzesItem}'s into DB restoring User previously given
     * myAnswers
     */
    private static class InsertQuizzesItemAsyncTask extends AsyncTask<QuizzesItem, Void, Void> {

        private final QuizzesItemDao mAsyncTaskDao;

        InsertQuizzesItemAsyncTask(QuizzesItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final QuizzesItem... items) {
            // Update QuizzesItem#myAnswers for newly downloaded QuizzesItems
            for (QuizzesItem item : items) {
                QuizzesItem oldItem = mAsyncTaskDao.getQuizzesItemByIdImmediate(item.getId());
                // restore my answers
                if (oldItem != null) {
                    item.setMyAnswers(oldItem.getMyAnswers());
                }
            }
            mAsyncTaskDao.insertQuizzesItem(items);
            return null;
        }

    }

    /**
     * AsyncTask for updating given {@link QuizzesItem} into DB this happens after user selects
     * given Answer
     */
    private static class UpdateQuizzesItemAsyncTask extends AsyncTask<QuizzesItem, Void, Void> {

        private final QuizzesItemDao mAsyncTaskQuizzesDao;


        UpdateQuizzesItemAsyncTask(QuizzesItemDao quizzesDao) {
            mAsyncTaskQuizzesDao = quizzesDao;
        }

        @Override
        protected Void doInBackground(final QuizzesItem... datas) {
            mAsyncTaskQuizzesDao.updateQuizzesItem(datas[0]);
            return null;
        }

    }

    /**
     * AsyncTask for inserting given {@link QuizData} into DB
     */
    private static class InsertQuizDataAsyncTask extends AsyncTask<QuizData, Void, Void> {

        private final QuizDataDao mAsyncTaskDao;

        InsertQuizDataAsyncTask(QuizDataDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final QuizData... items) {

            mAsyncTaskDao.insertQuizData(items[0]);
            return null;
        }

    }
}