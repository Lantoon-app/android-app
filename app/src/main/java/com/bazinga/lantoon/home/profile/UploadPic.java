package com.bazinga.lantoon.home.profile;

import com.bazinga.lantoon.retrofit.Status;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;

public class UploadPic {

    @SerializedName("profilepic")
    @Expose
    private MultipartBody.Part imageFile;
    @SerializedName("userid")
    @Expose
    private String userid;

    public MultipartBody.Part getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartBody.Part imageFile) {
        this.imageFile = imageFile;
    }

    public String getUerId() {
        return userid;
    }

    public void setUerId(String userid) {
        this.userid = userid;
    }
}
