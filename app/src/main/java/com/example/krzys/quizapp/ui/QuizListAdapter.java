package com.example.krzys.quizapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.krzys.quizapp.R;
import com.example.krzys.quizapp.data.model.quizzes.QuizItem;
import com.example.krzys.quizapp.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.ViewHolder> {
    private static final String TAG = Utils.getLogTag(QuizListAdapter.class.getName());

    List<QuizItem> mQuizItemsList;
    final Context mContext;

    public QuizListAdapter(@NonNull Context context, @NonNull List<QuizItem> objects) {
        mContext = context;
        mQuizItemsList = objects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout view = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quiz_list_row_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        QuizItem item = mQuizItemsList.get(position);

        holder.textViewTitle.setText(item.getTitle());
        holder.textViewType.setText(item.getType());
        Log.e(TAG, "onBindViewHolder " + item.getMainPhoto().getUrl());
        /*Picasso.get()
                .load(item.getMainPhoto().getUrl())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.imageView);*/
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
    public void onViewRecycled(@NonNull ViewHolder holder) {
        Log.e(TAG, "onViewRecycled");
        GlideApp.with(mContext).clear(holder.imageView);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return mQuizItemsList.size();
    }

    public void addItems(List<QuizItem> quizItemsList) {
        mQuizItemsList = quizItemsList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ConstraintLayout rootView;
        public final ImageView imageView;
        public final TextView textViewTitle;
        public final TextView textViewType;

        private ViewHolder(ConstraintLayout rootView) {
            super(rootView);
            this.rootView = rootView;
            this.imageView = rootView.findViewById(R.id.imageView);
            this.textViewTitle = rootView.findViewById(R.id.textViewTitle);
            this.textViewType = rootView.findViewById(R.id.textViewType);
        }
    }
}