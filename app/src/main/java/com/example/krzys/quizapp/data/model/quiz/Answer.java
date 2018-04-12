
package com.example.krzys.quizapp.data.model.quiz;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Answer {

    @SerializedName("image")
    @Expose
    private Image_ image;
    @SerializedName("order")
    @Expose
    private Long order;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("isCorrect")
    @Expose
    private Long isCorrect;

    public Image_ getImage() {
        return image;
    }

    public void setImage(Image_ image) {
        this.image = image;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Long isCorrect) {
        this.isCorrect = isCorrect;
    }

}
