package com.bazinga.lantoonnew.home.chapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

public class ChapterViewModelFactory implements ViewModelProvider.Factory {
    int Learnlangid;
    String userid,deviceid;

    public ChapterViewModelFactory(int Learnlangid, String userid, String deviceid) {
        this.Learnlangid = Learnlangid;
        this.userid = userid;
        this.deviceid = deviceid;
    }

    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        return (T) new ChapterViewModel(Learnlangid,userid,deviceid);
    }
}
