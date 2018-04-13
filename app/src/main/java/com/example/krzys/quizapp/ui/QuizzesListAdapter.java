package com.example.krzys.quizapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.krzys.quizapp.R;
import com.example.krzys.quizapp.data.model.quizzes.QuizzesItem;
import com.example.krzys.quizapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class QuizzesListAdapter extends RecyclerView.Adapter<QuizzesListViewHolder> implements
        QuizzesListViewHolder.QuizItemHolderClickListener {
    private static final String TAG = Utils.getLogTag(QuizzesListAdapter.class.getName());

    final Context mContext;
    private final LayoutInflater mInflater;

    final List<QuizzesItem> mQuizzesItemsList;
    final QuizItemClickListener mQuizItemClickListener;

    public interface QuizItemClickListener {
        void onQuizItemClicked(View view, QuizzesItem item);
    }

    public QuizzesListAdapter(@NonNull Context context, @NonNull QuizItemClickListener
            quizItemClickListener) {
        mContext = context;
        mInflater = LayoutInflater.from(context);

        mQuizzesItemsList = new ArrayList<>();
        mQuizItemClickListener = quizItemClickListener;
    }

    @NonNull
    @Override
    public QuizzesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout view = (ConstraintLayout) mInflater.inflate(R.layout
                .quizzes_list_row_item, parent, false);

        return new QuizzesListViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizzesListViewHolder holder, int position) {

        QuizzesItem item = mQuizzesItemsList.get(position);

        holder.textViewTitle.setText(item.getTitle());
        holder.textViewType.setText(item.getType());
        Log.e(TAG, "onBindViewHolder " + item.getMainPhoto().getUrl());
        if (item.getMainPhoto() != null) {
            String url = item.getMainPhoto().getUrl();
            GlideApp.with(mContext).load(url).placeholder(R.mipmap.ic_launcher).error(R.mipmap
                    .ic_launcher).fallback(R.mipmap.ic_launcher).into(holder.imageView);
        } else {
            Log.e(TAG, "onBindViewHolder no image Url");
            GlideApp.with(mContext).clear(holder.imageView);
        }
    }

    @Override
    public void onViewRecycled(@NonNull QuizzesListViewHolder holder) {
        GlideApp.with(mContext).clear(holder.imageView);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return mQuizzesItemsList.size();
    }

    public void addQuizzesItems(@NonNull List<QuizzesItem> quizzesItemsList) {
        mQuizzesItemsList.clear();
        mQuizzesItemsList.addAll(quizzesItemsList);
        notifyDataSetChanged();
    }

    @Override
    public void onQuizItemClicked(View view, int position) {
        mQuizItemClickListener.onQuizItemClicked(view, mQuizzesItemsList.get(position));
    }
}