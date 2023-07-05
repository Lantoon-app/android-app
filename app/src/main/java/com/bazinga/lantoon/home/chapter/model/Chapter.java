package com.bazinga.lantoon.home.chapter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
    @SerializedName("chapter_type")
    @Expose
    private String chapterType;
    @SerializedName("chapter_level")
    @Expose
    private String chapterLevel;
    @SerializedName("ActiveStatus")
    @Expose
    private String activeStatus;
    @SerializedName("lock_status")
    @Expose
    private String lockStatus;
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
    @SerializedName("gemcount")
    @Expose
    private Integer gemcount;
    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName("retake_status")
    @Expose
    private RetakeStatus retakeStatus;
    @SerializedName("evaluation_id")
    @Expose
    private String evaluationId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("chapters")
    @Expose
    private String chapters;
    @SerializedName("evaluation_number")
    @Expose
    private String evaluationNumber;
    @SerializedName("chapter_list")
    @Expose
    private List<String> chapterList = null;
    @SerializedName("completed")
    @Expose
    private Integer completed;

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

    public String getChapterType() {
        return chapterType;
    }

    public void setChapterType(String chapterType) {
        this.chapterType = chapterType;
    }

    public String getChapterLevel() {
        return chapterLevel;
    }

    public void setChapterLevel(String chapterLevel) {
        this.chapterLevel = chapterLevel;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
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

    public Integer getGemcount() {
        return gemcount;
    }

    public void setGemcount(Integer gemcount) {
        this.gemcount = gemcount;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public RetakeStatus getRetakeStatus() {
        return retakeStatus;
    }

    public void setRetakeStatus(RetakeStatus retakeStatus) {
        this.retakeStatus = retakeStatus;
    }

    public String getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(String evaluationId) {
        this.evaluationId = evaluationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChapters() {
        return chapters;
    }

    public void setChapters(String chapters) {
        this.chapters = chapters;
    }

    public String getEvaluationNumber() {
        return evaluationNumber;
    }

    public void setEvaluationNumber(String evaluationNumber) {
        this.evaluationNumber = evaluationNumber;
    }

    public List<String> getChapterList() {
        return chapterList;
    }

    public void setChapterList(List<String> chapterList) {
        this.chapterList = chapterList;
    }

    public Integer getCompleted() {
        return completed;
    }

    public void setCompleted(Integer completed) {
        this.completed = completed;
    }

}
