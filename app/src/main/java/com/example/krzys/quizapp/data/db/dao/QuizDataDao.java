package com.example.krzys.quizapp.data.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import com.example.krzys.quizapp.data.db.converter.AnswerConverter;
import com.example.krzys.quizapp.data.db.converter.LatestResultConverter;
import com.example.krzys.quizapp.data.db.converter.QuestionConverter;
import com.example.krzys.quizapp.data.db.converter.RateConverter;
import com.example.krzys.quizapp.data.model.quiz.QuizData;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
@TypeConverters({AnswerConverter.class, LatestResultConverter.class, QuestionConverter.class,
        RateConverter.class})
public interface QuizDataDao {

    @Query("select * from QuizData where id = :id")
    LiveData<QuizData> getQuizDataById(long id);

    @Insert(onConflict = REPLACE)
    void addQuiz(QuizData... quizData);

    @Delete
    void deleteQuiz(QuizData quizData);

}
