package com.example.krzys.quizapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.krzys.quizapp.R;
import com.example.krzys.quizapp.data.database.QuizListViewModel;
import com.example.krzys.quizapp.data.model.quizzes.QuizItem;
import com.example.krzys.quizapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = Utils.getLogTag(MainActivity.class.getName());

    private List<QuizItem> mQuizzesList;

    private View mRootView;

    private QuizListAdapter mQuizzesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Array List for Binding Data from JSON to this List
        mQuizzesList = new ArrayList<>();

        mRootView = findViewById(R.id.parentLayout);

        //Getting List and Setting List Adapter
        RecyclerView mQuizzesRecyclerView = findViewById(R.id.quizzes_recycler_view);
        mQuizzesRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mQuizzesRecyclerView.setLayoutManager(mLayoutManager);

        mQuizzesAdapter = new QuizListAdapter(MainActivity.this, mQuizzesList);
        mQuizzesRecyclerView.setAdapter(mQuizzesAdapter);

        QuizListViewModel mQuizListViewModel = ViewModelProviders.of(this).get(QuizListViewModel
                .class);

        mQuizListViewModel.getQuizItemsList().observe(MainActivity.this, quizItems -> {
            Log.w(TAG, "QuizListViewModel observer onChanged quizItems:" + quizItems);
            mQuizzesAdapter.addItems(quizItems);
            mQuizzesList = quizItems;
            // TODO: for logging only
            for (QuizItem item : mQuizzesList) {
                Log.i(TAG, "QuizListViewModel observer onChanged item:" + item.getCreatedAt() + "" +
                        " " + item.getTitle());
            }
        });

        if (!Utils.checkConnection(getApplicationContext())) {
            Snackbar.make(mRootView, R.string.string_internet_connection_not_available, Snackbar
                    .LENGTH_LONG).show();
        }
    }
}
