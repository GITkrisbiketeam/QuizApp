
package com.example.krzys.quizapp.data.model.quizzes;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuizzesListData {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("items")
    @Expose
    private List<QuizzesItem> quizzesItem = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<QuizzesItem> getItems() {
        return quizzesItem;
    }

    public void setItems(List<QuizzesItem> quizzesItem) {
        this.quizzesItem = quizzesItem;
    }

}
