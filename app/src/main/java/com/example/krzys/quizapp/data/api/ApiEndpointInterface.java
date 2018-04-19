package com.example.krzys.quizapp.data.api;

import com.example.krzys.quizapp.data.model.quiz.QuizData;
import com.example.krzys.quizapp.data.model.quizzes.QuizzesListData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiEndpointInterface {

    /**
     * Get list of Quiz Data
     *
     * @param fromId from which item to start fetching
     * @param count  how many items to fetch
     * @return QuizzesListData item from which will be data retrieved
     */
    @GET("quizzes/{id}/{count}")
    Call<QuizzesListData> getQuizListData(@Path("id") int fromId, @Path("count") int count);

    /**
     * Get details of Quiz
     *
     * @param id id of the quiz to retrieve
     * @return QuizData with details of selected quiz
     */
    @GET("quiz/{id}/0")
    Call<QuizData> getQuizData(@Path("id") long id);
}