package com.example.krzys.quizapp.data.livedata;

import android.arch.lifecycle.LiveData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import com.example.krzys.quizapp.utils.Utils;

public class ConnectionLiveData extends LiveData<Boolean> {

    private static final String TAG = Utils.getLogTag(ConnectionLiveData.class.getSimpleName());

    private Context context;

    public ConnectionLiveData(Context context) {
        this.context = context;
    }

    @Override
    protected void onActive() {
        super.onActive();
        Log.d(TAG, "onActive");
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(networkReceiver, filter);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        Log.d(TAG, "onInactive");
        context.unregisterReceiver(networkReceiver);
    }

    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive");
            boolean noConnection = intent.getBooleanExtra(ConnectivityManager
                    .EXTRA_NO_CONNECTIVITY, false);
            if (noConnection) {
                Log.d(TAG, "onReceive EXTRA_NO_CONNECTIVITY");
                postValue(Boolean.FALSE);
            } else {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                        .CONNECTIVITY_SERVICE);
                if (cm != null && cm.getActiveNetworkInfo().isConnected()) {
                    Log.d(TAG, "onReceive is connected");
                    postValue(Boolean.TRUE);
                } else {
                    Log.d(TAG, "onReceive is NOT connected");
                    postValue(Boolean.FALSE);
                }
            }
        }
    };
}
