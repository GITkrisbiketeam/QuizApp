package com.example.krzys.quizapp.data.model.quiz;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Image_ implements Parcelable {

    @SerializedName("author")
    @Expose
    private String author;
    @PrimaryKey(autoGenerate = true)
    @SerializedName("width")
    @Expose
    private String width;
    @SerializedName("mediaId")
    @Expose
    private String mediaId;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("height")
    @Expose
    private String height;
    public final static Creator<Image_> CREATOR = new Creator<Image_>() {


        @SuppressWarnings({"unchecked"})
        public Image_ createFromParcel(Parcel in) {
            return new Image_(in);
        }

        public Image_[] newArray(int size) {
            return (new Image_[size]);
        }

    };

    protected Image_(Parcel in) {
        this.author = ((String) in.readValue((String.class.getClassLoader())));
        this.width = ((String) in.readValue((String.class.getClassLoader())));
        this.mediaId = ((String) in.readValue((String.class.getClassLoader())));
        this.source = ((String) in.readValue((String.class.getClassLoader())));
        this.url = ((String) in.readValue((String.class.getClassLoader())));
        this.height = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Image_() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "author: " + author + "; width: " + width + "; mediaId: " + mediaId + "; source: " +
                source + "; url: " + url + "; height: " + height;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(author);
        dest.writeValue(width);
        dest.writeValue(mediaId);
        dest.writeValue(source);
        dest.writeValue(url);
        dest.writeValue(height);
    }

    public int describeContents() {
        return 0;
    }

}
