package com.bazinga.lantoonnew.home.payment;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

public class PaymentViewModelFactory implements ViewModelProvider.Factory {

    String userid;

    public PaymentViewModelFactory(String userid) {
        this.userid = userid;
    }

    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        return (T) new PaymentViewModel(userid);
    }
}
