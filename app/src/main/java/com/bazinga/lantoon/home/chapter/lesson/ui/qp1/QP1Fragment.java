package com.bazinga.lantoon.home.chapter.lesson.ui.qp1;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bazinga.lantoon.R;

public class QP1Fragment extends Fragment {

    private QP1ViewModel mViewModel;

    public static QP1Fragment newInstance() {
        return new QP1Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_question_type_qp1, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(QP1ViewModel.class);
        // TODO: Use the ViewModel
    }

}