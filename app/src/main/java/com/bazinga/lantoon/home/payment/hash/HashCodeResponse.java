package com.bazinga.lantoon.home.payment.hash;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HashCodeResponse {
    @SerializedName("data")
    @Expose
    private HashCode hashCode;

    public HashCode getHashCode() {
        return hashCode;
    }

    public void setHashCode(HashCode hashCode) {
        this.hashCode = hashCode;
    }
}
