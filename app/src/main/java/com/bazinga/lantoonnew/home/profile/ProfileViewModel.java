package com.bazinga.lantoonnew.home.profile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bazinga.lantoonnew.home.HomeActivity;
import com.bazinga.lantoonnew.retrofit.ApiClient;
import com.bazinga.lantoonnew.retrofit.ApiInterface;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<Profile> mUser;

    public ProfileViewModel() {
        mUser = new MutableLiveData<>();
        //mText.setValue("This is Profile fragment");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Profile> call = apiInterface.getProfile(HomeActivity.sessionManager.getUid());
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {

                mUser.setValue(response.body());
                Log.d("profile data ", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Log.e("profile data ", t.getMessage());
            }
        });
    }

    public void postProfileData(ProfileData profileData) {

        Log.d("update profile data ", new GsonBuilder().setPrettyPrinting().create().toJson(profileData));
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Profile> call = apiInterface.updateProfile(profileData);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                //if(response.body().getStatus().getCode() == 1025 )
                Log.d("update profile data ", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                mUser.setValue(response.body());
                HomeActivity.sessionManager.setUserName(response.body().getProfileData().getUname());
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Log.e("update profile data ", t.getMessage());
            }
        });
    }

    public LiveData<Profile> getUser() {

        return mUser;
    }
}