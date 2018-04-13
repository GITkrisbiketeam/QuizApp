package com.example.krzys.quizapp.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.krzys.quizapp.data.QuizAppRepository;
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

public class QuizAppViewModel extends AndroidViewModel {

    private static final String TAG = Utils.getLogTag(QuizAppViewModel.class.getName());

    private final QuizAppRepository mRepository;
    private final LiveData<List<QuizzesItem>> mAllQuizzesList;

    private int mQuizActivityCurrentQuestion = -1;

    public QuizAppViewModel(Application application) {
        super(application);
        Log.d(TAG, "QuizAppViewModel");
        mRepository = new QuizAppRepository(application);
        mAllQuizzesList = mRepository.getAllQuizzesItems();

        Log.d(TAG, "QuizAppViewModel mAllQuizzesList.getValue().size(): " + (mAllQuizzesList
                .getValue() != null ? mAllQuizzesList.getValue().size() : -1));

        Log.d(TAG, "QuizAppViewModel QuizItems retrofit requested");
    }

    public void getNewQuizzes(Application application) {
        Context context = application.getApplicationContext();
        if (Utils.checkConnection(context)) {
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
                        /*new AddAsyncTask(quizAppRoomDatabase).execute(quizzesList.toArray(new
                                QuizzesItem[]{}));*/
                        // Add all items to Database
                        //TODO: add some method to add multiple items in one time
                        for (QuizzesItem item : quizzesList) {
                            /*Log.i(TAG, "getNewQuizzes RetrofitClient item:" + item.getCreatedAt()
                                    + " " + item.getTitle());*/
                            insert(item);
                        }
                    } else {
                        //TODO: UI should not be here
                        //Snackbar.make(mRootView, R.string.string_some_thing_wrong, Snackbar
                        // .LENGTH_LONG).show();
                        Log.w(TAG, "getNewQuizzes RetrofitClient onResponse response:" + response
                                .toString());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<QuizzesListData> call, @NonNull Throwable t) {
                    Utils.showToast(context, "Failed to load data");
                    Log.e(TAG, "onFailure call:" + call, t);
                }
            });

        } else {
            Log.e(TAG, "no Internet connection");
        }
    }

    public LiveData<List<QuizzesItem>> getAllQuizzesList() {
        return mAllQuizzesList;
    }

    public LiveData<QuizData> loadQuizData(final long quizId) {
        return mRepository.loadQuizData(quizId);
    }

    public void insert(QuizzesItem item) { mRepository.insert(item); }

    public void updateQuizData(QuizData... quizData) {
        mRepository.updateQuizData(quizData);
    }

    public int getQuizActivityCurrentQuestion() {
        return mQuizActivityCurrentQuestion;
    }

    public void setQuizActivityCurrentQuestion(int quizActivityCurrentQuestion) {
        mQuizActivityCurrentQuestion = quizActivityCurrentQuestion;
    }


}