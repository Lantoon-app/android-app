package com.bazinga.lantoonnew.home.chapter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class  ActiveLesson {
    @SerializedName("lessonno")
    @Expose
    private Integer lessonno;
    @SerializedName("startingquesno")
    @Expose
    private Integer startingquesno;

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

}
