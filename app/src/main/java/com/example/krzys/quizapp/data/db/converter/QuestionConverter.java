package com.example.krzys.quizapp.data.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.example.krzys.quizapp.data.dto.quiz.Question;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class QuestionConverter implements Serializable {

    @TypeConverter
    public String fromQuestionsList(List<Question> questions) {
        if (questions == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Question>>() {
        }.getType();
        return gson.toJson(questions, type);
    }

    @TypeConverter
    public List<Question> toQuestionsList(String questionsString) {
        if (questionsString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Question>>() {
        }.getType();
        return gson.fromJson(questionsString, type);
    }

}