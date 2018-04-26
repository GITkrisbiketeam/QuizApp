package com.example.krzys.quizapp.ui;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.krzys.quizapp.data.dto.quizzes.QuizzesItem;
import com.example.krzys.quizapp.repository.NetworkState;
import com.example.krzys.quizapp.utils.Utils;

import java.util.List;

class QuizzesListAdapter extends PagedListAdapter<QuizzesItem, RecyclerView.ViewHolder> {

    private static final String TAG = Utils.getLogTag(QuizzesListAdapter.class);

    private static final int VIEW_TYPE_QUIZ = 1;
    private static final int VIEW_TYPE_NETWORK = 2;

    private final QuizItemClickListener mQuizItemClickListener;

    private NetworkState mNetworkState;

    public interface QuizItemClickListener {
        void onQuizItemClicked(ImageView imageView, ProgressBar progressBar, QuizzesItem item);

        void onRefreshButtonClicked();
    }

    @NonNull
    private static final DiffUtil.ItemCallback<QuizzesItem> QUIZZES_ITEM_COMPARATOR = new
            DiffUtil.ItemCallback<QuizzesItem>() {
        public boolean areItemsTheSame(@NonNull QuizzesItem oldItem, @NonNull QuizzesItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        public boolean areContentsTheSame(@NonNull QuizzesItem oldItem, @NonNull QuizzesItem
                newItem) {
            return oldItem.equals(newItem);
        }

        @Nullable
        public Object getChangePayload(@NonNull QuizzesItem oldItem, @NonNull QuizzesItem newItem) {
            return oldItem.sameExceptMyAnswers(newItem) ? newItem.getMyAnswers() : null;

        }
    };

    public QuizzesListAdapter(@NonNull QuizItemClickListener quizItemClickListener) {
        super(QUIZZES_ITEM_COMPARATOR);

        mQuizItemClickListener = quizItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return VIEW_TYPE_NETWORK;
        } else {
            return VIEW_TYPE_QUIZ;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (VIEW_TYPE_QUIZ == viewType) {
            return QuizzesListViewHolder.create(parent, (ImageView imageView, ProgressBar
                    progressBar, int position) -> mQuizItemClickListener.onQuizItemClicked
                    (imageView, progressBar, getItem(position)));
        } else {
            return QuizzesListNetworkViewHolder.create(parent,
                    mQuizItemClickListener::onRefreshButtonClicked);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull
            List<Object> payloads) {

        if (!payloads.isEmpty() && getItemViewType(position) == VIEW_TYPE_QUIZ) {
            Log.e(TAG, "onBindViewHolder payloads: " + payloads);
            QuizzesItem quizzesItem = getItem(position);
            if (quizzesItem != null) {
                ((QuizzesListViewHolder) holder).updateProgress(quizzesItem);

            }
        } else {
            this.onBindViewHolder(holder, position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case VIEW_TYPE_QUIZ:
                QuizzesItem quizzesItem = getItem(position);
                if (quizzesItem != null) {
                    ((QuizzesListViewHolder) holder).bindTo(quizzesItem);
                } else {
                    // Null defines a placeholder item - PagedListAdapter will automatically
                    // invalidate
                    // this row when the actual object is loaded from the database
                    ((QuizzesListViewHolder) holder).clear();

                }
                break;
            case VIEW_TYPE_NETWORK:
                ((QuizzesListNetworkViewHolder) holder).bindTo(mNetworkState);
                break;
            default:
                Log.e(TAG, "onBindViewHolder wrong View type");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (hasExtraRow() ? 1 : 0);
    }

    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = mNetworkState;
        boolean hadExtraRow = hasExtraRow();
        mNetworkState = newNetworkState;
        boolean hasExtraRow = hasExtraRow();
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount());
            } else {
                notifyItemInserted(super.getItemCount());
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    private boolean hasExtraRow() {
        return mNetworkState != null && mNetworkState != NetworkState.LOADED;
    }
}