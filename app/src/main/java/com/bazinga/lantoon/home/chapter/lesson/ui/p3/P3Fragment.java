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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bazinga.lantoon.Audio;
import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.Utils;
import com.bazinga.lantoon.home.chapter.lesson.QuestionsActivity;
import com.bazinga.lantoon.home.chapter.lesson.model.Question;
import com.bazinga.lantoon.home.chapter.lesson.ui.p1.P1ViewModel;
import com.bazinga.lantoon.home.chapter.lesson.ui.p2.P2Fragment;
import com.bazinga.lantoon.home.chapter.lesson.ui.p2.P2ViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class P3Fragment extends Fragment implements View.OnClickListener {

    private P3ViewModel mViewModel;
    CommonFunction cf;
    Audio audio;
    Question question;
    TextView tvQuestionNo, tvQuestionName, tvRecText;
    ImageButton imgBtnHome, imgBtnHelp,imgBtnNext;
    ImageView imbBtnQuestionImg;
    ProgressBar pbTop;
    Button btnAudio, btnAudioSlow, btnMic;


    public static P3Fragment newInstance(int questionNo, int totalQuestions, String data) {
        P3Fragment fragment = new P3Fragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Utils.TAG_QUESTION_NO, questionNo);
        bundle.putInt(Utils.TAG_QUESTIONS_TOTAL, totalQuestions);
        bundle.putString(Utils.TAG_QUESTION_TYPE, data);
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_question_type_p3, container, false);
        initializeView(view);
        return view;
    }

    private void initializeView(View view) {
        imgBtnHome = view.findViewById(R.id.imgBtnHome);
        imgBtnHelp = view.findViewById(R.id.imgBtnHelp);
        imgBtnNext = view.findViewById(R.id.imgBtnNext);
        pbTop = view.findViewById(R.id.pbTop);
        tvQuestionNo = view.findViewById(R.id.tvQuestionNo);
        tvQuestionName = view.findViewById(R.id.tvQuestionName);
        tvRecText = view.findViewById(R.id.tvRecText);
        imbBtnQuestionImg = view.findViewById(R.id.imbBtnQuestionImg);
        btnAudio = view.findViewById(R.id.btnAudio);
        btnAudioSlow = view.findViewById(R.id.btnAudioSlow);
        btnMic = view.findViewById(R.id.btnMic);
        tvRecText.setText("");
        imgBtnHome.setOnClickListener(this::onClick);
        imgBtnHelp.setOnClickListener(this::onClick);
        imgBtnNext.setOnClickListener(this::onClick);
        btnAudio.setOnClickListener(this::onClick);
        btnAudioSlow.setOnClickListener(this::onClick);
        btnMic.setOnClickListener(this::onClick);
        setClickableButton(false);
    }

    private void setClickableButton(boolean clickable) {
        btnAudio.setClickable(clickable);
        btnAudioSlow.setClickable(clickable);
        btnMic.setClickable(clickable);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(P3ViewModel.class);
        cf = new CommonFunction();
        audio = new Audio();
        // TODO: Use the ViewModel
        setTopBarState(getArguments().getInt(Utils.TAG_QUESTION_NO), getArguments().getInt(Utils.TAG_QUESTIONS_TOTAL));
        Gson g = new Gson();
        question = g.fromJson(getArguments().getString(Utils.TAG_QUESTION_TYPE), Question.class);
        tvQuestionName.setText(question.getWord());
        cf.setImage(getActivity(), Utils.FILE_DESTINATION_PATH + File.separator + question.getRightImagePath(), imbBtnQuestionImg);
        audio.playAudioFile(Utils.FILE_DESTINATION_PATH + File.separator + question.getAudioPath());
        cf.shakeAnimation(imbBtnQuestionImg, 1000);
        setClickableButton(true);

        Log.d("data p1 ", new GsonBuilder().setPrettyPrinting().create().toJson(question));
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void setTopBarState(int quesNo, int totalQues) {
        tvQuestionNo.setText(quesNo + "/" + totalQues);
        int percentage = cf.percent(quesNo, totalQues);
        Log.d("percentage", String.valueOf(percentage));
        pbTop.setProgress(cf.percent(quesNo, totalQues));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtnHome:
                break;
            case R.id.imgBtnHelp:
                break;
            case R.id.imgBtnNext:
                QuestionsActivity.mPager.setCurrentItem(QuestionsActivity.mPager.getCurrentItem()+1);
                break;
            case R.id.btnAudio:
                audio.playAudioFile(Utils.FILE_DESTINATION_PATH + File.separator + question.getAudioPath());
                break;
            case R.id.btnAudioSlow:
                audio.playAudioSlow(Utils.FILE_DESTINATION_PATH + File.separator + question.getAudioPath());
                break;
            case R.id.btnMic:
                cf.speechToText(getContext(),tvRecText,question.getWord());

                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

}