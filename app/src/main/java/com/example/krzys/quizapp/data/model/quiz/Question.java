package com.example.krzys.quizapp.data.model.quiz;

import java.util.ArrayList;
import java.util.List;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.krzys.quizapp.data.db.converter.AnswerConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Question implements Parcelable {

    @Embedded(prefix = "image_")
    @SerializedName("image")
    @Expose
    private Image image;

    @TypeConverters(AnswerConverter.class)
    @SerializedName("answers")
    @Expose
    private List<Answer> answers = null;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("answer")
    @Expose
    private String answer;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("order")
    @Expose
    private Integer order;

    public final static Creator<Question> CREATOR = new Creator<Question>() {


        @SuppressWarnings({"unchecked"})
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        public Question[] newArray(int size) {
            return (new Question[size]);
        }

    };

    protected Question(Parcel in) {
        this.image = ((Image) in.readValue((Image.class.getClassLoader())));
        this.answers = new ArrayList<>();
        in.readList(this.answers, (Answer.class.getClassLoader()));
        this.text = ((String) in.readValue((String.class.getClassLoader())));
        this.answer = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.order = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public Question() {
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "image: " + image + "; answers: " + answers + "; text: " + text + "; answer: " +
                answer + "; type: " + type + "; order: " + order;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(image);
        dest.writeList(answers);
        dest.writeValue(text);
        dest.writeValue(answer);
        dest.writeValue(type);
        dest.writeValue(order);
    }

    public int describeContents() {
        return 0;
    }

}
