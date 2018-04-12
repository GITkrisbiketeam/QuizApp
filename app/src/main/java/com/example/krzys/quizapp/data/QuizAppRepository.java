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
import com.example.krzys.quizapp.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizAppRepository {
    private static final String TAG = Utils.getLogTag(QuizAppRepository.class.getName());


    private final QuizzesItemDao mQuizzesItemDao;
    private final QuizDataDao mQuizDataDao;
    private final LiveData<List<QuizzesItem>> mAllQuizzesItems;

    public QuizAppRepository(Application application) {
        QuizAppRoomDatabase db = QuizAppRoomDatabase.getDatabase(application);
        mQuizzesItemDao = db.quizzesItemDao();
        mQuizDataDao = db.quizDataDao();
        mAllQuizzesItems = mQuizzesItemDao.getAllQuizzesItems();
    }

    public LiveData<List<QuizzesItem>> getAllQuizzesItems() {
        //TODO: should we download Quizzes lst all the time????
        getNewQuizzes();
        return mAllQuizzesItems;
    }

    public LiveData<QuizData> loadQuizData(final long quizId) {
        getQuizData(quizId);
        return mQuizDataDao.getQuizDataById(quizId);
    }

    public void insert(QuizzesItem... items) {
        new InsertQuizzesItemAsyncTask(mQuizzesItemDao).execute(items);
    }

    public void getNewQuizzes() {
        //Creating an object of our api interface
        ApiEndpointInterface api = RetrofitClient.getApiService();

        //Calling JSON
        Call<QuizzesListData> call = api.getQuizListData(0, 100);

        //Enqueue Callback will be call when get response...
        call.enqueue(new Callback<QuizzesListData>() {
            @Override
            public void onResponse(@NonNull Call<QuizzesListData> call, @NonNull
                    Response<QuizzesListData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.w(TAG, "getNewQuizzes RetrofitClient onResponse response:" + response
                            .toString());
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
                    Log.w(TAG, "getQuizData RetrofitClient onResponse response:" + response
                            .toString());
                    //Got Successfully
                    QuizData quizData = response.body();
                    if (quizData != null) {
                        Log.i(TAG, "getQuizData RetrofitClient quizData:" + quizData.toString());
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

    private static class InsertQuizzesItemAsyncTask extends AsyncTask<QuizzesItem, Void, Void> {

        private final QuizzesItemDao mAsyncTaskDao;

        InsertQuizzesItemAsyncTask(QuizzesItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final QuizzesItem... items) {
            mAsyncTaskDao.addQuiz(items);
            return null;
        }

    }

    private static class InsertQuizDataAsyncTask extends AsyncTask<QuizData, Void, Void> {

        private final QuizDataDao mAsyncTaskDao;

        InsertQuizDataAsyncTask(QuizDataDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final QuizData... items) {
            mAsyncTaskDao.addQuiz(items);
            return null;
        }

    }

    private static class DeleteAsyncTask extends AsyncTask<QuizzesItem, Void, Void> {

        private final QuizzesItemDao mAsyncTaskDao;

        DeleteAsyncTask(QuizzesItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final QuizzesItem... params) {
            mAsyncTaskDao.deleteQuiz(params[0]);
            return null;
        }

    }


}
