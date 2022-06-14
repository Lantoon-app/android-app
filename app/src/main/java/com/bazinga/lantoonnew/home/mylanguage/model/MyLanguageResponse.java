package com.bazinga.lantoonnew.home.mylanguage.model;

import com.bazinga.lantoonnew.retrofit.Status;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyLanguageResponse {
    @SerializedName("data")
    @Expose
    private List<MyLanguageData> myLanguageData = null;
    @SerializedName("status")
    @Expose
    private Status status;

    public List<MyLanguageData> getData() {
        return myLanguageData;
    }

    public void setData(List<MyLanguageData> myLanguageData) {
        this.myLanguageData = myLanguageData;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
