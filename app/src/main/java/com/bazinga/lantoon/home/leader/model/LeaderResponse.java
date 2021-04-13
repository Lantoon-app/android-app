package com.bazinga.lantoon.home.leader.model;

import com.bazinga.lantoon.home.chapter.lesson.model.ContinueNext;
import com.bazinga.lantoon.home.chapter.model.Chapter;
import com.bazinga.lantoon.retrofit.Status;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LeaderResponse {
    @SerializedName("status")
    @Expose
    private Status status;
    @Expose
    private List<Leader> data = null;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Leader> getData() {
        return data;
    }

    public void setData(List<Leader> data) {
        this.data = data;
    }


}
