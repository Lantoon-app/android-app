package com.bazinga.lantoon.home.changereferlanguage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChangeReferenceLanguageViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ChangeReferenceLanguageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Setting fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}