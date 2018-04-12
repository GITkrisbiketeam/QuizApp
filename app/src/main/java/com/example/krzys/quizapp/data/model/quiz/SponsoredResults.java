
package com.example.krzys.quizapp.data.model.quiz;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SponsoredResults {

    @SerializedName("imageAuthor")
    @Expose
    private String imageAuthor;
    @SerializedName("imageHeight")
    @Expose
    private String imageHeight;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("imageWidth")
    @Expose
    private String imageWidth;
    @SerializedName("textColor")
    @Expose
    private String textColor;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("mainColor")
    @Expose
    private String mainColor;
    @SerializedName("imageSource")
    @Expose
    private String imageSource;

    public String getImageAuthor() {
        return imageAuthor;
    }

    public void setImageAuthor(String imageAuthor) {
        this.imageAuthor = imageAuthor;
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMainColor() {
        return mainColor;
    }

    public void setMainColor(String mainColor) {
        this.mainColor = mainColor;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

}
