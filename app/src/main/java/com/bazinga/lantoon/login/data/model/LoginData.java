package com.bazinga.lantoon.login.data.model;

import com.bazinga.lantoon.home.mylanguage.model.KnownLanguage;
import com.bazinga.lantoon.home.mylanguage.model.LearnLanguage;
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
    @SerializedName("region_code")
    @Expose
    public String region_code;
    @SerializedName("learnlang")
    @Expose
    private Integer learnlangId;
    @SerializedName("learnlang_obj")
    @Expose
    private LearnLanguage learnlang_obj;

    @SerializedName("knownlang")
    @Expose
    private Integer knownlangId;
    @SerializedName("knownlang_obj")
    @Expose
    private KnownLanguage knownlang_obj;
    @SerializedName("userrole")
    @Expose
    private Integer userrole;
    @SerializedName("mindurationperday")
    @Expose
    private Integer mindurationperday;
    @SerializedName("registrationtype")
    @Expose
    private Integer registrationtype;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("speakcode")
    @Expose
    private String SpeakCode;

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
 public String getRegionCode() {
        return region_code;
    }

    public void setRegionCode(String region_code) {
        this.region_code = region_code;
    }

    public Integer getLearnlangId() {
        return learnlangId;
    }

    public void setLearnlangId(Integer learnlangId) {
        this.learnlangId = learnlangId;
    }

    public LearnLanguage getLearnlangObj() {
        return learnlang_obj;
    }

    public void setLearnlangObj(LearnLanguage learnlang_obj) {
        this.learnlang_obj = learnlang_obj;
    }

    public Integer getKnownlangId() {
        return knownlangId;
    }

    public void setKnownlangId(Integer knownlangId) {
        this.knownlangId = knownlangId;
    }

    public KnownLanguage getKnownlangObj() {
        return knownlang_obj;
    }

    public void setKnownlangObj(KnownLanguage knownlang_obj) {
        this.knownlang_obj = knownlang_obj;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSpeakCode() {
        return SpeakCode;
    }

    public void setSpeakCode(String SpeakCode) {
        this.SpeakCode = SpeakCode;
    }

}
