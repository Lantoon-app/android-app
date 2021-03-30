package com.bazinga.lantoon.login.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginData {
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
    @SerializedName("learnlang")
    @Expose
    private Integer learnlang;
    @SerializedName("knownlang")
    @Expose
    private Integer knownlang;
    @SerializedName("userrole")
    @Expose
    private Integer userrole;
    @SerializedName("mindurationperday")
    @Expose
    private Integer mindurationperday;
    @SerializedName("registrationtype")
    @Expose
    private Integer registrationtype;

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

    public Integer getLearnlang() {
        return learnlang;
    }

    public void setLearnlang(Integer learnlang) {
        this.learnlang = learnlang;
    }

    public Integer getKnownlang() {
        return knownlang;
    }

    public void setKnownlang(Integer knownlang) {
        this.knownlang = knownlang;
    }

    public Integer getUserrole() {
        return userrole;
    }

    public void setUserrole(Integer userrole) {
        this.userrole = userrole;
    }

    public Integer getMindurationperday() {
        return mindurationperday;
    }

    public void setMindurationperday(Integer mindurationperday) {
        this.mindurationperday = mindurationperday;
    }

    public Integer getRegistrationtype() {
        return registrationtype;
    }

    public void setRegistrationtype(Integer registrationtype) {
        this.registrationtype = registrationtype;
    }


}
