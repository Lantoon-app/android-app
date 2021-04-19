package com.bazinga.lantoon.home.target;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bazinga.lantoon.home.target.model.Target;
import com.bazinga.lantoon.home.target.model.TargetResponse;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TargetViewModel extends ViewModel {

    private MutableLiveData<List<Target>> listMutableLiveData;


    public LiveData<List<Target>> getTargets(String userId) {
        listMutableLiveData = new MutableLiveData<>();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<TargetResponse> call = apiInterface.getTargets(userId);
        call.enqueue(new Callback<TargetResponse>() {
            @Override
            public void onResponse(Call<TargetResponse> call, Response<TargetResponse> response) {

                if (response.isSuccessful()) {
                    Log.d("response targets", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                    if (response.body().getStatus().getCode() == 1041)
                        listMutableLiveData.setValue(response.body().getTargetListData());
                    else listMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<TargetResponse> call, Throwable t) {
                listMutableLiveData.setValue(null);
            }
        });
        return listMutableLiveData;
    }
}