package com.example.krzys.quizapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.example.krzys.quizapp.R;

public class Utils {
    private static final String TAG_PREFIX = "QuizApp_";

    /**
     * Generate uniform Log TAG
     */
    public static String getLogTag(String className) {
        return TAG_PREFIX + className;
    }

    /**
     * Check if there is Internet Connection active
     */
    public static boolean checkConnection(@NonNull Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo()
                .isConnected();
    }

    /**
     * Shows Snackbar on provided viewRoot with given message
     *
     * @param viewRoot      View to which attach Snackbar
     * @param messageResId  int with resource Id with message to show
     */
    public static void showSnackbar(@NonNull View viewRoot, int messageResId) {
        Snackbar.make(viewRoot, messageResId, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Get readable Streing value of Quiz Item type parameter
     *
     * @param context   App Context
     * @param type      String with type valye returned from API
     *
     * @return  readable form of quiz type
     */
    public static String getTypeString(Context context, String type){
        int resId;
        switch (type) {
            case Constants.TYPE_QUIZ:
                resId = R.string.list_item_type_quiz;
                break;
            case Constants.TYPE_TEST:
                resId = R.string.list_item_type_test;
                break;
            case Constants.TYPE_POLL:
                resId = R.string.list_item_type_poll;
                break;
            default:
                resId = R.string.list_item_type_unknown;
        }
        return context.getString(resId);
    }
}
