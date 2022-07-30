package com.bazinga.lantoonnew.home.chapter.lesson.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostLessonData {

    @SerializedName("continue_status")
    @Expose
    private Integer continueStatus;
    @SerializedName("failed_chapters")
    @Expose
    private String failedChapters;
    @SerializedName("input_data")
    @Expose
    private EvaluationScore inputData;
    @SerializedName("gem")
    @Expose
    private Integer gem;
    @SerializedName("award")
    @Expose
    private Integer award;
    @SerializedName("percentage")
    @Expose
    private Double percentage;
    @SerializedName("responsetime")
    @Expose
    private Double responsetime;

    public Integer getContinueStatus() {
        return continueStatus;
    }

    public void setContinueStatus(Integer continueStatus) {
        this.continueStatus = continueStatus;
    }

    public String getFailedChapters() {
        return failedChapters;
    }

    public void setFailedChapters(String failedChapters) {
        this.failedChapters = failedChapters;
    }

    public EvaluationScore getEvaluationScore() {
        return inputData;
    }

    public void setEvaluationScore(EvaluationScore inputData) {
        this.inputData = inputData;
    }
    public Integer getGem() {
        return gem;
    }

    public void setGem(Integer gem) {
        this.gem = gem;
    }

    public Integer getAward() {
        return award;
    }

    public void setAward(Integer award) {
        this.award = award;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Double getResponsetime() {
        return responsetime;
    }

    public void setResponsetime(Double responsetime) {
        this.responsetime = responsetime;
    }

}
