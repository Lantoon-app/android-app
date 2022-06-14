package com.bazinga.lantoonnew.home.payment;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bazinga.lantoonnew.home.payment.model.PaymentPackage;
import com.bazinga.lantoonnew.home.payment.model.PaymentPackageResponse;
import com.bazinga.lantoonnew.retrofit.ApiClient;
import com.bazinga.lantoonnew.retrofit.ApiInterface;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentViewModel extends ViewModel {

    MutableLiveData<List<PaymentPackage>> paymentPackageLiveData;

    public PaymentViewModel(String userid) {
        paymentPackageLiveData = new MutableLiveData<>();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<PaymentPackageResponse> call = apiInterface.getPaymentPackages(userid);
        call.enqueue(new Callback<PaymentPackageResponse>() {
            @Override
            public void onResponse(Call<PaymentPackageResponse> call, Response<PaymentPackageResponse> response) {

                //System.out.println("language list success=  " + response.message().toString());
                if (response.isSuccessful()) {
                    paymentPackageLiveData.setValue(response.body().getPaymentPackageListData());
                    Log.d("response body= ", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                } else {
                    Log.e("response message= ", response.message() + response.code());
                }


            }

            @Override
            public void onFailure(Call<PaymentPackageResponse> call, Throwable t) {

                call.cancel();
                t.printStackTrace();
                Log.e("response ERROR= ", "" + t.getMessage() + " " + t.getLocalizedMessage());
            }
        });
    }

    public LiveData<List<PaymentPackage>> getPaymentPackageMutableLiveData() {
        return paymentPackageLiveData;
    }
}