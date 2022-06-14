package com.bazinga.lantoonnew.login.forget;

import com.bazinga.lantoonnew.retrofit.Status;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtpResponse {
    @SerializedName("data")
    @Expose
    private OTP otp;
    @SerializedName("status")
    @Expose
    private Status status;

    public OTP getOTPdata() {
        return otp;
    }

    public void setOTPData(OTP otp) {
        this.otp = otp;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
