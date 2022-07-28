package com.bazinga.lantoonnew.home.chapter;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bazinga.lantoonnew.BuildConfig;
import com.bazinga.lantoonnew.home.chapter.model.ChapterResponse;
import com.bazinga.lantoonnew.retrofit.ApiClient;
import com.bazinga.lantoonnew.retrofit.ApiInterface;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterViewModel extends ViewModel {

    private MutableLiveData<ChapterResponse> chapterMutableLiveData;
    public static final int PAGE_START = 1;
    private int currentPageNo = PAGE_START;

    public ChapterViewModel(int LearnLangId, String userid, String deviceid) {

        getData(currentPageNo, LearnLangId, userid, deviceid);
    }

    public void getData(int currentPageNo, int LearnLangId, String userid, String deviceid) {
        Log.d("Chapter", currentPageNo + "learnid" + LearnLangId + "user" + userid);

        chapterMutableLiveData = new MutableLiveData<>();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ChapterResponse> call = null;

        /*if (ApiClient.isTest)
            call = apiInterface.getChapterV2(1, 1, "RET2021983451", 17, "789542632FDaa");
        else*/
            call = apiInterface.getChapterV2(LearnLangId, currentPageNo, userid, BuildConfig.VERSION_CODE, deviceid);
       /* if (ApiClient.isTest)
            //with logout concept
            call = apiInterface.getChapter(LearnLangId, currentPageNo, userid, BuildConfig.VERSION_CODE, deviceid);
        else
            call = apiInterface.getChapter(LearnLangId, currentPageNo, userid, BuildConfig.VERSION_CODE);
*/
        call.enqueue(new Callback<ChapterResponse>() {
            @Override
            public void onResponse(Call<ChapterResponse> call, Response<ChapterResponse> response) {
                if (response.isSuccessful()) {
                    //if (response.body().getStatus().getCode() == 1032) {
                    Log.d("Chapter list success", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                    chapterMutableLiveData.setValue(response.body());
                    //}
                } else
                    chapterMutableLiveData.setValue(null);

            }

            @Override
            public void onFailure(Call<ChapterResponse> call, Throwable t) {
                System.out.println("Chapter list error=  " + t.toString());
                chapterMutableLiveData.setValue(null);
            }
        });
    }

    public LiveData<ChapterResponse> getChapters() {

        return chapterMutableLiveData;
    }

}