package com.example.krzys.quizapp.ui;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.krzys.quizapp.data.dto.quizzes.QuizzesItem;
import com.example.krzys.quizapp.utils.Utils;

import java.util.List;

public class QuizzesListAdapter extends PagedListAdapter<QuizzesItem, QuizzesListViewHolder> implements
        QuizzesListViewHolder.QuizItemHolderClickListener {
    private static final String TAG = Utils.getLogTag(QuizzesListAdapter.class.getSimpleName());

    final Context mContext;

    final QuizItemClickListener mQuizItemClickListener;

    @FunctionalInterface
    public interface QuizItemClickListener {
        void onQuizItemClicked(ImageView imageView, ProgressBar progressBar, QuizzesItem item);
    }

    @NonNull
    private static final DiffUtil.ItemCallback<QuizzesItem> QUIZZES_ITEM_COMPARATOR = new DiffUtil.ItemCallback<QuizzesItem>() {
        public boolean areItemsTheSame(@NonNull QuizzesItem oldItem, @NonNull QuizzesItem newItem) {

            boolean result = oldItem == null ? newItem == null : oldItem.getId().equals(newItem.getId());
            //Log.e(TAG, "areItemsTheSame result: " + result + " oldItem " + oldItem.getId() + " newItem:" + newItem.getId());

            return result;
        }

        public boolean areContentsTheSame(@NonNull QuizzesItem oldItem, @NonNull QuizzesItem newItem) {

            boolean result = oldItem == null ? newItem == null : oldItem.equals(newItem);
            //Log.e(TAG, "areContentsTheSame result: " + result + " oldItem " + oldItem.getId() + " newItem:" + newItem.getId());
            return result;
        }

        @Nullable
        public Object getChangePayload(@NonNull QuizzesItem oldItem, @NonNull QuizzesItem newItem) {
            //Log.e(TAG, "getChangePayload oldItem " + oldItem.getId() + " newItem:" + newItem.getId());
            return oldItem != null && newItem != null && oldItem.sameExeptMyAnswers(newItem)? newItem.getMyAnswers(): null;

        }
    };

    public QuizzesListAdapter(@NonNull Context context, @NonNull QuizItemClickListener
            quizItemClickListener) {
        super(QUIZZES_ITEM_COMPARATOR);
        mContext = context;

        mQuizItemClickListener = quizItemClickListener;
    }

    @NonNull
    @Override
    public QuizzesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return QuizzesListViewHolder.create(parent, this);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizzesListViewHolder holder, int position, @NonNull
            List<Object> payloads) {

        if(!payloads.isEmpty()){
            Log.e(TAG, "onBindViewHolder payloads: " + payloads);
        }
        if (payloads != null && !payloads.isEmpty()) {
            QuizzesItem quizzesItem = getItem(position);
            holder.updateProgress(quizzesItem);
        } else {
            this.onBindViewHolder(holder, position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull QuizzesListViewHolder holder, int position) {

        QuizzesItem quizzesItem = getItem(position);
        if (quizzesItem != null) {
            holder.bindTo(quizzesItem);
        } else {
            // Null defines a placeholder item - PagedListAdapter will automatically invalidate
            // this row when the actual object is loaded from the database
            holder.clear();

        }
    }

    @Override
    public void onQuizItemClicked(ImageView imageView, ProgressBar progressBar, int position) {
        mQuizItemClickListener.onQuizItemClicked(imageView, progressBar, getItem(position));
    }
}