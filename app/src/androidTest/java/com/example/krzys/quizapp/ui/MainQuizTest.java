package com.example.krzys.quizapp.ui;

import android.arch.core.executor.testing.CountingTaskExecutorRule;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.example.krzys.quizapp.EspressoTestUtil;
import com.example.krzys.quizapp.R;
import com.example.krzys.quizapp.data.db.QuizAppRoomDatabase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;

public class MainQuizTest {
    @Rule
    public ActivityTestRule<QuizzesListActivity> mActivityRule = new ActivityTestRule<>(
            QuizzesListActivity.class);

    @Rule
    public CountingTaskExecutorRule mCountingTaskExecutorRule = new CountingTaskExecutorRule();

    public MainQuizTest() {
        // delete the database
        InstrumentationRegistry.getTargetContext().deleteDatabase(QuizAppRoomDatabase.DATA_BASE_NAME);
    }

    @Before
    public void disableRecyclerViewAnimations() {
        // Disable RecyclerView animations
        EspressoTestUtil.disableAnimations(mActivityRule);
    }

    /*@Before
    public void waitForDbCreation() throws Throwable {
        final CountDownLatch latch = new CountDownLatch(1);
        final LiveData<Boolean> databaseCreated = QuizAppRoomDatabase.getDatabase(
                InstrumentationRegistry.getTargetContext())
                .getDatabaseCreated();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                databaseCreated.observeForever(new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {
                        if (Boolean.TRUE.equals(aBoolean)) {
                            databaseCreated.removeObserver(this);
                            latch.countDown();
                        }
                    }
                });
            }
        });
        MatcherAssert.assertThat("database should've initialized",
                latch.await(1, TimeUnit.MINUTES), CoreMatchers.is(true));
    }*/

    @Test
    public void clickOnFirstItem_opensComments() throws Throwable {
        drain();
        // When clicking on the first product
        onView(ViewMatchers.withContentDescription(R.string.cd_quizzes_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        /*drain();
        // Then the second screen with the comments should appear.
        onView(withContentDescription(R.string.cd_comments_list))
                .check(matches(isDisplayed()));
        drain();
        // Then the second screen with the comments should appear.
        onView(withContentDescription(R.string.cd_product_name))
                .check(matches(not(withText(""))));*/

    }

    private void drain() throws TimeoutException, InterruptedException {
        mCountingTaskExecutorRule.drainTasks(1, TimeUnit.MINUTES);
    }
}
