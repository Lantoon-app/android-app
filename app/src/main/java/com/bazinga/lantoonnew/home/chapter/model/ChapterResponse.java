package com.bazinga.lantoonnew.home.chapter.model;

import com.bazinga.lantoonnew.home.chapter.lesson.model.ContinueNext;
import com.bazinga.lantoonnew.retrofit.Status;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChapterResponse {
    @SerializedName("status")
    @Expose
    private Status status;
    @Expose
    private List<Chapter> data = null;
    @SerializedName("continuenext")
    @Expose
    private ContinueNext continuenext;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Chapter> getData() {
        return data;
    }

    public void setData(List<Chapter> data) {
        this.data = data;
    }

    public ContinueNext getContinuenext() {
        return continuenext;
    }

    public void setContinuenext(ContinueNext continuenext) {
        this.continuenext = continuenext;
    }
}
