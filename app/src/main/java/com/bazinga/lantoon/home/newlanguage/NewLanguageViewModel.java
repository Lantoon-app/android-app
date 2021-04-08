package com.bazinga.lantoon.home.newlanguage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewLanguageViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NewLanguageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is new_language fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}