package com.bazinga.lantoon.home.chapter.lesson.ui.qp3;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bazinga.lantoon.R;

public class QP3Fragment extends Fragment {

    private QP3ViewModel mViewModel;

    public static QP3Fragment newInstance() {
        return new QP3Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_question_type_qp3, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(QP3ViewModel.class);
        // TODO: Use the ViewModel
    }

}