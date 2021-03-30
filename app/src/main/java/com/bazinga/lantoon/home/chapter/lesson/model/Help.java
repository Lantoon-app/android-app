package com.bazinga.lantoon.home.chapter.lesson.model;

import com.bazinga.lantoon.login.data.model.LoginData;
import com.bazinga.lantoon.retrofit.Status;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Help {
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("data")
    @Expose
    private HelpData data;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public HelpData getData() {
        return data;
    }

    public void setData(HelpData data) {
        this.data = data;
    }
}
