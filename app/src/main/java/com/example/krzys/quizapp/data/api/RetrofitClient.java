package com.example.krzys.quizapp.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_QUIZ_URL = "http://quiz.o2.pl/api/v1/";

    /**
     * Get Retrofit Instance
     */
    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_QUIZ_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Get API Service
     *
     * @return API Service
     */
    public static ApiEndpointInterface getApiService() {
        return getRetrofitInstance().create(ApiEndpointInterface.class);
    }
}