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
import com.bazinga.lantoon.Tags;
import com.bazinga.lantoon.home.chapter.lesson.QuestionsActivity;
import com.bazinga.lantoon.home.chapter.lesson.ReferencePopup;
import com.bazinga.lantoon.home.chapter.lesson.model.Question;
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
    ImageButton imgBtnHome, imgBtnHelp;
    ImageView imbBtnQuestionImg;
    ProgressBar pbTop;
    Button btnAudio, btnAudioSlow, btnMic;
    ReferencePopup referencePopup;
    int quesNo, totalQues;

    public static P3Fragment newInstance(int questionNo, int totalQuestions, String data) {
        P3Fragment fragment = new P3Fragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Tags.TAG_QUESTION_NO, questionNo);
        bundle.putInt(Tags.TAG_QUESTIONS_TOTAL, totalQuestions);
        bundle.putString(Tags.TAG_QUESTION_TYPE, data);
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
        btnAudio.setOnClickListener(this::onClick);
        btnAudioSlow.setOnClickListener(this::onClick);
        btnMic.setOnClickListener(this::onClick);
        setClickableButton(false);
    }

    private void setClickableButton(boolean clickable) {
        imgBtnHelp.setClickable(clickable);
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
        quesNo = getArguments().getInt(Tags.TAG_QUESTION_NO);
        totalQues = getArguments().getInt(Tags.TAG_QUESTIONS_TOTAL);
        setTopBarState(quesNo, totalQues);
        Gson g = new Gson();
        question = g.fromJson(getArguments().getString(Tags.TAG_QUESTION_TYPE), Question.class);
        if (question.getReference() == null)
            imgBtnHelp.setVisibility(View.INVISIBLE);
        else
            referencePopup = new ReferencePopup(question.getReference());
        tvQuestionName.setText(question.getWord());
        cf.setImage(getActivity(), QuestionsActivity.strFilePath + File.separator + question.getRightImagePath(), imbBtnQuestionImg);
        //audio.playAudioFile(QuestionsActivity.strFilePath + File.separator + question.getAudioPath());

        setClickableButton(true);

        Log.d("data p1 ", new GsonBuilder().setPrettyPrinting().create().toJson(question));
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        cf.shakeAnimation(imbBtnQuestionImg, 1000);
        audio.playAudioFile(QuestionsActivity.strFilePath + File.separator + question.getAudioPath());
        cf.mikeAnimation(btnMic, 2000);
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
                cf.onClickHomeButton(getView(), getActivity(), getArguments().getInt(Tags.TAG_QUESTION_NO));
                break;
            case R.id.imgBtnHelp:
                if (question.getReference() != null) {
                    referencePopup.showPopupWindow(getView());
                }
                break;
            case R.id.btnAudio:
                audio.playAudioFile(QuestionsActivity.strFilePath + File.separator + question.getAudioPath());
                break;
            case R.id.btnAudioSlow:
                audio.playAudioSlow(getActivity(), QuestionsActivity.strFilePath + File.separator + question.getAudioPath());
                break;
            case R.id.btnMic:
                //String withoutSplChar = "t.e?s!t. le.tt!er ";
                String ansWrd =question.getAnsWord().replaceAll("[^ .,a-zA-Z0-9]", "").replace(".","").trim();

                System.out.println("withoutSplChar "+ansWrd);
                if (quesNo == totalQues)
                    cf.speechToText(getContext(), tvRecText, ansWrd, true, getView(), getActivity(), quesNo, question.getPlusMark(), question.getMinusMark());
                else
                    cf.speechToText(getContext(), tvRecText, ansWrd, false, getView(), getActivity(), quesNo, question.getPlusMark(), question.getMinusMark());

                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

}