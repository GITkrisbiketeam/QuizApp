package com.example.krzys.quizapp.data.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.example.krzys.quizapp.data.db.converter.IntegerConverter;
import com.example.krzys.quizapp.data.db.converter.TagsConverter;
import com.example.krzys.quizapp.data.dto.quizzes.QuizzesItem;

import java.util.List;


import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
@TypeConverters({TagsConverter.class, IntegerConverter.class})
public interface QuizzesItemDao {

    /**
     * Get {@link LiveData} holder for List of{@link QuizzesItem} to be observed
     *
     * @return {@link QuizzesItem) {@link List} of {@link LiveData} data holder to be observed
     */
    @Query("select * from QuizzesItem ORDER BY indexInResponse ASC") //ORDER BY indexInResponse ASC
    DataSource.Factory<Integer, QuizzesItem> getAllQuizzesItems();

    /**
     * Get {@link LiveData} holder for List of{@link QuizzesItem} to be observed
     *
     * @return {@link QuizzesItem) {@link List} of {@link LiveData} data holder to be observed
     */
    @Query("select * from QuizzesItem ORDER BY createdAt DESC")
    LiveData<List<QuizzesItem>> getAllQuizzesItemsList();

    @Query("SELECT MAX(indexInResponse) + 1 FROM QuizzesItem")
    int getNextIndexInQuizzesItems();

    /**
     * Get {@link LiveData} holder for {@link QuizzesItem} observer based on provided id of Quiz Item
     *
     * @param id Long with id of Quiz Item to observe changes
     * @return {@link QuizzesItem) {@link LiveData} data holder to be observed
     */
    @Query("select * from QuizzesItem where id = :id")
    LiveData<QuizzesItem> getQuizzesItemById(long id);

    /**
     * Get {@link QuizzesItem} from DB immediately. This should not be run fro UI thread or error
     * will occur
     *
     * @param id Long with id of QuizzesItem to get from DB
     * @return {@link QuizzesItem) object
     */
    @Query("select * from QuizzesItem where id = :id")
    QuizzesItem getQuizzesItemByIdImmediate(long id);

    /**
     * Get {@link LiveData} holder for List of Quiz Item type to be observed
     *
     * @return Quiz Item type String {@link List} of {@link LiveData} data holder to be observed
     */
    @Query("select type from QuizzesItem")
    LiveData<List<String>> getTypes();

    /**
     * Inserts given {@link QuizzesItem} into DB performing {@link OnConflictStrategy#REPLACE}
     * operation on conflict
     *
     * @param quizzesItems {@link QuizzesItem} to be inserted into DB
     */
    @Insert(onConflict = REPLACE)
    void insertQuizzesItem(QuizzesItem... quizzesItems);

    /**
     * Updates given {@link QuizzesItem} into DB performing default
     * {@link OnConflictStrategy#ABORT} operation on conflict
     *
     * @param quizzesItem {@link QuizzesItem} to be inserted into DB
     */
    @Update
    void updateQuizzesItem(QuizzesItem quizzesItem);

}
