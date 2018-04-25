
package com.example.krzys.quizapp.data.dto.quiz;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FlagResult implements Parcelable
{

    @SerializedName("image")
    @Expose
    private Image image;
    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("content")
    @Expose
    private String content;
    public final static Creator<FlagResult> CREATOR = new Creator<FlagResult>() {


        @SuppressWarnings({
            "unchecked"
        })
        public FlagResult createFromParcel(Parcel in) {
            return new FlagResult(in);
        }

        public FlagResult[] newArray(int size) {
            return (new FlagResult[size]);
        }

    }
    ;

    protected FlagResult(Parcel in) {
        this.image = ((Image) in.readValue((Image.class.getClassLoader())));
        this.flag = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.content = ((String) in.readValue((String.class.getClassLoader())));
    }

    public FlagResult() {
    }

    public FlagResult(Image image, String flag, String title, String content) {
        this.image = image;
        this.flag = flag;
        this.title = title;
        this.content = content;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "image: " + image + "; flag: " + flag + "; title: " + title + "; content: " +
                content;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(image);
        dest.writeValue(flag);
        dest.writeValue(title);
        dest.writeValue(content);
    }

    public int describeContents() {
        return  0;
    }

}
