package com.example.krzys.quizapp.data.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.example.krzys.quizapp.data.model.quiz.Question;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class QuestionConverter implements Serializable {

    @TypeConverter
    public String fromTagsList(List<Question> tags) {
        if (tags == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Question>>() {
        }.getType();
        return gson.toJson(tags, type);
    }

    @TypeConverter
    public List<Question> toTagsList(String tagsString) {
        if (tagsString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Question>>() {
        }.getType();
        return gson.fromJson(tagsString, type);
    }

}