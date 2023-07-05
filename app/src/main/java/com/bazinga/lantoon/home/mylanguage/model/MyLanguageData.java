package com.bazinga.lantoon.home.mylanguage.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyLanguageData {
    @SerializedName("slid")
    @Expose
    private String slid;
    @SerializedName("Uid")
    @Expose
    private String uid;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("GroupCode")
    @Expose
    private String groupCode;
    @SerializedName("Validupto")
    @Expose
    private String validupto;
    @SerializedName("LearnLanguage")
    @Expose
    private LearnLanguage learnLanguage = null;
    @SerializedName("KnownLanguage")
    @Expose
    private KnownLanguage knownLanguage = null;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;
    @SerializedName("ModifiedOn")
    @Expose
    private String modifiedOn;
    @SerializedName("DeactivatedOn")
    @Expose
    private String deactivatedOn;
    @SerializedName("PaymentStatus")
    @Expose
    private String paymentStatus;
    @SerializedName("MinDurationPerDay")
    @Expose
    private String minDurationPerDay;

    public String getSlid() {
        return slid;
    }

    public void setSlid(String slid) {
        this.slid = slid;
    }

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

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getValidupto() {
        return validupto;
    }

    public void setValidupto(String validupto) {
        this.validupto = validupto;
    }

    public LearnLanguage getLearnLanguage() {
        return learnLanguage;
    }

    public void setLearnLanguage(LearnLanguage learnLanguage) {
        this.learnLanguage = learnLanguage;
    }

    public KnownLanguage getKnownLanguage() {
        return knownLanguage;
    }

    public void setKnownLanguage(KnownLanguage knownLanguage) {
        this.knownLanguage = knownLanguage;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getDeactivatedOn() {
        return deactivatedOn;
    }

    public void setDeactivatedOn(String deactivatedOn) {
        this.deactivatedOn = deactivatedOn;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getMinDurationPerDay() {
        return minDurationPerDay;
    }

    public void setMinDurationPerDay(String minDurationPerDay) {
        this.minDurationPerDay = minDurationPerDay;
    }

}
