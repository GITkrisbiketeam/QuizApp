package com.example.krzys.quizapp.data.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class IntegerConverter implements Serializable {

    @TypeConverter
    public String fromBooleansList(List<Integer> integers) {
        if (integers == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {
        }.getType();
        return gson.toJson(integers, type);
    }

    @TypeConverter
    public List<Integer> toBooleansList(String integersString) {
        if (integersString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {
        }.getType();
        return gson.fromJson(integersString, type);
    }

}