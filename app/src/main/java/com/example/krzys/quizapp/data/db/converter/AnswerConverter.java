package com.example.krzys.quizapp.data.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.example.krzys.quizapp.data.model.quiz.Answer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class AnswerConverter implements Serializable {

    @TypeConverter
    public String fromAnswersList(List<Answer> answers) {
        if (answers == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Answer>>() {
        }.getType();
        return gson.toJson(answers, type);
    }

    @TypeConverter
    public List<Answer> toAnswersList(String answersString) {
        if (answersString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Answer>>() {
        }.getType();
        return gson.fromJson(answersString, type);
    }

}