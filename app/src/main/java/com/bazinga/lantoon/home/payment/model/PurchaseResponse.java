package com.bazinga.lantoon.home.payment.model;

import com.bazinga.lantoon.retrofit.Status;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PurchaseResponse {
    @SerializedName("status")
    @Expose
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
