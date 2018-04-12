package com.example.krzys.quizapp.data.model.common;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Category_ implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("name")
    @Expose
    private String name;
    public final static Creator<Category_> CREATOR = new Creator<Category_>() {


        @SuppressWarnings({"unchecked"})
        public Category_ createFromParcel(Parcel in) {
            return new Category_(in);
        }

        public Category_[] newArray(int size) {
            return (new Category_[size]);
        }

    };

    protected Category_(Parcel in) {
        this.id = ((Long) in.readValue((Long.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Category_() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "id: " + id + "; name: " + name;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
    }

    public int describeContents() {
        return 0;
    }

}
