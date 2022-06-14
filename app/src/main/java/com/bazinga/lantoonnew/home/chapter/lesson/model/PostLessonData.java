package com.bazinga.lantoonnew.home.chapter.lesson.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostLessonData {
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
