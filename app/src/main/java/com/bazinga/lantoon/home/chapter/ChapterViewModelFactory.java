package com.bazinga.lantoon.home.chapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

public class ChapterViewModelFactory implements ViewModelProvider.Factory {
    int Learnlangid;
    String userid;

    public ChapterViewModelFactory(int Learnlangid, String userid) {
        this.Learnlangid = Learnlangid;
        this.userid = userid;
    }

    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        return (T) new ChapterViewModel(Learnlangid,userid);
    }
}
