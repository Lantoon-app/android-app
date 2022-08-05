package com.bazinga.lantoon.home.payment.hash;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HashCode {
    @SerializedName("hash_converted")
    @Expose
    private String hashConverted;

    public String getHashConverted() {
        return hashConverted;
    }

    public void setHashConverted(String hashConverted) {
        this.hashConverted = hashConverted;
    }
}
