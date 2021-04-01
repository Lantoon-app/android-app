package com.bazinga.lantoon.home.target;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TargetViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TargetViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Target fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}