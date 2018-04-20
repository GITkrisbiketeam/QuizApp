package com.example.krzys.quizapp.data.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.example.krzys.quizapp.data.dto.quiz.FlagResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class FlagResultConverter implements Serializable {

    @TypeConverter
    public String fromQuestionsList(List<FlagResult> flagResult) {
        if (flagResult == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<FlagResult>>() {
        }.getType();
        return gson.toJson(flagResult, type);
    }

    @TypeConverter
    public List<FlagResult> toQuestionsList(String flagResultString) {
        if (flagResultString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<FlagResult>>() {
        }.getType();
        return gson.fromJson(flagResultString, type);
    }

}