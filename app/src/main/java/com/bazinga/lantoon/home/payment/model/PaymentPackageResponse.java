package com.bazinga.lantoon.home.payment.model;

import com.bazinga.lantoon.retrofit.Status;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentPackageResponse {
    @SerializedName("data")
    @Expose
    private List<PaymentPackage> paymentPackageList = null;
    @SerializedName("status")
    @Expose
    private Status status;

    public List<PaymentPackage> getPaymentPackageListData() {
        return paymentPackageList;
    }

    public void setPaymentPackageListData(List<PaymentPackage> paymentPackageList) {
        this.paymentPackageList = paymentPackageList;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
