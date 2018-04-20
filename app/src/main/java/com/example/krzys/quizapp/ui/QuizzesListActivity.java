package com.example.krzys.quizapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
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
import com.example.krzys.quizapp.data.dto.quizzes.QuizzesItem;
import com.example.krzys.quizapp.viewmodel.QuizzesListViewModel;
import com.example.krzys.quizapp.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class QuizzesListActivity extends AppCompatActivity {

    private static final String TAG = Utils.getLogTag(QuizzesListActivity.class.getSimpleName());

    private View mRootView;

    private QuizzesListAdapter mQuizzesAdapter;

    private QuizzesListViewModel mQuizzesListViewModel;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG, "onCreate savedInstanceState: " + savedInstanceState);
        setContentView(R.layout.activity_quizzes_list);

        Toolbar toolbar = findViewById(R.id.activity_quiz_toolbar);
        setSupportActionBar(toolbar);

        mRootView = findViewById(R.id.parentLayout);

        initSwypeRefreshLayout();

        initRecyclerAdapter();

        mQuizzesListViewModel = ViewModelProviders.of(this).get(QuizzesListViewModel.class);

        mQuizzesListViewModel.getQuizzesItemsListLiveData().observe(this, quizzesItems -> {
            Log.w(TAG, "QuizzesListViewModel observer onChanged quizzesItems.size():" +
                    quizzesItems.size());
            mQuizzesAdapter.submitList(quizzesItems);

            mSwipeRefreshLayout.setRefreshing(false);
        });

        // Just for Logging
        mQuizzesListViewModel.getAllQuizzesListTypes().observe(this, quizzesTypes -> {
            if (quizzesTypes != null) {
                List<String> types;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    types = quizzesTypes.parallelStream().unordered().distinct().collect
                            (Collectors.toList());
                } else {
                    types = new ArrayList<>(new HashSet<>(quizzesTypes));
                }
                Log.e(TAG, "typeList: " + Arrays.toString(types.toArray()));
            }
        });

        mQuizzesListViewModel.getConnectionLiveData().observe(this, networkState -> {
            Log.w(TAG, "QuizzesListViewModel network state changed:" + networkState);
        });

        //TODO: do sth with this
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
            case R.id.quizzes_menu_refresh:
                // User chose the "Refresh" action
                Log.w(TAG, "onOptionsItemSelected refresh called");
                if (!mSwipeRefreshLayout.isRefreshing()) {
                    triggerSwypeRefresh(true);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initSwypeRefreshLayout() {
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_container);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R
                .color.holo_green_light, android.R.color.holo_orange_light, android.R.color
                .holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(() -> triggerSwypeRefresh(false));

    }

    private void initRecyclerAdapter() {
        // Getting List and Setting List Adapter
        RecyclerView quizzesRecyclerView = findViewById(R.id.quizzes_recycler_view);
        quizzesRecyclerView.setHasFixedSize(true);

        // Disable list items refresh animation
        ((SimpleItemAnimator) quizzesRecyclerView.getItemAnimator()).setSupportsChangeAnimations
                (false);

        //Add dividers
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL);
        quizzesRecyclerView.addItemDecoration(itemDecor);

        mQuizzesAdapter = new QuizzesListAdapter(this, (ImageView imageView, ProgressBar
                progressBar, QuizzesItem item) -> {
            Log.w(TAG, "onQuizItemClicked clicked item: " + item);
            Intent intent = new Intent(this, QuizActivity.class);

            // Pass QuizzesItem in the bundle and populate details activity.
            intent.putExtra(QuizActivity.EXTRA_QUIZ, item);
            Pair<View, String> image = Pair.create(imageView, getString(R.string
                    .transition_name_shared_image));
            Pair<View, String> progress = Pair.create(progressBar, getString(R.string
                    .transition_name_shared_progress));

            //Start QuizActivity with
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(this, image, progress);
            startActivity(intent, options.toBundle());
        });

        quizzesRecyclerView.setAdapter(mQuizzesAdapter);
    }

    /**
     * Trigger refresh of QuizzesItems to download new items
     * //TODO: we should probably use some sort of Refresh state LiveData in ViewModel
     *
     *
     * @param animate should we show animate refresh circle
     */
    private void triggerSwypeRefresh(boolean animate) {
        Log.w(TAG, "triggerSwypeRefresh");
        if (Utils.checkConnection(this)) {
            mQuizzesListViewModel.refreshQuizzes();
            if (animate) {
                mSwipeRefreshLayout.setRefreshing(true);
            }

        } else {
            Utils.showSnackbar(mRootView, R.string.string_internet_connection_not_available);
            // Stop refreshing animation
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

}
