package com.example.krzys.quizapp.data.database;

import android.arch.persistence.room.TypeConverter;

import com.example.krzys.quizapp.data.model.quizzes.Category;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class CategoriesConverter implements Serializable {

    @TypeConverter
    public String fromCategoriesList(List<Category> tags) {
        if (tags == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Category>>() {
        }.getType();
        return gson.toJson(tags, type);
    }

    @TypeConverter
    public List<Category> toCategoriesList(String categoriesString) {
        if (categoriesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Category>>() {
        }.getType();
        return gson.fromJson(categoriesString, type);
    }

}