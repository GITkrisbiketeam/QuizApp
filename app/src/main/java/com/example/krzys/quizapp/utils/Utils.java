package com.example.krzys.quizapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

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
        return cm != null && cm.getActiveNetworkInfo() != null;
    }

    /**
     * Shows {@link Toast} message
     *
     * @param context Context from which to show {@link Toast}
     * @param text    String with message to show in {@link Toast}
     */
    public static void showToast(@NonNull Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
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
}
