package com.example.krzys.quizapp.data.model.common;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Category implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("uid")
    @Expose
    private Long uid;
    @SerializedName("secondaryCid")
    @Expose
    private String secondaryCid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    public final static Creator<Category> CREATOR = new Creator<Category>() {


        @SuppressWarnings({"unchecked"})
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        public Category[] newArray(int size) {
            return (new Category[size]);
        }

    };

    protected Category(Parcel in) {
        this.uid = ((Long) in.readValue((Long.class.getClassLoader())));
        this.secondaryCid = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Category() {
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getSecondaryCid() {
        return secondaryCid;
    }

    public void setSecondaryCid(String secondaryCid) {
        this.secondaryCid = secondaryCid;
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
        return "uid: " + uid + "; secondaryCid: " + secondaryCid + "; name: " + name + "; type: "
                + type;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(uid);
        dest.writeValue(secondaryCid);
        dest.writeValue(name);
        dest.writeValue(type);
    }

    public int describeContents() {
        return 0;
    }

}
