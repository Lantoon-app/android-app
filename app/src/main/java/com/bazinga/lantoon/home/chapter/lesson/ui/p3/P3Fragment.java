package com.bazinga.lantoon.home.chapter.lesson.ui.p3;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.Utils;
import com.bazinga.lantoon.home.chapter.lesson.model.Question;
import com.bazinga.lantoon.home.chapter.lesson.ui.p2.P2Fragment;
import com.bazinga.lantoon.home.chapter.lesson.ui.p2.P2ViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

public class P3Fragment extends Fragment {

    private P3ViewModel mViewModel;
    Question question;
    TextView tvQuestionNo, tvQuestionName;

    public static P3Fragment newInstance(int questionNo,int totalQuestions, String data) {
        P3Fragment fragment = new P3Fragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Utils.TAG_QUESTION_NO,questionNo);
        bundle.putInt(Utils.TAG_QUESTIONS_TOTAL,totalQuestions);
        bundle.putString(Utils.TAG_QUESTION_TYPE, data);
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_question_type_p3, container, false);
        tvQuestionNo = view.findViewById(R.id.tvQuestionNo);
        tvQuestionName = view.findViewById(R.id.tvQuestionName);
        tvQuestionNo.setText(String.valueOf(getArguments().getInt(Utils.TAG_QUESTION_NO))+"/"+String.valueOf(getArguments().getInt(Utils.TAG_QUESTIONS_TOTAL)));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(P3ViewModel.class);
        // TODO: Use the ViewModel
        Gson g= new Gson();
        question = g.fromJson(getArguments().getString(Utils.TAG_QUESTION_TYPE),Question.class);
        tvQuestionName.setText(question.getWord());
        Log.d("data p3 " ,new GsonBuilder().setPrettyPrinting().create().toJson(question));
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}