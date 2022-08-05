package com.bazinga.lantoon.home.target.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Target {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("region_code")
    @Expose
    private String regionCode;
    @SerializedName("institute_code")
    @Expose
    private String instituteCode;
    @SerializedName("group_code")
    @Expose
    private String groupCode;
    @SerializedName("from_date")
    @Expose
    private String fromDate;
    @SerializedName("to_date")
    @Expose
    private String toDate;
    @SerializedName("display_text")
    @Expose
    private String displayText;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("modified_by")
    @Expose
    private String modifiedBy;
    @SerializedName("modified_on")
    @Expose
    private String modifiedOn;
    @SerializedName("targetstatus")
    @Expose
    private Integer targetstatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getInstituteCode() {
        return instituteCode;
    }

    public void setInstituteCode(String instituteCode) {
        this.instituteCode = instituteCode;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Integer getTargetstatus() {
        return targetstatus;
    }

    public void setTargetstatus(Integer targetstatus) {
        this.targetstatus = targetstatus;
    }
}
