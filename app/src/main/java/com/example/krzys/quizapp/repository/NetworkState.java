package com.example.krzys.quizapp.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.krzys.quizapp.repository.NetworkState.Status.FAILED;
import static com.example.krzys.quizapp.repository.NetworkState.Status.RUNNING;
import static com.example.krzys.quizapp.repository.NetworkState.Status.SUCCESS;


public class NetworkState {

    @NonNull
    public static final NetworkState LOADED = new NetworkState(SUCCESS);
    @NonNull
    public static final NetworkState LOADING = new NetworkState(RUNNING);

    @NonNull
    private Status status;
    @NonNull
    private String msg;

    enum Status {
        RUNNING,
        SUCCESS,
        FAILED
    }

    public NetworkState(@NonNull Status status) {
        this.status = status;
    }

    public NetworkState(@NonNull Status status, @NonNull String msg) {
        this.status = status;
        this.msg = msg;
    }

    @Nullable
    public final String getMsg() {
        return this.msg;
    }

    @NonNull
    public static NetworkState error(String msg) {
        return new NetworkState(FAILED, msg);
    }

}