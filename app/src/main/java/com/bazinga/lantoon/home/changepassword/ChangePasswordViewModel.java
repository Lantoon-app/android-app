package com.bazinga.lantoon.home.changepassword;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bazinga.lantoon.home.changepassword.model.ChangePasswordResponse;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.bazinga.lantoon.retrofit.Status;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordViewModel extends ViewModel {

    private MutableLiveData<Status> mutableLiveData;

    public ChangePasswordViewModel(String userid, String newPass, String oldPass) {

       update(userid,newPass,oldPass);
    }

    public void update(String userid, String newPass, String oldPass) {
        System.out.println("newpass "+newPass + "oldpass "+oldPass);
        mutableLiveData = new MutableLiveData<>();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ChangePasswordResponse> call = apiInterface.changePassword(userid, newPass, oldPass);
        call.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                //Log.d("Status ", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));

                if (response.body().getStatus() != null)
                    mutableLiveData.setValue(response.body().getStatus());
                else failure("Something went wrong");
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                Log.d("Status ", t.getMessage());

                failure(t.getMessage());

            }
        });

    }

    void failure(String something_went_wrong) {
        Status status = new Status();
        status.setMessage(something_went_wrong);
        mutableLiveData.setValue(status);
    }

    public LiveData<Status> getResult() {
        return mutableLiveData;
    }
}