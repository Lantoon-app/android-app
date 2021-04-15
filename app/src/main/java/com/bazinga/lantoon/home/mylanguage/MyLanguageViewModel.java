package com.bazinga.lantoon.home.mylanguage;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bazinga.lantoon.home.HomeActivity;
import com.bazinga.lantoon.home.mylanguage.model.MyLanguageData;
import com.bazinga.lantoon.home.mylanguage.model.MyLanguageResponse;
import com.bazinga.lantoon.login.SessionManager;
import com.bazinga.lantoon.registration.langselection.model.Language;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyLanguageViewModel extends ViewModel {

    MutableLiveData<List<MyLanguageData>> languageLiveData;
    MyLanguageResponse languageList;

    public MyLanguageViewModel() {

        languageLiveData = new MutableLiveData<>();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<MyLanguageResponse> call = apiInterface.getMyLanguage(HomeActivity.sessionManager.getUserDetails().getUid());
        call.enqueue(new Callback<MyLanguageResponse>() {
            @Override
            public void onResponse(Call<MyLanguageResponse> call, Response<MyLanguageResponse> response) {

                //System.out.println("language list success=  " + response.message().toString());
                if (response.isSuccessful()) {
                    if (response.body().getStatus().getCode() == 1031)
                        languageLiveData.setValue(response.body().getData());
                    Log.e("response body= ", new Gson().toJson(response.body()));
                } else {
                    Log.e("response message= ", response.message() + response.code());
                }


            }

            @Override
            public void onFailure(Call<MyLanguageResponse> call, Throwable t) {

                call.cancel();
                t.printStackTrace();
                Log.e("response ERROR= ", "" + t.getMessage() + " " + t.getLocalizedMessage());
            }
        });
    }

    public LiveData<List<MyLanguageData>> getLanguageMutableLiveData() {
        return languageLiveData;
    }
}