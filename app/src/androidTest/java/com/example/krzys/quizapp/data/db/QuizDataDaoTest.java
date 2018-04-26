package com.example.krzys.quizapp.data.db;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.krzys.quizapp.LiveDataTestUtil;
import com.example.krzys.quizapp.data.db.dao.QuizDataDao;
import com.example.krzys.quizapp.data.db.dao.QuizzesItemDao;
import com.example.krzys.quizapp.data.dto.quizzes.QuizzesItem;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static com.example.krzys.quizapp.data.db.TestData.QUIZZES_ITEM;
import static com.example.krzys.quizapp.data.db.TestData.QUIZZES_ITEM_LIST;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Test the implementation of {@link com.example.krzys.quizapp.data.db.dao.QuizDataDao}
 */
@RunWith(AndroidJUnit4.class)
public class QuizDataDaoTest {


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private QuizAppRoomDatabase mDatabase;

    private QuizDataDao mQuizDataDao;

    private QuizzesItemDao mQuizzesItemDao;

    @Before
    public void initDb() throws Exception {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                QuizAppRoomDatabase.class)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();

        mQuizDataDao = mDatabase.quizDataDao();
        mQuizzesItemDao = mDatabase.quizzesItemDao();
    }

    @After
    public void closeDb() throws Exception {
        mDatabase.close();
    }

    @Test
    public void getQuizzesItemsWhenNoItemsInserted() throws InterruptedException {
        List<QuizzesItem> items = LiveDataTestUtil.getValue(mQuizzesItemDao.getAllQuizzesItemsList(
        ));

        assertTrue(items.isEmpty());
    }

    @Test
    public void getQuizzesItemsByProductId() throws InterruptedException {
        mQuizzesItemDao.insertQuizzesItem(QUIZZES_ITEM_LIST.toArray(new QuizzesItem[0]));

        QuizzesItem item = LiveDataTestUtil.getValue(mQuizzesItemDao.getQuizzesItemById(QUIZZES_ITEM.getId()));

        assertNotNull(item);
    }

    /*@Test
    public void getCommentsWhenNoCommentInserted() throws InterruptedException {
        List<CommentEntity> comments = LiveDataTestUtil.getValue(mQuizDataDao.loadComments
                (COMMENT_ENTITY.getProductId()));

        assertTrue(comments.isEmpty());
    }

    @Test
    public void cantInsertCommentWithoutProduct() throws InterruptedException {
        try {
            mQuizDataDao.insertAll(COMMENTS);
            fail("SQLiteConstraintException expected");
        } catch (SQLiteConstraintException ignored) {

        }
    }

    @Test
    public void getCommentsAfterInserted() throws InterruptedException {
        mQuizzesItemDao.insertAll(PRODUCTS);
        mQuizDataDao.insertAll(COMMENTS);

        List<CommentEntity> comments = LiveDataTestUtil.getValue(mQuizDataDao.loadComments
                (COMMENT_ENTITY.getProductId()));

        assertThat(comments.size(), is(1));
    }

    @Test
    public void getCommentByProductId() throws InterruptedException {
        mQuizzesItemDao.insertAll(PRODUCTS);
        mQuizDataDao.insertAll(COMMENTS);

        List<CommentEntity> comments = LiveDataTestUtil.getValue(mQuizDataDao.loadComments(
                (COMMENT_ENTITY.getProductId())));

        assertThat(comments.size(), is(1));
    }*/

}
