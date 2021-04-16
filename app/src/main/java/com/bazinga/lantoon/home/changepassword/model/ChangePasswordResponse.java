package com.bazinga.lantoon.home.changepassword.model;

import com.bazinga.lantoon.retrofit.Status;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePasswordResponse {
    @SerializedName("status")
    @Expose
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

