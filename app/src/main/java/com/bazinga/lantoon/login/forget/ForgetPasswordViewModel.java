package com.bazinga.lantoon.login.forget;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bazinga.lantoon.home.HomeActivity;
import com.bazinga.lantoon.home.mylanguage.model.MyLanguageData;
import com.bazinga.lantoon.home.mylanguage.model.MyLanguageResponse;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.bazinga.lantoon.retrofit.Status;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordViewModel extends ViewModel {
    MutableLiveData<OtpResponse> otpLiveData;
    MutableLiveData<Status> statusMutableLiveData;
    MutableLiveData<Boolean> validateOtpLiveData;


    public LiveData<OtpResponse> getOtpLiveData(String email) {
        otpLiveData = new MutableLiveData<>();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<OtpResponse> call = apiInterface.getOtp(email);
        call.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {

                //System.out.println("language list success=  " + response.message().toString());
                if (response.isSuccessful()) {
                    // if (response.body().getStatus().getCode() == 1045)
                    otpLiveData.setValue(response.body());
                    Log.d("response body= ", new Gson().toJson(response.body()));
                } else {
                    otpLiveData.setValue(null);
                    Log.d("response message= ", response.message() + response.code());
                }


            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                otpLiveData.setValue(null);
                call.cancel();
                t.printStackTrace();
                Log.e("response ERROR= ", "" + t.getMessage() + " " + t.getLocalizedMessage());
            }
        });
        return otpLiveData;
    }

    public LiveData<Boolean> validateOTP(int otpFromServer, int otp){
        validateOtpLiveData = new MutableLiveData<>();
        if(otpFromServer == otp)
            validateOtpLiveData.setValue(true);
        else validateOtpLiveData.setValue(false);
        return validateOtpLiveData;
    }
    public LiveData<Status> changeForgetPassword(String uid, String newPass){
        statusMutableLiveData = new MutableLiveData<>();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiInterface.changeForgetPassword(uid,newPass);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                //System.out.println("language list success=  " + response.message().toString());
                if (response.isSuccessful()) {
                    // if (response.body().getStatus().getCode() == 1045)
                    Status status = new Gson().fromJson(response.body().get("status").getAsJsonObject(),Status.class);
                    statusMutableLiveData.setValue(status);
                    Log.d("response body= ", new Gson().toJson(response.body()));
                } else {
                    statusMutableLiveData.setValue(null);
                    Log.d("response message= ", response.message() + response.code());
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                statusMutableLiveData.setValue(null);
                call.cancel();
                t.printStackTrace();
                Log.e("response ERROR= ", "" + t.getMessage() + " " + t.getLocalizedMessage());
            }
        });

        return statusMutableLiveData;
    }
}
