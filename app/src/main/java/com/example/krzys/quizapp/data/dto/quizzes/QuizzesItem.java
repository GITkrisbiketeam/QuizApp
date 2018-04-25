package com.example.krzys.quizapp.data.dto.quizzes;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.krzys.quizapp.data.db.converter.IntegerConverter;
import com.example.krzys.quizapp.data.db.converter.TagsConverter;
import com.example.krzys.quizapp.data.dto.common.Category;
import com.example.krzys.quizapp.data.dto.common.MainPhoto;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class QuizzesItem implements Parcelable {

    @Ignore
    @SerializedName("buttonStart")
    @Expose
    private String buttonStart;

    @Ignore
    @SerializedName("shareTitle")
    @Expose
    private String shareTitle;

    @SerializedName("questions")
    @Expose
    private Integer questions;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    @Ignore
    @SerializedName("sponsored")
    @Expose
    private Boolean sponsored;

    @Ignore
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

    @Embedded(prefix = "category_")
    @SerializedName("category")
    @Expose
    private Category category;

    @TypeConverters(TagsConverter.class)
    @SerializedName("tags")
    @Expose
    private List<Tag> tags = null;

    /**
     * Custom field in Room Database to store user answers
     */
    @TypeConverters(IntegerConverter.class)
    private List<Integer> myAnswers = null;

    // to be consistent w/ changing backend order, we need to keep a data like this
    private int indexInResponse;

    public final static Parcelable.Creator<QuizzesItem> CREATOR = new Creator<QuizzesItem>() {


        @SuppressWarnings({"unchecked"})
        public QuizzesItem createFromParcel(Parcel in) {
            return new QuizzesItem(in);
        }

        public QuizzesItem[] newArray(int size) {
            return (new QuizzesItem[size]);
        }

    };

    protected QuizzesItem(Parcel in) {
        this.buttonStart = ((String) in.readValue((String.class.getClassLoader())));
        this.shareTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.questions = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
        this.sponsored = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.categories = new ArrayList<>();
        in.readList(this.categories, (Category.class.getClassLoader()));
        this.id = ((Long) in.readValue((Long.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.content = ((String) in.readValue((String.class.getClassLoader())));
        this.mainPhoto = ((MainPhoto) in.readValue((MainPhoto.class.getClassLoader())));
        this.category = ((Category) in.readValue((Category.class.getClassLoader())));
        this.tags = new ArrayList<>();
        in.readList(this.tags, (Tag.class.getClassLoader()));
        this.myAnswers = new ArrayList<>();
        in.readList(this.myAnswers, (Integer.class.getClassLoader()));
        this.indexInResponse  = in.readInt();
    }

    public QuizzesItem() {
    }

    public QuizzesItem(Long id, Integer questions, String createdAt, String title, String type,
                       String content, List<Category> categories, MainPhoto mainPhoto, Category category,
                       List<Tag> tags, List<Integer> myAnswers) {
        this.id = id;
        this.questions = questions;
        this.createdAt = createdAt;
        this.title = title;
        this.type = type;
        this.content = content;
        this.categories = categories;
        this.mainPhoto = mainPhoto;
        this.category = category;
        this.tags = tags;
        this.myAnswers = myAnswers;
    }

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Integer> getMyAnswers() {
        return myAnswers;
    }

    public void setMyAnswers(List<Integer> myAnswers) {
        this.myAnswers = myAnswers;
    }

    public int getIndexInResponse() {
        return indexInResponse;
    }

    public void setIndexInResponse(int indexInResponse) {
        this.indexInResponse = indexInResponse;
    }



    @Override
    public String toString() {
        return "buttonStart: " + buttonStart + "; shareTitle: " + shareTitle + "; questions: " +
                questions + "; createdAt: " + createdAt + "; sponsored: " + sponsored + "; " +
                "categories: " + categories + "; id: " + id + "; title: " + title + "; type: " +
                type + "; content: " + content + "; mainPhoto: " + mainPhoto + "; category: " +
                category + "; tags: " + tags + "; myAnswers: "  + myAnswers +
                "; indexInResponse: " + indexInResponse;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(buttonStart);
        dest.writeValue(shareTitle);
        dest.writeValue(questions);
        dest.writeValue(createdAt);
        dest.writeValue(sponsored);
        dest.writeList(categories);
        dest.writeValue(id);
        dest.writeValue(title);
        dest.writeValue(type);
        dest.writeValue(content);
        dest.writeValue(mainPhoto);
        dest.writeValue(category);
        dest.writeList(tags);
        dest.writeList(myAnswers);
        dest.writeInt(indexInResponse);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof QuizzesItem))
            return false;
        QuizzesItem that = (QuizzesItem) o;
        return sameExeptMyAnswers(o) &&
                (myAnswers == null ? that.myAnswers == null : myAnswers.equals(that.myAnswers));
    }

    public boolean sameExeptMyAnswers(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof QuizzesItem))
            return false;
        QuizzesItem that = (QuizzesItem) o;
        return
                //indexInResponse == that.indexInResponse &&
                        (buttonStart == null ? that.buttonStart == null : buttonStart.equals(that.buttonStart)) &&
                        (shareTitle == null ? that.shareTitle == null : shareTitle.equals(that.shareTitle)) &&
                        (questions == null ? that.questions == null : questions.equals(that.questions)) &&
                        (createdAt == null ? that.createdAt == null : createdAt.equals(that.createdAt)) &&
                        (sponsored == null ? that.sponsored == null : sponsored.equals(that.sponsored)) &&
                        (id == null ? that.id == null : id.equals(that.id)) &&
                        (title == null ? that.title == null : title.equals(that.title)) &&
                        (type == null ? that.type == null : type.equals(that.type)) &&
                        (content == null ? that.content == null : content.equals(that.content)) &&

                        (mainPhoto == null ? that.mainPhoto == null : mainPhoto.equals(that.mainPhoto)) &&
                        (category == null ? that.category == null : category.equals(that.category)) &&

                        (tags == null ? that.tags == null : tags.equals(that.tags)) &&
                        (categories == null ? that.categories == null : categories.equals(that.categories));
    }

    public int describeContents() {
        return 0;
    }

}
