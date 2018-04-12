package com.example.krzys.quizapp.data.model.quiz;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class LatestResult implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("city")
    @Expose
    private Long city;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("result")
    @Expose
    private Double result;
    @SerializedName("resolveTime")
    @Expose
    private Long resolveTime;
    public final static Creator<LatestResult> CREATOR = new Creator<LatestResult>() {


        @SuppressWarnings({"unchecked"})
        public LatestResult createFromParcel(Parcel in) {
            return new LatestResult(in);
        }

        public LatestResult[] newArray(int size) {
            return (new LatestResult[size]);
        }

    };

    protected LatestResult(Parcel in) {
        this.city = ((Long) in.readValue((Long.class.getClassLoader())));
        this.endDate = ((String) in.readValue((String.class.getClassLoader())));
        this.result = ((Double) in.readValue((Double.class.getClassLoader())));
        this.resolveTime = ((Long) in.readValue((Long.class.getClassLoader())));
    }

    public LatestResult() {
    }

    public Long getCity() {
        return city;
    }

    public void setCity(Long city) {
        this.city = city;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public Long getResolveTime() {
        return resolveTime;
    }

    public void setResolveTime(Long resolveTime) {
        this.resolveTime = resolveTime;
    }

    @Override
    public String toString() {
        return "city: " + city + "; endDate: " + endDate + "; result: " + result + "; " +
                "resolveTime: " + resolveTime;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(city);
        dest.writeValue(endDate);
        dest.writeValue(result);
        dest.writeValue(resolveTime);
    }

    public int describeContents() {
        return 0;
    }

}
