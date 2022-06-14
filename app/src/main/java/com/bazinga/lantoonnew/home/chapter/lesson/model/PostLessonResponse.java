package com.bazinga.lantoonnew.home.chapter.lesson.model;

import com.bazinga.lantoonnew.retrofit.Status;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostLessonResponse {
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("data")
    @Expose
    private PostLessonData postLessonData;
    @SerializedName("continuenext")
    @Expose
    private ContinueNext continuenext;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public PostLessonData getPostLessonData() {
        return postLessonData;
    }

    public void setPostLessonData(PostLessonData postLessonData) {
        this.postLessonData = postLessonData;
    }

    public ContinueNext getContinuenext() {
        return continuenext;
    }

    public void setContinuenext(ContinueNext continuenext) {
        this.continuenext = continuenext;
    }
}
