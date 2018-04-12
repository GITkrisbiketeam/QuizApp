
package com.example.krzys.quizapp.data.model.quizzes;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.List;

import com.example.krzys.quizapp.data.database.CategoriesConverter;
import com.example.krzys.quizapp.data.database.TagsConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class QuizItem {

    @SerializedName("buttonStart")
    @Expose
    private String buttonStart;

    @SerializedName("shareTitle")
    @Expose
    private String shareTitle;

    @SerializedName("questions")
    @Expose
    private Integer questions;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    @SerializedName("sponsored")
    @Expose
    private Boolean sponsored;

    @TypeConverters(CategoriesConverter.class)
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("content")
    @Expose
    private String content;

    @Embedded(prefix = "mainPhoto_")
    @SerializedName("mainPhoto")
    @Expose
    private MainPhoto mainPhoto;

    @Embedded(prefix = "category__")
    @SerializedName("category")
    @Expose
    private Category_ category;

    @TypeConverters(TagsConverter.class)
    @SerializedName("tags")
    @Expose
    private List<Tag> tags = null;

    public String getButtonStart() {
        return buttonStart;
    }

    public void setButtonStart(String buttonStart) {
        this.buttonStart = buttonStart;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public Integer getQuestions() {
        return questions;
    }

    public void setQuestions(Integer questions) {
        this.questions = questions;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getSponsored() {
        return sponsored;
    }

    public void setSponsored(Boolean sponsored) {
        this.sponsored = sponsored;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MainPhoto getMainPhoto() {
        return mainPhoto;
    }

    public void setMainPhoto(MainPhoto mainPhoto) {
        this.mainPhoto = mainPhoto;
    }

    public Category_ getCategory() {
        return category;
    }

    public void setCategory(Category_ category) {
        this.category = category;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

}
