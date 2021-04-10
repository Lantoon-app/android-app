package com.bazinga.lantoon.home.chapter;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bazinga.lantoon.home.chapter.model.Chapter;
import com.bazinga.lantoon.home.chapter.model.ChapterResponse;
import com.bazinga.lantoon.home.chapter.widget.HorizontalSwipeRefreshLayout;
import com.bazinga.lantoon.login.SessionManager;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterViewModel extends ViewModel {

    private MutableLiveData<List<Chapter>> chapterMutableLiveData;
    public static final int PAGE_START = 1;
    private int currentPageNo = PAGE_START;
    HorizontalSwipeRefreshLayout swipeRefreshLayout;


    public ChapterViewModel(int LearnLangId, String userid) {

        getData(currentPageNo,swipeRefreshLayout,LearnLangId,userid);
    }

    public void getData(int currentPageNo, HorizontalSwipeRefreshLayout swipeRefreshLayout, int LearnLangId,String userid) {
        System.out.println("Chapter Current page = " + String.valueOf(currentPageNo));
        chapterMutableLiveData = new MutableLiveData<>();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ChapterResponse> call = apiInterface.getChapter(LearnLangId, currentPageNo,userid);
        call.enqueue(new Callback<ChapterResponse>() {
            @Override
            public void onResponse(Call<ChapterResponse> call, Response<ChapterResponse> response) {

                Log.d("Chapter list success",new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                chapterMutableLiveData.setValue(response.body().getData());

            }

            @Override
            public void onFailure(Call<ChapterResponse> call, Throwable t) {
                System.out.println("Chapter list error=  " + t.toString());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public LiveData<List<Chapter>> getChapters() {
        System.out.println("Chapter Current pageLiveData = " + String.valueOf(currentPageNo));
        return chapterMutableLiveData;
    }

}