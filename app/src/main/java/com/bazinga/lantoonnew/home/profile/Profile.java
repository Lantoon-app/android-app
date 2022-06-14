package com.bazinga.lantoonnew.home.profile;

import com.bazinga.lantoonnew.registration.model.DurationData;
import com.bazinga.lantoonnew.retrofit.Status;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Profile {
    @SerializedName("data")
    @Expose
    private ProfileData profileData;
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("duration")
    @Expose
    private List<DurationData> duration = null;

    public ProfileData getProfileData() {
        return profileData;
    }

    public void setProfileData(ProfileData profileData) {
        this.profileData = profileData;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<DurationData> getDurationData() {
        return duration;
    }

    public void setDurationData(List<DurationData> duration) {
        this.duration = duration;
    }
}
