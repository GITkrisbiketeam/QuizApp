package com.example.krzys.quizapp.ui;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.krzys.quizapp.R;
import com.example.krzys.quizapp.data.dto.quizzes.QuizzesItem;
import com.example.krzys.quizapp.utils.Constants;
import com.example.krzys.quizapp.utils.Utils;

import java.util.List;

public class QuizzesListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String TAG = Utils.getLogTag(QuizzesListViewHolder.class);

    private final QuizzesItemHolderClickListener mListener;

    private final View rootView;
    private final ImageView imageView;
    private final TextView textViewTitle;
    private final TextView textViewScore;
    private final ProgressBar progressBar;

    @FunctionalInterface
    public interface QuizzesItemHolderClickListener {
        void onQuizzesItemClicked(ImageView imageView, ProgressBar progressBar, int position);
    }

    private QuizzesListViewHolder(View rootView, QuizzesItemHolderClickListener clickListener) {
        super(rootView);
        mListener = clickListener;

        this.rootView = rootView;
        this.imageView = rootView.findViewById(R.id.quizzes_list_item_image_view);
        this.textViewTitle = rootView.findViewById(R.id.quizzes_list_item_text_view_title);
        this.textViewScore = rootView.findViewById(R.id.quizzes_list_item_text_view_score);
        this.progressBar = rootView.findViewById(R.id.quizzes_list_item_progress);
        // set ProgressBar color on pre Lollipop
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            progressBar.getProgressDrawable().setColorFilter(rootView.getContext().getResources()
                    .getColor(R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        this.rootView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        mListener.onQuizzesItemClicked(imageView, progressBar, getAdapterPosition());
    }

    public static QuizzesListViewHolder create(@NonNull ViewGroup parent,
                                               QuizzesItemHolderClickListener clickListener) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .quizzes_list_row_item_quiz, parent, false);
        return new QuizzesListViewHolder(rootView, clickListener);
    }

    public void bindTo(@NonNull QuizzesItem quizzesItem){
        Log.w(TAG, "bindTo quizzesItem " + quizzesItem.getIndexInResponse());

        textViewTitle.setText(quizzesItem.getTitle());

        List<Integer> myAnswers = quizzesItem.getMyAnswers();
        if (myAnswers != null) {
            int questionsCount = quizzesItem.getQuestions();
            int answersCount = myAnswers.size();
            int myCorrectAnswers = 0;
            switch (quizzesItem.getType()) {
                case Constants.TYPE_QUIZ:
                    // count correct answers, correct answer will be Integer '1' value
                    for (Integer b : myAnswers) {
                        if (b != null && b == 1) {
                            myCorrectAnswers++;
                        }
                    }
                    String scoreText = null;
                    if (answersCount == questionsCount) {
                        // if answer count is the same as questions count this means quiz was solved

                        int score = Math.round(myCorrectAnswers / (float) questionsCount * 100);
                        scoreText = rootView.getContext().getString(R.string.list_item_solve_score,
                                myCorrectAnswers, questionsCount, score);
                    } else if (answersCount > 0) {
                        // if answer count is more than 0 thi means User started this quiz
                        int score = Math.round(answersCount / (float) questionsCount * 100);
                        scoreText = rootView.getContext().getString(R.string.list_item_quiz_progress, score);
                    }
                    textViewScore.setVisibility(View.VISIBLE);
                    textViewScore.setText(scoreText);

                    break;
                case Constants.TYPE_TEST:
                    //TODO:
                case Constants.TYPE_POLL:
                    //TODO:
                    break;
                default:
                    textViewScore.setVisibility(View.GONE);
            }

            updateProgress(quizzesItem);
        } else {
            textViewScore.setVisibility(View.GONE);
            progressBar.setProgress(0);
        }

        if (quizzesItem.getMainPhoto() != null) {
            imageView.setVisibility(View.VISIBLE);
            GlideApp.with(rootView.getContext()).load(quizzesItem.getMainPhoto().getUrl()).placeholder(R.mipmap
                    .ic_launcher).error(R.mipmap.ic_launcher).fallback(R.mipmap.ic_launcher)
                    .centerCrop().into(imageView);
        } else {
            Log.w(TAG, "onBindViewHolder no image Url");
            GlideApp.with(rootView.getContext()).clear(imageView);
            imageView.setVisibility(View.GONE);
        }
    }

    public void updateProgress(@NonNull QuizzesItem quizzesItem) {
        Log.w(TAG, "updateProgress quizzesItem");
        progressBar.setMax(quizzesItem.getQuestions());
        progressBar.setProgress(quizzesItem.getMyAnswers() == null ? 0 : quizzesItem
                .getMyAnswers().size());
    }

    public void clear() {
        textViewTitle.setText(R.string.quizzes_item_title_loading);
        GlideApp.with(rootView.getContext()).clear(imageView);
        textViewScore.setVisibility(View.GONE);
        progressBar.setProgress(0);
    }
}