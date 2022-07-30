package com.bazinga.lantoonnew.home.chapter.lesson.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScoreDetails {

    @SerializedName("chapter_no")
    @Expose
    private Integer chapterNo;
    @SerializedName("correct")
    @Expose
    private Integer correct;
    @SerializedName("incorrect")
    @Expose
    private Integer incorrect;
    @SerializedName("total_ques")
    @Expose
    private Integer totalQues;

    public Integer getChapterNo() {
        return chapterNo;
    }

    public void setChapterNo(Integer chapterNo) {
        this.chapterNo = chapterNo;
    }

    public Integer getCorrect() {
        return correct;
    }

    public void setCorrect(Integer correct) {
        this.correct = correct;
    }

    public Integer getIncorrect() {
        return incorrect;
    }

    public void setIncorrect(Integer incorrect) {
        this.incorrect = incorrect;
    }

    public Integer getTotalQues() {
        return totalQues;
    }

    public void setTotalQues(Integer totalQues) {
        this.totalQues = totalQues;
    }

}
