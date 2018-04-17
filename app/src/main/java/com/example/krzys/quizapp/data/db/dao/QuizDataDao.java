package com.example.krzys.quizapp.data.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import com.example.krzys.quizapp.data.db.converter.AnswerConverter;
import com.example.krzys.quizapp.data.db.converter.IntegerConverter;
import com.example.krzys.quizapp.data.db.converter.FlagResultConverter;
import com.example.krzys.quizapp.data.db.converter.QuestionConverter;
import com.example.krzys.quizapp.data.db.converter.RateConverter;
import com.example.krzys.quizapp.data.model.quiz.QuizData;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
@TypeConverters({AnswerConverter.class, QuestionConverter.class, RateConverter.class,
        IntegerConverter.class, FlagResultConverter.class})
public interface QuizDataDao {

    /**
     * Get {@link LiveData} holder for {@link QuizData} observer based on provided id of Quiz Item
     *
     * @param id Long with id of Quiz Item to observe changes
     * @return {@link QuizData) {@link LiveData} data holder to be observed
     */
    @Query("select * from QuizData where id = :id")
    LiveData<QuizData> getQuizDataById(long id);

    /**
     * Inserts given {@link QuizData} into DB performing {@link OnConflictStrategy#REPLACE}
     * operation on conflict
     *
     * @param quizData {@link QuizData} to be inserted into DB
     */
    @Insert(onConflict = REPLACE)
    void insertQuizData(QuizData quizData);

}
