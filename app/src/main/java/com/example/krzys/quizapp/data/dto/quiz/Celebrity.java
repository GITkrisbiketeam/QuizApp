package com.example.krzys.quizapp.data.dto.quiz;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Celebrity implements Parcelable {

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
    public final static Creator<Celebrity> CREATOR = new Creator<Celebrity>() {


        @SuppressWarnings({"unchecked"})
        public Celebrity createFromParcel(Parcel in) {
            return new Celebrity(in);
        }

        public Celebrity[] newArray(int size) {
            return (new Celebrity[size]);
        }

    };

    protected Celebrity(Parcel in) {
        this.result = ((String) in.readValue((String.class.getClassLoader())));
        this.imageAuthor = ((String) in.readValue((String.class.getClassLoader())));
        this.imageHeight = ((String) in.readValue((String.class.getClassLoader())));
        this.imageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.show = ((Long) in.readValue((Long.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.imageTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.imageWidth = ((String) in.readValue((String.class.getClassLoader())));
        this.content = ((String) in.readValue((String.class.getClassLoader())));
        this.imageSource = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Celebrity() {
    }

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

    @Override
    public String toString() {
        return "result: " + result + "; imageAuthor: " + imageAuthor + "; imageHeight: " +
                imageHeight + "; imageUrl: " + imageUrl + "; show: " + show + "; name: " + name +
                "; imageTitle: " + imageTitle + "; imageWidth: " + imageWidth + "; content: " +
                content + "; imageSource: " + imageSource;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(result);
        dest.writeValue(imageAuthor);
        dest.writeValue(imageHeight);
        dest.writeValue(imageUrl);
        dest.writeValue(show);
        dest.writeValue(name);
        dest.writeValue(imageTitle);
        dest.writeValue(imageWidth);
        dest.writeValue(content);
        dest.writeValue(imageSource);
    }

    public int describeContents() {
        return 0;
    }

}
