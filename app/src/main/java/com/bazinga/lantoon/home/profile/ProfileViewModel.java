package com.bazinga.lantoon.home.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bazinga.lantoon.login.data.model.LoggedInUser;
import com.bazinga.lantoon.registration.model.User;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<LoggedInUser> mUser;

    public ProfileViewModel() {
        mUser = new MutableLiveData<>();
        //mText.setValue("This is Profile fragment");
    }

    public LiveData<LoggedInUser> getUser() {
        return mUser;
    }
}