package com.bazinga.lantoon.home.changepassword;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bazinga.lantoon.home.leader.LeaderViewModel;

import org.jetbrains.annotations.NotNull;

public class ChangePasswordViewModelProvider implements ViewModelProvider.Factory {

    String userid,newPass,oldPass;


    public ChangePasswordViewModelProvider(String userid,  String newPass,String oldPass) {

        this.userid = userid;
        this.newPass = newPass;
        this.oldPass = oldPass;
    }

    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        return (T) new ChangePasswordViewModel(userid,newPass,oldPass);
    }
}
