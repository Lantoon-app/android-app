package com.bazinga.lantoon.home.leader;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import org.jetbrains.annotations.NotNull;

public class LeaderViewModelProvider implements ViewModelProvider.Factory {

    String userid;

    public LeaderViewModelProvider(String userid) {

        this.userid = userid;
    }

    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        return (T) new LeaderViewModel(userid);
    }
}
