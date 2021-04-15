package com.bazinga.lantoon.home.chapter.model;

import com.bazinga.lantoon.retrofit.Status;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Chapter {
    @SerializedName("ChapterID")
    @Expose
    private String chapterID;
    @SerializedName("LanguageName")
    @Expose
    private String languageName;
    @SerializedName("ChapterNo")
    @Expose
    private String chapterNo;
    @SerializedName("NameInSchool")
    @Expose
    private String nameInSchool;
    @SerializedName("NameInRetail")
    @Expose
    private String nameInRetail;
    @SerializedName("ActiveStatus")
    @Expose
    private String activeStatus;
    @SerializedName("VisibilityStatus")
    @Expose
    private String visibilityStatus;
    @SerializedName("CreatedBy")
    @Expose
    private String createdBy;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;
    @SerializedName("VisibilityActivatedOn")
    @Expose
    private String visibilityActivatedOn;
    @SerializedName("completedLessons")
    @Expose
    private Integer completedLessons;
    @SerializedName("activeLesson")
    @Expose
    private ActiveLesson activeLesson;

    public String getChapterID() {
        return chapterID;
    }

    public void setChapterID(String chapterID) {
        this.chapterID = chapterID;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getChapterNo() {
        return chapterNo;
    }

    public void setChapterNo(String chapterNo) {
        this.chapterNo = chapterNo;
    }

    public String getNameInSchool() {
        return nameInSchool;
    }

    public void setNameInSchool(String nameInSchool) {
        this.nameInSchool = nameInSchool;
    }

    public String getNameInRetail() {
        return nameInRetail;
    }

    public void setNameInRetail(String nameInRetail) {
        this.nameInRetail = nameInRetail;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getVisibilityStatus() {
        return visibilityStatus;
    }

    public void setVisibilityStatus(String visibilityStatus) {
        this.visibilityStatus = visibilityStatus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getVisibilityActivatedOn() {
        return visibilityActivatedOn;
    }

    public void setVisibilityActivatedOn(String visibilityActivatedOn) {
        this.visibilityActivatedOn = visibilityActivatedOn;
    }

    public Integer getCompletedLessons() {
        return completedLessons;
    }

    public void setCompletedLessons(Integer completedLessons) {
        this.completedLessons = completedLessons;
    }
    public ActiveLesson getActiveLesson() {
        return activeLesson;
    }

    public void setActiveLesson(ActiveLesson activeLesson) {
        this.activeLesson = activeLesson;
    }
}
