package com.example.krzys.quizapp.data.model.quiz;

import java.util.ArrayList;
import java.util.List;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.krzys.quizapp.data.db.converter.BooleanConverter;
import com.example.krzys.quizapp.data.db.converter.CategoriesConverter;
import com.example.krzys.quizapp.data.db.converter.LatestResultConverter;
import com.example.krzys.quizapp.data.db.converter.QuestionConverter;
import com.example.krzys.quizapp.data.db.converter.RateConverter;
import com.example.krzys.quizapp.data.model.common.Category;
import com.example.krzys.quizapp.data.model.common.Category_;
import com.example.krzys.quizapp.data.model.common.MainPhoto;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class QuizData implements Parcelable {

    @Embedded(prefix = "celebrity_")
    @SerializedName("celebrity")
    @Expose
    private Celebrity celebrity;
    @TypeConverters(RateConverter.class)
    @SerializedName("rates")
    @Expose
    private List<Rate> rates = null;
    @TypeConverters(QuestionConverter.class)
    @SerializedName("questions")
    @Expose
    private List<Question> questions = null;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("sponsored")
    @Expose
    private Boolean sponsored;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("buttonStart")
    @Expose
    private String buttonStart;
    @SerializedName("shareTitle")
    @Expose
    private String shareTitle;
    @TypeConverters(CategoriesConverter.class)
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("scripts")
    @Expose
    private String scripts;
    @Embedded(prefix = "mainPhoto_")
    @SerializedName("mainPhoto")
    @Expose
    private MainPhoto mainPhoto;
    @Embedded(prefix = "category__")
    @SerializedName("category")
    @Expose
    private Category_ category;
    @SerializedName("isBattle")
    @Expose
    private Boolean isBattle;
    @SerializedName("created")
    @Expose
    private Long created;
    @TypeConverters(LatestResultConverter.class)
    @SerializedName("latestResults")
    @Expose
    private List<LatestResult> latestResults = null;
    @SerializedName("avgResult")
    @Expose
    private Double avgResult;
    @SerializedName("resultCount")
    @Expose
    private Long resultCount;
    @SerializedName("cityAvg")
    @Expose
    private String cityAvg;
    @SerializedName("cityTime")
    @Expose
    private String cityTime;
    @SerializedName("cityCount")
    @Expose
    private String cityCount;
    @SerializedName("userBattleDone")
    @Expose
    private Boolean userBattleDone;
    @Embedded(prefix = "sponsoredResults_")
    @SerializedName("sponsoredResults")
    @Expose
    private SponsoredResults sponsoredResults;


    @TypeConverters(BooleanConverter.class)
    private List<Boolean> myAnswers = null;

    public final static Creator<QuizData> CREATOR = new Creator<QuizData>() {


        @SuppressWarnings({"unchecked"})
        public QuizData createFromParcel(Parcel in) {
            return new QuizData(in);
        }

        public QuizData[] newArray(int size) {
            return (new QuizData[size]);
        }

    };

    protected QuizData(Parcel in) {
        this.celebrity = ((Celebrity) in.readValue((Celebrity.class.getClassLoader())));
        this.rates = new ArrayList<>();
        in.readList(this.rates, (Rate.class.getClassLoader()));
        this.questions = new ArrayList<>();
        in.readList(this.questions, (Question.class.getClassLoader()));
        this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
        this.sponsored = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.content = ((String) in.readValue((String.class.getClassLoader())));
        this.buttonStart = ((String) in.readValue((String.class.getClassLoader())));
        this.shareTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.categories = new ArrayList<>();
        in.readList(this.categories, (Category.class.getClassLoader()));
        this.id = ((Long) in.readValue((Long.class.getClassLoader())));
        this.scripts = ((String) in.readValue((String.class.getClassLoader())));
        this.mainPhoto = ((MainPhoto) in.readValue((MainPhoto.class.getClassLoader())));
        this.category = ((Category_) in.readValue((Category_.class.getClassLoader())));
        this.isBattle = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.created = ((Long) in.readValue((Long.class.getClassLoader())));
        this.latestResults = new ArrayList<>();
        in.readList(this.latestResults, (LatestResult.class.getClassLoader()));
        this.avgResult = ((Double) in.readValue((Double.class.getClassLoader())));
        this.resultCount = ((Long) in.readValue((Long.class.getClassLoader())));
        this.cityAvg = ((String) in.readValue((String.class.getClassLoader())));
        this.cityTime = ((String) in.readValue((String.class.getClassLoader())));
        this.cityCount = ((String) in.readValue((String.class.getClassLoader())));
        this.userBattleDone = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.sponsoredResults = ((SponsoredResults) in.readValue((SponsoredResults.class
                .getClassLoader())));
        this.myAnswers = new ArrayList<>();
        in.readList(this.myAnswers, (Boolean.class.getClassLoader()));
    }

    public QuizData() {
    }

    public Celebrity getCelebrity() {
        return celebrity;
    }

    public void setCelebrity(Celebrity celebrity) {
        this.celebrity = celebrity;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
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

    public String getScripts() {
        return scripts;
    }

    public void setScripts(String scripts) {
        this.scripts = scripts;
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

    public Boolean getIsBattle() {
        return isBattle;
    }

    public void setIsBattle(Boolean isBattle) {
        this.isBattle = isBattle;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public List<LatestResult> getLatestResults() {
        return latestResults;
    }

    public void setLatestResults(List<LatestResult> latestResults) {
        this.latestResults = latestResults;
    }

    public Double getAvgResult() {
        return avgResult;
    }

    public void setAvgResult(Double avgResult) {
        this.avgResult = avgResult;
    }

    public Long getResultCount() {
        return resultCount;
    }

    public void setResultCount(Long resultCount) {
        this.resultCount = resultCount;
    }

    public String getCityAvg() {
        return cityAvg;
    }

    public void setCityAvg(String cityAvg) {
        this.cityAvg = cityAvg;
    }

    public String getCityTime() {
        return cityTime;
    }

    public void setCityTime(String cityTime) {
        this.cityTime = cityTime;
    }

    public String getCityCount() {
        return cityCount;
    }

    public void setCityCount(String cityCount) {
        this.cityCount = cityCount;
    }

    public Boolean getUserBattleDone() {
        return userBattleDone;
    }

    public void setUserBattleDone(Boolean userBattleDone) {
        this.userBattleDone = userBattleDone;
    }

    public SponsoredResults getSponsoredResults() {
        return sponsoredResults;
    }

    public void setSponsoredResults(SponsoredResults sponsoredResults) {
        this.sponsoredResults = sponsoredResults;
    }

    public List<Boolean> getMyAnswers() {
        return myAnswers;
    }

    public void setMyAnswers(List<Boolean> myAnswers) {
        this.myAnswers = myAnswers;
    }

    @Override
    public String toString() {
        return "celebrity: " + celebrity + "; rates: " + rates + "; questions: " + questions +
                "; createdAt: " + createdAt + "; sponsored: " + sponsored + "; title: " + title +
                "; type: " + type + "; content: " + content + "; buttonStart: " + buttonStart +
                "; shareTitle: " + shareTitle + "; categories: " + categories + "; id: " + id +
                "; scripts: " + scripts + "; mainPhoto: " + mainPhoto + "; category: " + category +
                "; isBattle: " + isBattle + "; created: " + created + "; latestResults: " +
                latestResults + "; avgResult: " + avgResult + "; resultCount: " + resultCount +
                "; cityAvg: " + cityAvg + "; cityTime: " + cityTime + "; cityCount: " + cityCount +
                "; userBattleDone: " + userBattleDone + "; sponsoredResults: " + sponsoredResults
                + "; myAnswers: " + myAnswers;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(celebrity);
        dest.writeList(rates);
        dest.writeList(questions);
        dest.writeValue(createdAt);
        dest.writeValue(sponsored);
        dest.writeValue(title);
        dest.writeValue(type);
        dest.writeValue(content);
        dest.writeValue(buttonStart);
        dest.writeValue(shareTitle);
        dest.writeList(categories);
        dest.writeValue(id);
        dest.writeValue(scripts);
        dest.writeValue(mainPhoto);
        dest.writeValue(category);
        dest.writeValue(isBattle);
        dest.writeValue(created);
        dest.writeList(latestResults);
        dest.writeValue(avgResult);
        dest.writeValue(resultCount);
        dest.writeValue(cityAvg);
        dest.writeValue(cityTime);
        dest.writeValue(cityCount);
        dest.writeValue(userBattleDone);
        dest.writeValue(sponsoredResults);
        dest.writeList(myAnswers);
    }

    public int describeContents() {
        return 0;
    }

}
