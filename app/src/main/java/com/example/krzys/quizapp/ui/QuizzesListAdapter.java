package com.example.krzys.quizapp.ui;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.krzys.quizapp.R;
import com.example.krzys.quizapp.data.model.quizzes.QuizzesItem;
import com.example.krzys.quizapp.utils.Constants;
import com.example.krzys.quizapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class QuizzesListAdapter extends RecyclerView.Adapter<QuizzesListViewHolder> implements
        QuizzesListViewHolder.QuizItemHolderClickListener {
    private static final String TAG = Utils.getLogTag(QuizzesListAdapter.class.getSimpleName());

    final Context mContext;
    private final LayoutInflater mInflater;

    final List<QuizzesItem> mQuizzesItemsList;
    final QuizItemClickListener mQuizItemClickListener;

    public interface QuizItemClickListener {
        void onQuizItemClicked(ImageView imageView, ProgressBar progressBar, QuizzesItem item);
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

        List<Integer> myAnswers = item.getMyAnswers();
        if (myAnswers != null) {
            int questionsCount = item.getQuestions();
            int answersCount = myAnswers.size();
            int myCorrectAnswers = 0;
            switch (item.getType()) {
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
                        scoreText = mContext.getString(R.string.list_item_solve_score,
                                myCorrectAnswers, questionsCount, score);
                    } else if (answersCount > 0) {
                        // if answer count is more than 0 thi means User started this quiz
                        int score = Math.round(answersCount / (float) questionsCount * 100);
                        scoreText = mContext.getString(R.string.list_item_quiz_progress, score);
                    }
                    holder.textViewScore.setVisibility(View.VISIBLE);
                    holder.textViewScore.setText(scoreText);

                    break;
                case Constants.TYPE_TEST:
                    //TODO:
                case Constants.TYPE_POLL:
                    //TODO:
                    break;
                default:
                    holder.textViewScore.setVisibility(View.GONE);
            }

            holder.progressBar.setMax(questionsCount);
            holder.progressBar.setProgress(answersCount);
            // set ProgressBar color on pre Lolipop
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                holder.progressBar.getProgressDrawable().setColorFilter(mContext.getResources()
                        .getColor(R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        } else {
            holder.textViewScore.setVisibility(View.GONE);
            holder.progressBar.setProgress(0);
        }

        if (item.getMainPhoto() != null) {
            holder.imageView.setVisibility(View.VISIBLE);
            GlideApp.with(mContext).load(item.getMainPhoto().getUrl()).placeholder(R.mipmap
                    .ic_launcher).error(R.mipmap.ic_launcher).fallback(R.mipmap.ic_launcher)
                    .centerCrop().into(holder.imageView);
        } else {
            Log.w(TAG, "onBindViewHolder no image Url");
            GlideApp.with(mContext).clear(holder.imageView);
            holder.imageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mQuizzesItemsList.size();
    }

    public void addQuizzesItems(@NonNull List<QuizzesItem> quizzesItemsList) {
        if (mQuizzesItemsList.size() > 0) {
            mQuizzesItemsList.clear();
            mQuizzesItemsList.addAll(quizzesItemsList);
            notifyItemRangeChanged(0, quizzesItemsList.size());
        } else {
            mQuizzesItemsList.addAll(quizzesItemsList);
            notifyDataSetChanged();
        }
    }

    @Override
    public void onQuizItemClicked(ImageView imageView, ProgressBar progressBar, int position) {
        mQuizItemClickListener.onQuizItemClicked(imageView, progressBar, mQuizzesItemsList.get
                (position));
    }
}