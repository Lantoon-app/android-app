package com.bazinga.lantoon.home.chapter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RetakeStatus {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("language_id")
    @Expose
    private String languageId;
    @SerializedName("chapter_no")
    @Expose
    private String chapterNo;
    @SerializedName("lesson_no")
    @Expose
    private Integer lessonNo;
    @SerializedName("evaluation_id")
    @Expose
    private String evaluationId;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String getChapterNo() {
        return chapterNo;
    }

    public void setChapterNo(String chapterNo) {
        this.chapterNo = chapterNo;
    }

    public Integer getLessonNo() {
        return lessonNo;
    }

    public void setLessonNo(Integer lessonNo) {
        this.lessonNo = lessonNo;
    }

    public String getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(String evaluationId) {
        this.evaluationId = evaluationId;
    }
}