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
import com.example.krzys.quizapp.repository.NetworkState;
import com.example.krzys.quizapp.viewmodel.QuizzesListViewModel;
import com.example.krzys.quizapp.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.krzys.quizapp.repository.NetworkState.*;

public class QuizzesListActivity extends AppCompatActivity {

    private static final String TAG = Utils.getLogTag(QuizzesListActivity.class);

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

        mQuizzesListViewModel = ViewModelProviders.of(this).get(QuizzesListViewModel.class);

        initSwypeRefreshLayout();

        initRecyclerAdapter();

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
                mQuizzesListViewModel.refreshQuizzes();
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
        mSwipeRefreshLayout.setOnRefreshListener(() -> mQuizzesListViewModel.refreshQuizzes());

        mQuizzesListViewModel.getRefreshStateLiveData().observe(this, networkState -> {
            Log.w(TAG, "getRefreshStateLiveData observer onChanged networkState:" + networkState);
            mSwipeRefreshLayout.setRefreshing(networkState == NetworkState.LOADING);
            mQuizzesAdapter.setNetworkState(networkState);
        });
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

        mQuizzesAdapter = new QuizzesListAdapter(
                new QuizzesListAdapter.QuizItemClickListener() {
                    @Override
                    public void onQuizItemClicked(ImageView imageView, ProgressBar progressBar,
                                                  QuizzesItem item) {
                        launchQuizActivity(imageView, progressBar, item);
                    }

                    @Override
                    public void onRefreshButtonClicked() {
                        mQuizzesListViewModel.retryLoadQuizzes();
                    }
                });

        quizzesRecyclerView.setAdapter(mQuizzesAdapter);

        mQuizzesListViewModel.getQuizzesItemsListLiveData().observe(this, quizzesItems -> {

            Log.w(TAG, "getQuizzesItemsListLiveData observer onChanged quizzesItems.size():" +
                    (quizzesItems != null ? quizzesItems.size(): "null"));
            mQuizzesAdapter.submitList(quizzesItems);
            quizzesRecyclerView.post(() -> {
                Log.w(TAG, "getQuizzesItemsListLiveData scroll " + mQuizzesAdapter.getItemCount());
                if (mQuizzesAdapter.getItemCount() <= 1) {
                    quizzesRecyclerView.scrollToPosition(0);
                }
            });
        });

        mQuizzesListViewModel.getNetworkStateLiveData().observe(this, networkState -> {
            Log.w(TAG, "getNetworkStateLiveData observer onChanged networkState:" + networkState);
            mQuizzesAdapter.setNetworkState(networkState);
            if (networkState != LOADING && networkState != LOADED) {
                Utils.showSnackbar(mRootView, R.string.string_internet_connection_not_available);
            }
        });
    }

    private void launchQuizActivity(ImageView imageView, ProgressBar progressBar, QuizzesItem item) {
        Log.w(TAG, "launchQuizActivity clicked item: " + item);
        Intent intent = new Intent(this, QuizActivity.class);

        // Pass QuizzesItem in the bundle and populate details activity.
        intent.putExtra(QuizActivity.EXTRA_QUIZ, item);
        Pair<View, String> image = Pair.create(imageView, getString(R.string
                .transition_name_shared_image));
        Pair<View, String> progress = Pair.create(progressBar, getString(R.string
                .transition_name_shared_progress));

        //Start QuizActivity with
        @SuppressWarnings("unchecked")
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, image, progress);
        startActivity(intent, options.toBundle());
    }
}
