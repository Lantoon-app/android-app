package com.bazinga.lantoon.login.data.model;

import com.bazinga.lantoon.retrofit.Status;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUserResponse {


    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("data")
    @Expose
    private LoginData loginData;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LoginData getLoginData() {
        return loginData;
    }

    public void setData(LoginData loginData) {
        this.loginData = loginData;
    }
}