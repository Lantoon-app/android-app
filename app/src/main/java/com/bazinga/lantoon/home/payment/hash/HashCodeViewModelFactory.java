package com.bazinga.lantoon.home.payment.hash;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import org.jetbrains.annotations.NotNull;

public class HashCodeViewModelFactory implements ViewModelProvider.Factory {

    String hashData;

    public HashCodeViewModelFactory(String hashData) {
        this.hashData = hashData;
    }

    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        return (T) new HashCodeGenerateViewModel(hashData);
    }
}
