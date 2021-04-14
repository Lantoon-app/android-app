package com.bazinga.lantoon.home.leader;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bazinga.lantoon.home.leader.model.LeaderResponse;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderViewModel extends ViewModel {

    private MutableLiveData<LeaderResponse> leaderResponseMutableLiveData;
    public static final int PAGE_START = 1;
    private int currentPageNo = PAGE_START;

    public LeaderViewModel(String userid,int langId) {
        getData(PAGE_START, userid,langId);
    }

    public void getData(int slideNo, String userid, int langId) {

        leaderResponseMutableLiveData = new MutableLiveData<>();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LeaderResponse> call = apiInterface.getLeaders(slideNo,userid,langId);
        call.enqueue(new Callback<LeaderResponse>() {
            @Override
            public void onResponse(Call<LeaderResponse> call, Response<LeaderResponse> response) {
                Log.d("LeaderResponse success", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                if (response.body().getStatus().getCode() == 1033) {
                    Log.d("LeaderResponse success", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                    leaderResponseMutableLiveData.setValue(response.body());
                } else
                    leaderResponseMutableLiveData.setValue(null);

            }

            @Override
            public void onFailure(Call<LeaderResponse> call, Throwable t) {
                System.out.println("LeaderResponse list error=  " + t.toString());
                leaderResponseMutableLiveData.setValue(null);
            }
        });
    }



    public LiveData<LeaderResponse> getLeaders() {

        return leaderResponseMutableLiveData;
    }
}