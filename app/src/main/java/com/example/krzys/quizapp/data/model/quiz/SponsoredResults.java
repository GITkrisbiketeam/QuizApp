package com.example.krzys.quizapp.data.model.quiz;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class SponsoredResults implements Parcelable {

    @SerializedName("imageAuthor")
    @Expose
    private String imageAuthor;
    @PrimaryKey(autoGenerate = true)
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
    public final static Creator<SponsoredResults> CREATOR = new Creator<SponsoredResults>() {


        @SuppressWarnings({"unchecked"})
        public SponsoredResults createFromParcel(Parcel in) {
            return new SponsoredResults(in);
        }

        public SponsoredResults[] newArray(int size) {
            return (new SponsoredResults[size]);
        }

    };

    protected SponsoredResults(Parcel in) {
        this.imageAuthor = ((String) in.readValue((String.class.getClassLoader())));
        this.imageHeight = ((String) in.readValue((String.class.getClassLoader())));
        this.imageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.imageWidth = ((String) in.readValue((String.class.getClassLoader())));
        this.textColor = ((String) in.readValue((String.class.getClassLoader())));
        this.content = ((String) in.readValue((String.class.getClassLoader())));
        this.mainColor = ((String) in.readValue((String.class.getClassLoader())));
        this.imageSource = ((String) in.readValue((String.class.getClassLoader())));
    }

    public SponsoredResults() {
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

    @Override
    public String toString() {
        return "imageAuthor: " + imageAuthor + "; imageHeight: " + imageHeight + "; imageUrl: " +
                imageUrl + "; imageWidth: " + imageWidth + "; textColor: " + textColor + "; " +
                "content: " + content + "; mainColor: " + mainColor + "; imageSource: " +
                imageSource;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(imageAuthor);
        dest.writeValue(imageHeight);
        dest.writeValue(imageUrl);
        dest.writeValue(imageWidth);
        dest.writeValue(textColor);
        dest.writeValue(content);
        dest.writeValue(mainColor);
        dest.writeValue(imageSource);
    }

    public int describeContents() {
        return 0;
    }

}
