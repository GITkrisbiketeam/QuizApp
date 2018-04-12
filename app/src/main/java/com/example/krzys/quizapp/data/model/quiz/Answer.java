package com.example.krzys.quizapp.data.model.quiz;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Answer implements Parcelable {

    @Embedded(prefix = "image__")
    @SerializedName("image")
    @Expose
    private Image_ image;
    @PrimaryKey(autoGenerate = true)
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("isCorrect")
    @Expose
    private Integer isCorrect;
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
        this.image = ((Image_) in.readValue((Image_.class.getClassLoader())));
        this.order = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.text = ((String) in.readValue((String.class.getClassLoader())));
        this.isCorrect = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public Answer() {
    }

    public Image_ getImage() {
        return image;
    }

    public void setImage(Image_ image) {
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

    @Override
    public String toString() {
        return "image: " + image + "; order: " + order + "; text: " + text + "; isCorrect: " +
                isCorrect;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(image);
        dest.writeValue(order);
        dest.writeValue(text);
        dest.writeValue(isCorrect);
    }

    public int describeContents() {
        return 0;
    }

}
