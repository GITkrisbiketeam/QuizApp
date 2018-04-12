package com.example.krzys.quizapp.data.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.krzys.quizapp.R;
import com.example.krzys.quizapp.data.model.quizzes.QuizItem;
import com.example.krzys.quizapp.data.model.quizzes.QuizListData;
import com.example.krzys.quizapp.data.retro.ApiEndpointInterface;
import com.example.krzys.quizapp.data.retro.RetrofitClient;
import com.example.krzys.quizapp.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizListViewModel extends AndroidViewModel {

    private static final String TAG = Utils.getLogTag(QuizListViewModel.class.getName());

    private final LiveData<List<QuizItem>> quizItemsList;

    private final AppDatabase appDatabase;

    public QuizListViewModel(Application application) {
        super(application);
        Log.d(TAG, "QuizListViewModel");
        appDatabase = AppDatabase.getDatabase(application);

        quizItemsList = appDatabase.quizItemModel().getAllBorrowedItems();
        Log.d(TAG, "QuizListViewModel QuizItems browsed");

        getNewQuizzes(application);
        Log.d(TAG, "QuizListViewModel QuizItems retrofit requested");
    }

    private void getNewQuizzes(Application application) {
        Context context = application.getApplicationContext();
        if (Utils.checkConnection(context)) {
            //Creating an object of our api interface
            ApiEndpointInterface api = RetrofitClient.getApiService();

            //Calling JSON
            Call<QuizListData> call = api.getQuizListData(0, 100);

            //Enqueue Callback will be call when get response...
            call.enqueue(new Callback<QuizListData>() {
                @Override
                public void onResponse(@NonNull Call<QuizListData> call, @NonNull
                        Response<QuizListData> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.w(TAG, "RetrofitClient onResponse response:" + response.toString());
                        //Got Successfully
                        List<QuizItem> quizzesList = response.body().getItems();
                        // Add all items to Database
                        //TODO: add some method to add multiple items in one time
                        for (QuizItem item : quizzesList) {
                            Log.i(TAG, "RetrofitClient item:" + item.getCreatedAt() + " " + item
                                    .getTitle());
                            addItem(item);
                        }
                    } else {
                        //TODO: UI should not be here
                        //Snackbar.make(mRootView, R.string.string_some_thing_wrong, Snackbar
                        // .LENGTH_LONG).show();
                        Log.w(TAG, "RetrofitClient onResponse response:" + response.toString());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<QuizListData> call, @NonNull Throwable t) {
                    Utils.showToast(context, "Failed to load data");
                    Log.e(TAG, "onFailure call:" + call, t);
                }
            });

        } else {
            //Snackbar.make(mRootView, R.string.string_internet_connection_not_available,
            // Snackbar.LENGTH_LONG).show();
            Utils.showToast(context, context.getString(R.string
                    .string_internet_connection_not_available));

        }
    }

    public LiveData<List<QuizItem>> getQuizItemsList() {
        return quizItemsList;
    }

    public void addItem(QuizItem quizItem) {
        new AddAsyncTask(appDatabase).execute(quizItem);
    }

    public void deleteItem(QuizItem quizItem) {
        new DeleteAsyncTask(appDatabase).execute(quizItem);
    }

    private static class AddAsyncTask extends AsyncTask<QuizItem, Void, Void> {

        private final AppDatabase db;

        AddAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final QuizItem... params) {
            db.quizItemModel().addQuiz(params[0]);
            return null;
        }

    }

    private static class DeleteAsyncTask extends AsyncTask<QuizItem, Void, Void> {

        private final AppDatabase db;

        DeleteAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final QuizItem... params) {
            db.quizItemModel().deleteQuiz(params[0]);
            return null;
        }

    }

}