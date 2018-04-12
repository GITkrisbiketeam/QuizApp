package com.example.krzys.quizapp.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.krzys.quizapp.data.db.dao.QuizDataDao;
import com.example.krzys.quizapp.data.db.dao.QuizzesItemDao;
import com.example.krzys.quizapp.data.model.quiz.QuizData;
import com.example.krzys.quizapp.data.model.quizzes.QuizzesItem;

@Database(entities = {QuizzesItem.class, QuizData.class}, version = 1)
public abstract class QuizAppRoomDatabase extends RoomDatabase {

    public abstract QuizzesItemDao quizzesItemDao();
    public abstract QuizDataDao quizDataDao();
    private static QuizAppRoomDatabase INSTANCE;

    public static QuizAppRoomDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), QuizAppRoomDatabase.class,
                    "quiz_db")
                    // Wipes and rebuilds instead of migrating
                    // if no Migration object.
                    // Migration is not part of this practical.
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

}