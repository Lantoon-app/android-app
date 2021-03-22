package com.bazinga.lantoon.home.leader;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LeaderViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LeaderViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Leader fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}