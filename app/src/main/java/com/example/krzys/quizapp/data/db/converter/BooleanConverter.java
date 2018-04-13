package com.example.krzys.quizapp.data.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class BooleanConverter implements Serializable {

    @TypeConverter
    public String fromBooleansList(List<Boolean> tags) {
        if (tags == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Boolean>>() {
        }.getType();
        return gson.toJson(tags, type);
    }

    @TypeConverter
    public List<Boolean> toBooleansList(String tagsString) {
        if (tagsString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Boolean>>() {
        }.getType();
        return gson.fromJson(tagsString, type);
    }

}