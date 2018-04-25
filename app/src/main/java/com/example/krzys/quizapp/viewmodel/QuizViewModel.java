package com.example.krzys.quizapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.krzys.quizapp.data.db.QuizAppRoomDatabase;
import com.example.krzys.quizapp.data.dto.quiz.QuizData;
import com.example.krzys.quizapp.data.dto.quizzes.QuizzesItem;
import com.example.krzys.quizapp.repository.QuizAppRepository;
import com.example.krzys.quizapp.repository.livedata.ConnectionLiveData;
import com.example.krzys.quizapp.utils.Utils;

public class QuizViewModel extends AndroidViewModel {

    //TODO add model and logic to store current question etc. no logic in UI should be present
    private static final String TAG = Utils.getLogTag(QuizViewModel.class.getSimpleName());

    private final QuizAppRepository mRepository;

    private final ConnectionLiveData mConnectionLiveData;

    private LiveData<QuizData> mQuizData;

    private LiveData<QuizzesItem> mQuizzesItem;

    private int mQuizActivityCurrentQuestion = -1;


    public QuizViewModel(Application application) {
        super(application);
        Log.d(TAG, "QuizViewModel");
        mRepository = QuizAppRepository.getInstance(QuizAppRoomDatabase.getDatabase(application));

        //TODO: will be still needed???
        mConnectionLiveData = new ConnectionLiveData(application);
    }

    public void init(@NonNull QuizzesItem item){
        if (mQuizzesItem == null){
            mQuizzesItem = mRepository.loadQuizzesItem(item.getId());
        }
        if (mQuizData == null){
            mQuizData = mRepository.loadQuizData(item.getId());
        }
    }

    public LiveData<QuizData> getQuizDataLiveData() {
        return mQuizData;
    }

    public LiveData<QuizzesItem> getQuizzesItemLiveData() {
        return mQuizzesItem;
    }





    public void updateQuizzesItem(QuizzesItem quizzesItem) {
        mRepository.updateQuizzesItem(quizzesItem);
    }


    //Getters and Setters
    public int getQuizActivityCurrentQuestion() {
        return mQuizActivityCurrentQuestion;
    }

    public void setQuizActivityCurrentQuestion(int quizActivityCurrentQuestion) {
        mQuizActivityCurrentQuestion = quizActivityCurrentQuestion;
    }


    /*@Deprecated
    public LiveData<QuizData> getQuizDataLiveData(@NonNull LifecycleOwner owner, final long quizId) {
        mConnectionLiveData.observe(owner, isConnected -> {
            if (isConnected != null && isConnected) {
                mRepository.getQuizDataFromApi(quizId);
            }
        });
        return mRepository.loadQuizData(quizId);
    }*/

    /*@Deprecated
    public LiveData<QuizzesItem> loadQuizzesItem(@NonNull LifecycleOwner owner, final long quizId) {
        mConnectionLiveData.observe(owner, isConnected -> {
            if (isConnected != null && isConnected) {
                mRepository.loadQuizzesItem(quizId);
            }
        });
        return mRepository.loadQuizzesItem(quizId);
    }*/
}