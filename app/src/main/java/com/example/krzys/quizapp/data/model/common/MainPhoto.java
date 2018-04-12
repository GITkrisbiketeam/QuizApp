package com.example.krzys.quizapp.data.model.common;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class MainPhoto implements Parcelable {

    @SerializedName("author")
    @Expose
    private String author;
    @PrimaryKey(autoGenerate = true)
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("media_id")
    @Expose
    private Long mediaId;
    public final static Parcelable.Creator<MainPhoto> CREATOR = new Creator<MainPhoto>() {


        @SuppressWarnings({"unchecked"})
        public MainPhoto createFromParcel(Parcel in) {
            return new MainPhoto(in);
        }

        public MainPhoto[] newArray(int size) {
            return (new MainPhoto[size]);
        }

    };

    protected MainPhoto(Parcel in) {
        this.author = ((String) in.readValue((String.class.getClassLoader())));
        this.width = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.source = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.url = ((String) in.readValue((String.class.getClassLoader())));
        this.height = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.mediaId = ((Long) in.readValue((Long.class.getClassLoader())));
    }

    public MainPhoto() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    @Override
    public String toString() {
        return "author: " + author + "; width: " + width + "; source: " + source + "; title: " +
                title + "; url: " + url + "; height: " + height + "; mediaId: " + mediaId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(author);
        dest.writeValue(width);
        dest.writeValue(source);
        dest.writeValue(title);
        dest.writeValue(url);
        dest.writeValue(height);
        dest.writeValue(mediaId);
    }

    public int describeContents() {
        return 0;
    }

}
