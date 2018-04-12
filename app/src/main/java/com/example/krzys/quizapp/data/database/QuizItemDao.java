package com.example.krzys.quizapp.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import com.example.krzys.quizapp.data.model.quizzes.QuizItem;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
@TypeConverters({CategoriesConverter.class, TagsConverter.class})
public interface QuizItemDao {

    @Query("select * from QuizItem ORDER BY createdAt DESC")
    LiveData<List<QuizItem>> getAllBorrowedItems();

    @Query("select * from QuizItem where id = :id")
    QuizItem getItemById(String id);

    @Insert(onConflict = REPLACE)
    void addQuiz(QuizItem quizItem);

    @Delete
    void deleteQuiz(QuizItem quizItem);

}
