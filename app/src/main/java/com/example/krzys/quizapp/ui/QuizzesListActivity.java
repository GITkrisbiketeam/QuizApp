package com.example.krzys.quizapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.krzys.quizapp.R;
import com.example.krzys.quizapp.data.model.quizzes.QuizzesItem;
import com.example.krzys.quizapp.data.viewmodel.QuizAppViewModel;
import com.example.krzys.quizapp.utils.Utils;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class QuizzesListActivity extends AppCompatActivity implements QuizzesListAdapter
        .QuizItemClickListener, SwipyRefreshLayout.OnRefreshListener {

    private static final String TAG = Utils.getLogTag(QuizzesListActivity.class.getSimpleName());

    private View mRootView;

    private QuizzesListAdapter mQuizzesAdapter;
    private QuizAppViewModel mQuizAppViewModel;

    private SwipyRefreshLayout mSwipeRefreshLayout;

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

        // Getting List and Setting List Adapter
        RecyclerView quizzesRecyclerView = findViewById(R.id.quizzes_recycler_view);
        quizzesRecyclerView.setHasFixedSize(true);
        // Disable list items refresh animation
        ((SimpleItemAnimator) quizzesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

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

        mQuizAppViewModel.getAllQuizzesList(this).observe(this, quizzesItems -> {
            Log.w(TAG, "QuizAppViewModel observer onChanged");
            if (quizzesItems != null) {
                mQuizzesAdapter.addQuizzesItems(quizzesItems);
                mSwipeRefreshLayout.setRefreshing(false);
                Log.w(TAG, "QuizAppViewModel observer onChanged quizzesItems.size():" +
                        quizzesItems.size());

                if (quizzesItems.size() > 0) {
                    Log.i(TAG, "QuizAppViewModel observer onChanged item:" + quizzesItems.get(0)
                            .getCreatedAt() + "" + " " + quizzesItems.get(0).getTitle());
                }
            }
        });

        mQuizAppViewModel.getAllQuizzesListTypes().observe(this, quizzesTypes ->{
            if (quizzesTypes!=null) {
                List<String> types;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    types = quizzesTypes.parallelStream().unordered().distinct().collect(Collectors
                                .toList());
                } else {
                    types = new ArrayList<>(new HashSet<>(quizzesTypes));
                }
                Log.e(TAG, "typeList: " + Arrays.toString(types.toArray()));
            }
        });


        if (Utils.checkConnection(getApplicationContext())) {
            // Show refreshing animation
            mSwipeRefreshLayout.setRefreshing(true);
        } else {
            Utils.showSnackbar(mRootView, R.string.string_internet_connection_not_available);
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
                if (Utils.checkConnection(this)) {
                    mQuizAppViewModel.updateNewQuizzes(0);
                    // Show refreshing animation
                    //mSwipeRefreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
                    mSwipeRefreshLayout.setRefreshing(true);
                    //mSwipeRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);

                } else {
                    Utils.showSnackbar(mRootView, R.string
                            .string_internet_connection_not_available);
                }
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onQuizItemClicked(ImageView imageView, ProgressBar progressBar, QuizzesItem item) {
        Log.w(TAG, "onQuizItemClicked clicked item: " + item);
        Intent intent = new Intent(this, QuizActivity.class);

        // Pass quiz id in the bundle and populate details activity.
        intent.putExtra(QuizActivity.EXTRA_QUIZ, item);
        Pair<View, String> image = Pair.create(imageView, getString(R.string
                .transition_name_shared_image));
        Pair<View, String> progress = Pair.create(progressBar, getString(R.string
                .transition_name_shared_progress));

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, image, progress);
        startActivity(intent, options.toBundle());
    }

    /**
     * Called due to {@link SwipyRefreshLayout} been called to refresh
     */
    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        Log.w(TAG, "onRefresh SwipeRefreshLayout called to refresh direction: " + direction);
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            if (Utils.checkConnection(this)) {
                mQuizAppViewModel.updateNewQuizzes(0);
            } else {
                Utils.showSnackbar(mRootView, R.string.string_internet_connection_not_available);
                // Stop refreshing animation
                mSwipeRefreshLayout.setRefreshing(false);
            }
        } else {
            if (Utils.checkConnection(this)) {
                mQuizAppViewModel.updateNewQuizzes(mQuizzesAdapter.getItemCount());
            } else {
                Utils.showSnackbar(mRootView, R.string.string_internet_connection_not_available);
                // Stop refreshing animation
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}
