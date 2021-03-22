package com.bazinga.lantoon.home.chapter.lesson;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bazinga.lantoon.home.chapter.lesson.model.Question;
import com.bazinga.lantoon.registration.langselection.model.Language;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionsViewModel extends ViewModel {
    MutableLiveData<JsonObject> questionsLiveData;
    // List<Question> languageList;

    public QuestionsViewModel() {
        questionsLiveData = new MutableLiveData<>();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> call = apiInterface.getQuestions(1, 1, 1);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                //System.out.println("language list success=  " + response.message().toString());
                if (response.isSuccessful()) {

                   /* Gson gson = new GsonBuilder()
                            .setPrettyPrinting()
                            .create();

                    String jsonOutput = gson.toJson(someObject);*/
                    //Log.e("response body= ", new GsonBuilder().setPrettyPrinting().create().toJson(response.body().get(0)));

                    JsonObject jsonObject = new JsonObject();

                    jsonObject = response.body().get(0).getAsJsonObject();

                    questionsLiveData.setValue(jsonObject);


                    /*if (response.body() != null) {
                        for (int i = 0; i < jsonObject.size(); i++) {
                            //listdata.add(jArray.getString(i));
                            Log.d("jsonObject", String.valueOf(i));
                        }

                    }*/
                    //Log.e("response len= ", response.body().get(0).getAsString());
                } else {
                    //Log.e("response message= ", response.message() + response.code());
                }


            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

                call.cancel();
                t.printStackTrace();
                Log.e("response ERROR= ", "" + t.getMessage() + " " + t.getLocalizedMessage());
            }
        });
    }

    public LiveData<JsonObject> getQuestionsMutableLiveData() {
        return questionsLiveData;
    }
}
