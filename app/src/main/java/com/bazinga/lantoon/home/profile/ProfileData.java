package com.bazinga.lantoon.home.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileData {
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("uname")
    @Expose
    private String uname;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("countrycode")
    @Expose
    private String countrycode;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("minsperday")
    @Expose
    private Integer minsperday;
    @SerializedName("picture")
    @Expose
    private String picture;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getMindurationperday() {
        return minsperday;
    }

    public void setMindurationperday(Integer minsperday) {
        this.minsperday = minsperday;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
