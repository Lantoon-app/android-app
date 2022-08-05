package com.bazinga.lantoon.home.profile;

import com.bazinga.lantoon.retrofit.Status;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfilePicture {
    @SerializedName("data")
    @Expose
    private ProfilePictureData profilePictureData;
    @SerializedName("status")
    @Expose
    private Status status;

    public ProfilePictureData getData() {
        return profilePictureData;
    }

    public void setData(ProfilePictureData profilePictureData) {
        this.profilePictureData = profilePictureData;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
