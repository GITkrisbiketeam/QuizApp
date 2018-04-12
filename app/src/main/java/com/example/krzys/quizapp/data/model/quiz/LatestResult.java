
package com.example.krzys.quizapp.data.model.quiz;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LatestResult {

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

}
