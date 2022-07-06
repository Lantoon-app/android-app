package com.bazinga.lantoonnew.home.chapter.lesson.ui.d2;

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

import com.bazinga.lantoonnew.Audio;
import com.bazinga.lantoonnew.CommonFunction;
import com.bazinga.lantoonnew.R;
import com.bazinga.lantoonnew.Tags;
import com.bazinga.lantoonnew.home.chapter.lesson.QuestionsActivity;
import com.bazinga.lantoonnew.home.chapter.lesson.ReferencePopup;
import com.bazinga.lantoonnew.home.chapter.lesson.model.Question;
import com.bazinga.lantoonnew.home.chapter.lesson.ui.p1.P1Fragment;
import com.bazinga.lantoonnew.home.chapter.lesson.ui.p1.P1ViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

public class D2Fragment extends Fragment implements View.OnClickListener {

    private D2ViewModel mViewModel;
    CommonFunction cf;
    Audio audio;
    Question question;
    ReferencePopup referencePopup;
    int quesNo, totalQues;
    TextView tvQuestionNo, tvFibQuestion;
    ImageButton imgBtnHome, imgBtnHelp;
    ImageView imbBtnQuestionImg;
    ProgressBar pbTop;
    Button btnAudio, btnAudioSlow;

    public static D2Fragment newInstance(int questionNo, int totalQuestions, String data) {
        D2Fragment fragment = new D2Fragment();
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
        View view = inflater.inflate(R.layout.layout_question_type_d2, container, false);
        initializeView(view);
        return view;
    }

    private void initializeView(View view) {
        imgBtnHome = view.findViewById(R.id.imgBtnHome);
        imgBtnHelp = view.findViewById(R.id.imgBtnHelp);
        pbTop = view.findViewById(R.id.pbTop);
        tvQuestionNo = view.findViewById(R.id.tvQuestionNo);
        tvFibQuestion = view.findViewById(R.id.tvFibQuestion);
        imbBtnQuestionImg = view.findViewById(R.id.imbBtnQuestionImg);
        btnAudio = view.findViewById(R.id.btnAudio);
        btnAudioSlow = view.findViewById(R.id.btnAudioSlow);

        imgBtnHome.setOnClickListener(this::onClick);
        btnAudio.setOnClickListener(this::onClick);
        btnAudioSlow.setOnClickListener(this::onClick);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(D2ViewModel.class);
        cf = new CommonFunction();
        audio = new Audio();
        quesNo = getArguments().getInt(Tags.TAG_QUESTION_NO);
        totalQues = getArguments().getInt(Tags.TAG_QUESTIONS_TOTAL);
        setTopBarState(quesNo, totalQues);
        Gson g = new Gson();
        question = g.fromJson(getArguments().getString(Tags.TAG_QUESTION_TYPE), Question.class);
        if (question.getReference() == null)
            imgBtnHelp.setVisibility(View.INVISIBLE);
        else
            referencePopup = new ReferencePopup( question.getReference());
        tvFibQuestion.setText(question.getFibQues());
        cf.setImage(getActivity(), QuestionsActivity.strFilePath + question.getRightImagePath(), imbBtnQuestionImg);
        Log.d("data d2 ", new GsonBuilder().setPrettyPrinting().create().toJson(question));
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        audio.playAudioFileAnim(getActivity(), QuestionsActivity.strFilePath + question.getAudioPath(), null);
    }

    private void setTopBarState(int quesNo, int totalQues) {
        tvQuestionNo.setText(quesNo + "/" + totalQues);
        int percentage = cf.percent(quesNo, totalQues);
        Log.d("percentage", String.valueOf(percentage));
        pbTop.setProgress(cf.percent(quesNo, totalQues));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBtnHome:
                cf.onClickHomeButton(getView(),getActivity(),getArguments().getInt(Tags.TAG_QUESTION_NO));
                break;
            case R.id.imgBtnHelp:
                if(question.getReference() != null) {
                    referencePopup.showPopupWindow(getView());
                }
                break;
            case R.id.btnAudio:
                audio.playAudioFileAnim(getActivity(), QuestionsActivity.strFilePath + question.getAudioPath(), null);
                break;
            case R.id.btnAudioSlow:
                audio.playAudioSlow(getActivity(), QuestionsActivity.strFilePath + question.getAudioPath());
                break;
        }
    }
}