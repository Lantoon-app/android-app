package com.bazinga.lantoonnew.login.forget;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OTP {
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("otp")
    @Expose
    private Integer otp;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }
}
