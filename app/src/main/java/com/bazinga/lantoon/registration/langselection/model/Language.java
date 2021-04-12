package com.bazinga.lantoon.registration.langselection.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Language {
    @SerializedName("LanguageID")
    @Expose
    private String languageID;
    @SerializedName("LanguageName")
    @Expose
    private String languageName;
    @SerializedName("NativeName")
    @Expose
    private String nativeName;
    @SerializedName("ImagePath")
    @Expose
    private String imagePath;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;
    @SerializedName("CreatedBy")
    @Expose
    private String createdBy;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("SpeakCode")
    @Expose
    private String SpeakCode;

    public String getLanguageID() {
        return languageID;
    }

    public void setLanguageID(String languageID) {
        this.languageID = languageID;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpeakCode() {
        return SpeakCode;
    }

    public void setSpeakCode(String SpeakCode) {
        this.SpeakCode = SpeakCode;
    }

}
