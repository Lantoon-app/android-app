package com.bazinga.lantoonnew.home.profile;

import com.bazinga.lantoonnew.retrofit.Status;
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
