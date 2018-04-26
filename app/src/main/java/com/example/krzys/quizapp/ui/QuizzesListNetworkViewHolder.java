package com.example.krzys.quizapp.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.krzys.quizapp.R;
import com.example.krzys.quizapp.repository.NetworkState;
import com.example.krzys.quizapp.utils.Utils;

public class QuizzesListNetworkViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = Utils.getLogTag(QuizzesListNetworkViewHolder.class);

    private final TextView textViewTitle;
    private final ProgressBar progressBar;
    private final Button retryButton;

    @FunctionalInterface
    public interface QuizzesItemNetworkHolderRetryListener {
        void onRetryButtonClicked();
    }

    private QuizzesListNetworkViewHolder(@NonNull View rootView, @NonNull
            QuizzesItemNetworkHolderRetryListener clickListener) {
        super(rootView);

        this.textViewTitle = rootView.findViewById(R.id.quizzes_list_item_network_error_msg);
        this.progressBar = rootView.findViewById(R.id.quizzes_list_item_network_progress_bar);
        this.retryButton = rootView.findViewById(R.id.quizzes_list_item_network_retry_button);

        this.retryButton.setOnClickListener((view) -> clickListener.onRetryButtonClicked());
    }

    @NonNull
    public static QuizzesListNetworkViewHolder create(@NonNull ViewGroup parent,
                                                      @NonNull QuizzesItemNetworkHolderRetryListener clickListener) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .quizzes_list_row_item_network, parent, false);
        return new QuizzesListNetworkViewHolder(rootView, clickListener);
    }

    public void bindTo(@NonNull NetworkState networkState) {
        Log.w(TAG, "bindTo networkState " + networkState);

        progressBar.setVisibility(networkState == NetworkState.LOADING ? View.VISIBLE : View.GONE);
        retryButton.setVisibility(networkState != NetworkState.LOADING && networkState !=
                NetworkState.LOADED ? View.VISIBLE : View.GONE);
        if (networkState.getMsg() != null) {
            textViewTitle.setVisibility(View.VISIBLE);
            textViewTitle.setText(networkState.getMsg());
        } else {

            textViewTitle.setVisibility(View.GONE);
        }
    }
}