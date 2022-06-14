package com.bazinga.lantoonnew.home.payment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrasactionData {

    @SerializedName("transactin_id")
    @Expose
    private String transactinId;

    public String getTransactinId() {
        return transactinId;
    }

    public void setTransactinId(String transactinId) {
        this.transactinId = transactinId;
    }
}
