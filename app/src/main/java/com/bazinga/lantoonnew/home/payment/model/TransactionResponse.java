package com.bazinga.lantoonnew.home.payment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionResponse {

    @SerializedName("data")
    @Expose
    private TrasactionData trasactionData;

    public TrasactionData getTrasactionData() {
        return trasactionData;
    }

    public void setData(TrasactionData trasactionData) {
        this.trasactionData = trasactionData;
    }

}
