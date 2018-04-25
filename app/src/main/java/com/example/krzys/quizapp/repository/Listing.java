package com.example.krzys.quizapp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;

public class Listing<T> {
    // the LiveData of paged lists for the UI to observe
    public final LiveData<PagedList<T>> mPagedList;
    // represents the network request status to show to the user
    public final LiveData<NetworkState> mNetworkState;
    // represents the refresh status to show to the user. Separate from networkState, this
    // value is importantly only when refresh is requested.
    public final LiveData<NetworkState> mRefreshState;

    public Listing(LiveData<PagedList<T>> mPagedList, LiveData<NetworkState> mNetworkState,
                   LiveData<NetworkState> mRefreshState) {
        this.mPagedList = mPagedList;
        this.mNetworkState = mNetworkState;
        this.mRefreshState = mRefreshState;
    }
}
