
package com.example.krzys.quizapp.data.model.quizzes;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuizListData {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("items")
    @Expose
    private List<QuizItem> quizItem = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<QuizItem> getItems() {
        return quizItem;
    }

    public void setItems(List<QuizItem> quizItem) {
        this.quizItem = quizItem;
    }

}
