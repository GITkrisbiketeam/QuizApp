package com.example.krzys.quizapp.data.dto.quiz;

import android.arch.persistence.room.Embedded;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Answer implements Parcelable {

    @Embedded(prefix = "image")
    @SerializedName("image")
    @Expose
    private Image image;

    @SerializedName("order")
    @Expose
    private Integer order;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("isCorrect")
    @Expose
    private Integer isCorrect;

    @SerializedName("votes")
    @Expose
    private Integer votes;

    @Embedded(prefix = "flag_answer")
    @SerializedName("flag_answer")
    @Expose
    private FlagAnswer flagAnswer;

    public final static Creator<Answer> CREATOR = new Creator<Answer>() {


        @SuppressWarnings({"unchecked"})
        public Answer createFromParcel(Parcel in) {
            return new Answer(in);
        }

        public Answer[] newArray(int size) {
            return (new Answer[size]);
        }

    };

    protected Answer(Parcel in) {
        this.image = ((Image) in.readValue((Image.class.getClassLoader())));
        this.order = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.text = ((String) in.readValue((String.class.getClassLoader())));
        this.isCorrect = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.votes = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.flagAnswer = ((FlagAnswer) in.readValue((FlagAnswer.class.getClassLoader())));
    }

    public Answer() {
    }

    public Answer(Image image, Integer order, String text, Integer isCorrect, Integer votes,
                  FlagAnswer flagAnswer) {
        this.image = image;
        this.order = order;
        this.text = text;
        this.isCorrect = isCorrect;
        this.votes = votes;
        this.flagAnswer = flagAnswer;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Integer isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public FlagAnswer getFlagAnswer() {
        return flagAnswer;
    }

    public void setFlagAnswer(FlagAnswer flagAnswer) {
        this.flagAnswer = flagAnswer;
    }

    @Override
    public String toString() {
        return "image: " + image + "; order: " + order + "; text: " + text + "; isCorrect: " +
                isCorrect + "; votes: " + votes;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(image);
        dest.writeValue(order);
        dest.writeValue(text);
        dest.writeValue(isCorrect);
        dest.writeValue(votes);
        dest.writeValue(flagAnswer);
    }

    public int describeContents() {
        return 0;
    }

}
