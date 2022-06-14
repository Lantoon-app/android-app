package com.bazinga.lantoonnew.home.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfilePictureData {
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("profilepic")
    @Expose
    private String profilepic;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }
}
