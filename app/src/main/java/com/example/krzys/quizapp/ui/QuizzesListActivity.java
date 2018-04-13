package com.example.krzys.quizapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.krzys.quizapp.R;
import com.example.krzys.quizapp.data.viewmodel.QuizAppViewModel;
import com.example.krzys.quizapp.data.model.quizzes.QuizzesItem;
import com.example.krzys.quizapp.utils.Utils;

public class QuizzesListActivity extends AppCompatActivity implements QuizzesListAdapter
        .QuizItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = Utils.getLogTag(QuizzesListActivity.class.getName());

    private View mRootView;

    private QuizzesListAdapter mQuizzesAdapter;
    private QuizAppViewModel mQuizAppViewModel;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG, "onCreate savedInstanceState: " + savedInstanceState);
        setContentView(R.layout.activity_quizzes_list);

        Toolbar toolbar = findViewById(R.id.activity_quiz_toolbar);
        setSupportActionBar(toolbar);

        mRootView = findViewById(R.id.parentLayout);

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R
                .color.holo_green_light, android.R.color.holo_orange_light, android.R.color
                .holo_red_light);

        //Getting List and Setting List Adapter
        RecyclerView quizzesRecyclerView = findViewById(R.id.quizzes_recycler_view);
        quizzesRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        quizzesRecyclerView.setLayoutManager(layoutManager);

        //Add dividers
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL);
        quizzesRecyclerView.addItemDecoration(itemDecor);

        mQuizzesAdapter = new QuizzesListAdapter(QuizzesListActivity.this, this);
        quizzesRecyclerView.setAdapter(mQuizzesAdapter);

        mQuizAppViewModel = ViewModelProviders.of(this).get(QuizAppViewModel.class);

        mQuizAppViewModel.getAllQuizzesList().observe(QuizzesListActivity.this, quizzesItems -> {
            Log.w(TAG, "QuizAppViewModel observer onChanged quizzesItems:" + quizzesItems);
            if (quizzesItems != null) {
                mQuizzesAdapter.addQuizzesItems(quizzesItems);
                mSwipeRefreshLayout.setRefreshing(false);
                Log.w(TAG, "QuizAppViewModel observer onChanged quizzesItems.size():" + quizzesItems.size());

                if (quizzesItems.size() > 0) {
                    Log.i(TAG, "QuizAppViewModel observer onChanged item:" + quizzesItems.get(0).getCreatedAt() + "" + " " + quizzesItems.get(0).getTitle());
                }
                // TODO: for logging only
                /*for (QuizzesItem item : mQuizzesList) {
                    Log.i(TAG, "QuizAppViewModel observer onChanged item:" + item.getCreatedAt() +
                            "" + " " + item.getTitle());
                }*/
            }
        });

        //mQuizAppViewModel.getNewQuizzes(getApplication());

        if (!Utils.checkConnection(getApplicationContext())) {
            Snackbar.make(mRootView, R.string.string_internet_connection_not_available, Snackbar
                    .LENGTH_LONG).show();
        } else {
            // Show refreshing animation
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_quizzes, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quizzes_action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.quizzes_menu_refresh:
                // User chose the "Refresh" action
                Log.w(TAG, "onOptionsItemSelected refresh called");
                mQuizAppViewModel.getNewQuizzes(getApplication());
                // Show refreshing animation
                mSwipeRefreshLayout.setRefreshing(true);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onQuizItemClicked(View view, QuizzesItem item) {
        Log.w(TAG, "onQuizItemClicked clicked item: " + item);
        Intent intent = new Intent(this, QuizActivity.class);
        ImageView imageView = view.findViewById(R.id.quizzes_list_item_image_view);
        Log.w(TAG, "onQuizItemClicked clicked imageView: " + imageView);

        // Pass quiz id in the bundle and populate details activity.
        intent.putExtra(QuizActivity.EXTRA_QUIZ, item);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, imageView, getString(R.string
                        .transition_name_shared_image));
        startActivity(intent, options.toBundle());
    }

    /**
     * Called due to {@link SwipeRefreshLayout} been called to refresh
     */
    @Override
    public void onRefresh() {
        Log.w(TAG, "onRefresh SwipeRefreshLayout called to refresh");
        mQuizAppViewModel.getNewQuizzes(getApplication());
    }
}
