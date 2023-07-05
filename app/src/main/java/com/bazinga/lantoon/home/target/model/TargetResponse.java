package com.bazinga.lantoon.home.target.model;

import com.bazinga.lantoon.retrofit.Status;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TargetResponse {
    @SerializedName("data")
    @Expose
    private List<Target> targetListData = null;
    @SerializedName("status")
    @Expose
    private Status status;

    public List<Target> getTargetListData() {
        return targetListData;
    }

    public void setTargetListData(List<Target> targetListData) {
        this.targetListData = targetListData;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
