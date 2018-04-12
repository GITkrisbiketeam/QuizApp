package com.example.krzys.quizapp.data.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.example.krzys.quizapp.data.model.quiz.LatestResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class LatestResultConverter implements Serializable {

    @TypeConverter
    public String fromTagsList(List<LatestResult> tags) {
        if (tags == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<LatestResult>>() {
        }.getType();
        return gson.toJson(tags, type);
    }

    @TypeConverter
    public List<LatestResult> toTagsList(String tagsString) {
        if (tagsString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<LatestResult>>() {
        }.getType();
        return gson.fromJson(tagsString, type);
    }

}