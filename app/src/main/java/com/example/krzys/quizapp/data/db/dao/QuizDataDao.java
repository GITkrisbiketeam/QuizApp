package com.example.krzys.quizapp.data.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import com.example.krzys.quizapp.data.db.converter.AnswerConverter;
import com.example.krzys.quizapp.data.db.converter.BooleanConverter;
import com.example.krzys.quizapp.data.db.converter.FlagResultConverter;
import com.example.krzys.quizapp.data.db.converter.QuestionConverter;
import com.example.krzys.quizapp.data.db.converter.RateConverter;
import com.example.krzys.quizapp.data.model.quiz.QuizData;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
@TypeConverters({AnswerConverter.class, QuestionConverter.class, RateConverter.class,
        BooleanConverter.class, FlagResultConverter.class})
public interface QuizDataDao {

    @Query("select * from QuizData where id = :id")
    LiveData<QuizData> getQuizDataById(long id);

    @Query("select * from QuizData where id = :id")
    QuizData getQuizDataByIdImmediate(long id);

    @Insert(onConflict = REPLACE)
    void insertQuizData(QuizData quizData);

    @Delete
    void deleteQuizData(QuizData quizData);

}
