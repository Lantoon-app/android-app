package com.bazinga.lantoon.home.chapter.lesson.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContinueNext {
    @SerializedName("langid")
    @Expose
    private Integer langid;
    @SerializedName("chapterno")
    @Expose
    private Integer chapterno;
    @SerializedName("lessonno")
    @Expose
    private Integer lessonno;
    @SerializedName("startingquesno")
    @Expose
    private Integer startingquesno;
    @SerializedName("spenttime")
    @Expose
    private String spentTime;
    @SerializedName("unlocked_chapters")
    @Expose
    private String unlockedChapters;

    public Integer getLangid() {
        return langid;
    }

    public void setLangid(Integer langid) {
        this.langid = langid;
    }

    public Integer getChapterno() {
        return chapterno;
    }

    public void setChapterno(Integer chapterno) {
        this.chapterno = chapterno;
    }

    public Integer getLessonno() {
        return lessonno;
    }

    public void setLessonno(Integer lessonno) {
        this.lessonno = lessonno;
    }

    public Integer getStartingquesno() {
        return startingquesno;
    }

    public void setStartingquesno(Integer startingquesno) {
        this.startingquesno = startingquesno;
    }

    public String getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(String spentTime) {
        this.spentTime = spentTime;
    }
    public String getunlockedChapters() {
        return unlockedChapters;
    }

    public void setunlockedChapters(String unlockedChapters) {
        this.unlockedChapters = unlockedChapters;
    }
}
