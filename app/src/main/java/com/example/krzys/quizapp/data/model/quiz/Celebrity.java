
package com.example.krzys.quizapp.data.model.quiz;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Celebrity {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("imageAuthor")
    @Expose
    private String imageAuthor;
    @SerializedName("imageHeight")
    @Expose
    private String imageHeight;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("show")
    @Expose
    private Long show;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("imageTitle")
    @Expose
    private String imageTitle;
    @SerializedName("imageWidth")
    @Expose
    private String imageWidth;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("imageSource")
    @Expose
    private String imageSource;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

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

    public Long getShow() {
        return show;
    }

    public void setShow(Long show) {
        this.show = show;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

}
