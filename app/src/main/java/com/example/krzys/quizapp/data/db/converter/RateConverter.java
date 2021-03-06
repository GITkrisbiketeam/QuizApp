package com.example.krzys.quizapp.data.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.example.krzys.quizapp.data.model.quiz.Rate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class RateConverter implements Serializable {

    @TypeConverter
    public String fromRatesList(List<Rate> rates) {
        if (rates == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Rate>>() {
        }.getType();
        return gson.toJson(rates, type);
    }

    @TypeConverter
    public List<Rate> toRatesList(String ratesString) {
        if (ratesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Rate>>() {
        }.getType();
        return gson.fromJson(ratesString, type);
    }

}