package com.bazinga.lantoon.home.payment.hash;

import android.util.Log;

import com.bazinga.lantoon.home.payment.hash.HashCodeResponse;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HashGeneration {
    public static String hashCode = "";

    public static String getHashCodeFromServer(String hashData) {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<HashCodeResponse> call = apiInterface.getPaymentHashCode(hashData);
        call.enqueue(new Callback<HashCodeResponse>() {
            @Override
            public void onResponse(Call<HashCodeResponse> call, Response<HashCodeResponse> response) {
                if (response.isSuccessful()) {
                    hashCode = response.body().getHashCode().getHashConverted();
                    Log.d("response body= ", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                } else {
                    Log.e("response message= ", response.message() + response.code());
                }
            }

            @Override
            public void onFailure(Call<HashCodeResponse> call, Throwable t) {
                call.cancel();
                t.printStackTrace();
                Log.e("response ERROR= ", "" + t.getMessage() + " " + t.getLocalizedMessage());
            }
        });
        return hashCode;
    }
}
