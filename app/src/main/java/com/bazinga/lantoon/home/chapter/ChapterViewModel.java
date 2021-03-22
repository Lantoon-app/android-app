package com.bazinga.lantoon.home.chapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bazinga.lantoon.home.chapter.model.Chapter;
import com.bazinga.lantoon.home.chapter.widget.HorizontalSwipeRefreshLayout;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterViewModel extends ViewModel {

    private MutableLiveData<List<Chapter>> chapterMutableLiveData;
    public static final int PAGE_START = 1;
    private int currentPageNo = PAGE_START;
    HorizontalSwipeRefreshLayout swipeRefreshLayout;

    public ChapterViewModel() {

        getData(currentPageNo,swipeRefreshLayout);
    }

    public void getData(int currentPageNo, HorizontalSwipeRefreshLayout swipeRefreshLayout) {
        System.out.println("Chapter Current page = " + String.valueOf(currentPageNo));
        chapterMutableLiveData = new MutableLiveData<>();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Chapter>> call = apiInterface.getChapter(1, currentPageNo);
        call.enqueue(new Callback<List<Chapter>>() {
            @Override
            public void onResponse(Call<List<Chapter>> call, Response<List<Chapter>> response) {

                System.out.println("Chapter list success=  " + response.message().toString());
                chapterMutableLiveData.setValue(response.body());

            }

            @Override
            public void onFailure(Call<List<Chapter>> call, Throwable t) {
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