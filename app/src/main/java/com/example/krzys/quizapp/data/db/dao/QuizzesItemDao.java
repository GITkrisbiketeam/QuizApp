package com.example.krzys.quizapp.data.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.example.krzys.quizapp.data.db.converter.BooleanConverter;
import com.example.krzys.quizapp.data.db.converter.CategoriesConverter;
import com.example.krzys.quizapp.data.db.converter.TagsConverter;
import com.example.krzys.quizapp.data.model.quizzes.QuizzesItem;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
@TypeConverters({CategoriesConverter.class, TagsConverter.class, BooleanConverter.class})
public interface QuizzesItemDao {

    @Query("select * from QuizzesItem ORDER BY createdAt DESC")
    LiveData<List<QuizzesItem>> getAllQuizzesItems();

    @Query("select * from QuizzesItem where id = :id")
    QuizzesItem getQuizItemById(long id);

    @Insert(onConflict = REPLACE)
    void addQuizzesItem(QuizzesItem... quizzesItems);

    @Update
    void updateQuizzesItem(QuizzesItem quizzesItem);

    @Delete
    void deleteQuizzesItem(QuizzesItem quizzesItem);

}
