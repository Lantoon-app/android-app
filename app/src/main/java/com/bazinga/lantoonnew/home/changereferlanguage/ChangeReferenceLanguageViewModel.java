package com.bazinga.lantoonnew.home.changereferlanguage;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bazinga.lantoonnew.registration.langselection.model.Language;
import com.bazinga.lantoonnew.retrofit.ApiClient;
import com.bazinga.lantoonnew.retrofit.ApiInterface;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeReferenceLanguageViewModel extends ViewModel {

    MutableLiveData<List<Language>> languageLiveData;

    public ChangeReferenceLanguageViewModel() {
        languageLiveData = new MutableLiveData<>();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Language>> call = apiInterface.getLanguages();
        call.enqueue(new Callback<List<Language>>() {
            @Override
            public void onResponse(Call<List<Language>> call, Response<List<Language>> response) {

                //System.out.println("language list success=  " + response.message().toString());
                if (response.isSuccessful()) {
                    languageLiveData.setValue(response.body());
                    Log.d("response body= ", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
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