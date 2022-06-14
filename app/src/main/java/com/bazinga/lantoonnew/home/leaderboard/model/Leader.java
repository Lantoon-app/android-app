package com.bazinga.lantoonnew.home.leaderboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Leader {
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("gemcount")
    @Expose
    private Integer gemcount;
    @SerializedName("rank")
    @Expose
    private Integer rank;
    @SerializedName("uname")
    @Expose
    private String uname;
    @SerializedName("picture")
    @Expose
    private String picture;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getGemcount() {
        return gemcount;
    }

    public void setGemcount(Integer gemcount) {
        this.gemcount = gemcount;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
