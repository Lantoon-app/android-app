package com.bazinga.lantoonnew.home.chapter.lesson.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContinueNext {
    @SerializedName("target_type")
    @Expose
    private String targetType;
    @SerializedName("langid")
    @Expose
    private String langid;
    @SerializedName("chapterno")
    @Expose
    private Integer chapterno;
    @SerializedName("evaluation_id")
    @Expose
    private Integer evaluation_id;

    @SerializedName("lessonno")
    @Expose
    private Integer lessonno;
    @SerializedName("startingquesno")
    @Expose
    private Integer startingquesno;
    @SerializedName("spenttime")
    @Expose
    private String spenttime;
    @SerializedName("unlocked_chapters")
    @Expose
    private String unlockedChapters;
    @SerializedName("chapter_type")
    @Expose
    private Integer chapter_type;

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getLangid() {
        return langid;
    }

    public void setLangid(String langid) {
        this.langid = langid;
    }

    public Integer getChapterno() {
        return chapterno;
    }

    public void setChapterno(Integer chapterno) {
        this.chapterno = chapterno;
    }

    public Integer getEvaluation_id() {
        return evaluation_id;
    }

    public void setEvaluation_id(Integer evaluation_id) {
        this.evaluation_id = evaluation_id;
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

    public String getSpenttime() {
        return spenttime;
    }

    public void setSpenttime(String spenttime) {
        this.spenttime = spenttime;
    }

    public String getUnlockedChapters() {
        return unlockedChapters;
    }

    public void setUnlockedChapters(String unlockedChapters) {
        this.unlockedChapters = unlockedChapters;
    }

    public Integer getChapterType() {
        return chapter_type;
    }

    public void setChapterType(Integer chapter_type) {
        this.chapter_type = chapter_type;
    }
}
