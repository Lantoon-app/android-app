package com.bazinga.lantoon.home.mylanguage;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bazinga.lantoon.registration.langselection.model.Language;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyLanguageViewModel extends ViewModel {

    MutableLiveData<List<Language>> languageLiveData;
    List<Language> languageList;

    public MyLanguageViewModel() {
        languageLiveData = new MutableLiveData<>();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Language>> call = apiInterface.getLanguages();
        call.enqueue(new Callback<List<Language>>() {
            @Override
            public void onResponse(Call<List<Language>> call, Response<List<Language>> response) {

                //System.out.println("language list success=  " + response.message().toString());
                if (response.isSuccessful()) {
                    languageLiveData.setValue(response.body());
                    Log.e("response body= ", new Gson().toJson(response.body()));
                } else {
                    Log.e("response message= ", response.message() + response.code());
                }


            }

            @Override
            public void onFailure(Call<List<Language>> call, Throwable t) {

                call.cancel();
                t.printStackTrace();
                Log.e("response ERROR= ", "" + t.getMessage() + " " + t.getLocalizedMessage());
            }
        });
    }

    public LiveData<List<Language>> getLanguageMutableLiveData() {
        return languageLiveData;
    }
}