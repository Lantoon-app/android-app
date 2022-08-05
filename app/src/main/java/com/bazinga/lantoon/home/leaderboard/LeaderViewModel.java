package com.bazinga.lantoon.home.leaderboard;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bazinga.lantoon.home.leaderboard.model.LeaderResponse;
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


    public void getLeaderData(int type, int slideNo, String userid, int langId) {
        Call<LeaderResponse> call = null;
        leaderResponseMutableLiveData = new MutableLiveData<>();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        if (type == 0)
            call = apiInterface.getLeadersGlobal(slideNo, userid, langId);
        else if (type == 1)
            call = apiInterface.getLeadersInstitutional(slideNo, userid, langId);
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