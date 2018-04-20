package com.example.krzys.quizapp.data.dto.quizzes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tag implements Parcelable {

    @SerializedName("uid")
    @Expose
    private Long uid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    public final static Parcelable.Creator<Tag> CREATOR = new Creator<Tag>() {


        @SuppressWarnings({"unchecked"})
        public Tag createFromParcel(Parcel in) {
            return new Tag(in);
        }

        public Tag[] newArray(int size) {
            return (new Tag[size]);
        }

    };

    protected Tag(Parcel in) {
        this.uid = ((Long) in.readValue((Long.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Tag() {
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "uid: " + uid + "; name: " + name + "; type: " + type;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(uid);
        dest.writeValue(name);
        dest.writeValue(type);
    }

    public int describeContents() {
        return 0;
    }

}
