package com.example.krzys.quizapp.ui;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.krzys.quizapp.R;

public class QuizzesListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final QuizItemHolderClickListener mListener;

    public final ConstraintLayout rootView;
    public final ImageView imageView;
    public final TextView textViewTitle;
    public final TextView textViewScore;
    public final ProgressBar progressBar;

    public interface QuizItemHolderClickListener {
        void onQuizItemClicked(View view, int position);
    }

    QuizzesListViewHolder(ConstraintLayout rootView, QuizItemHolderClickListener clickListener) {
        super(rootView);
        mListener = clickListener;

        this.rootView = rootView;
        this.imageView = rootView.findViewById(R.id.quizzes_list_item_image_view);
        this.textViewTitle = rootView.findViewById(R.id.quizzes_list_item_text_view_title);
        this.textViewScore = rootView.findViewById(R.id.quizzes_list_item_text_view_score);
        this.progressBar = rootView.findViewById(R.id.quizzes_list_item_progress);

        this.rootView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        mListener.onQuizItemClicked(view, getAdapterPosition());
    }
}