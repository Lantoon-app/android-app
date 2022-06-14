package com.bazinga.lantoonnew.home.leaderboard;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import org.jetbrains.annotations.NotNull;

public class LeaderViewModelProvider implements ViewModelProvider.Factory {

    String userid;
    int langId;

    public LeaderViewModelProvider(String userid,int langId) {

        this.userid = userid;
        this.langId = langId;
    }

    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        return (T) new LeaderViewModel(userid,langId);
    }
}
