package com.bazinga.lantoonnew.home.chapter.lesson.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Score {
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("langid")
    @Expose
    private String langid;
    @SerializedName("chaptno")
    @Expose
    private String chaptno;
    @SerializedName("lessonno")
    @Expose
    private String lessonno;
    @SerializedName("gcode")
    @Expose
    private String gcode;
    @SerializedName("pmark")
    @Expose
    private String pmark;
    @SerializedName("nmark")
    @Expose
    private String nmark;
    @SerializedName("out_of_total")
    @Expose
    private String outOfTotal;
    @SerializedName("award")
    @Expose
    private String award;
    @SerializedName("gem")
    @Expose
    private String gem;
    @SerializedName("spent_time")
    @Expose
    private String spentTime;
    @SerializedName("completedques")
    @Expose
    private String completedques;
    @SerializedName("totalques")
    @Expose
    private String totalques;
    @SerializedName("attemptcount")
    @Expose
    private Map<String,String> attemptcount = null;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLangid() {
        return langid;
    }

    public void setLangid(String langid) {
        this.langid = langid;
    }

    public String getChaptno() {
        return chaptno;
    }

    public void setChaptno(String chaptno) {
        this.chaptno = chaptno;
    }

    public String getLessonno() {
        return lessonno;
    }

    public void setLessonno(String lessonno) {
        this.lessonno = lessonno;
    }

    public String getGcode() {
        return gcode;
    }

    public void setGcode(String gcode) {
        this.gcode = gcode;
    }

    public String getPmark() {
        return pmark;
    }

    public void setPmark(String pmark) {
        this.pmark = pmark;
    }

    public String getNmark() {
        return nmark;
    }

    public void setNmark(String nmark) {
        this.nmark = nmark;
    }

    public String getOutOfTotal() {
        return outOfTotal;
    }

    public void setOutOfTotal(String outOfTotal) {
        this.outOfTotal = outOfTotal;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getGem() {
        return gem;
    }

    public void setGem(String gem) {
        this.gem = gem;
    }

    public String getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(String spentTime) {
        this.spentTime = spentTime;
    }


    public String getCompletedques() {
        return completedques;
    }

    public void setCompletedques(String completedques) {
        this.completedques = completedques;
    }

    public String getTotalques() {
        return totalques;
    }

    public void setTotalques(String totalques) {
        this.totalques = totalques;
    }

    public Map<String, String> getAttemptcount() {
        return attemptcount;
    }

    public void setAttemptcount(Map<String,String> attemptcount) {
        this.attemptcount = attemptcount;
    }

}

