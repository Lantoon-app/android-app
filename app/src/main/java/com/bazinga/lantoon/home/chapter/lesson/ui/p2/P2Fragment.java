package com.bazinga.lantoon.home.chapter.lesson.ui.p2;

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
import com.bazinga.lantoon.home.chapter.lesson.HelpPopup;
import com.bazinga.lantoon.home.chapter.lesson.QuestionsActivity;
import com.bazinga.lantoon.home.chapter.lesson.model.Question;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.File;


public class P2Fragment extends Fragment implements View.OnClickListener {

    private P2ViewModel mViewModel;
    CommonFunction cf;
    Audio audio;
    Question question;
    TextView tvQuestionNo;
    ImageButton imgBtnHome, imgBtnHelp;
    ImageView imgBtnAnsImage1, imgBtnAnsImage2, imgBtnAnsImage3, imgBtnAnsImage4;
    ProgressBar pbTop;
    Button btnAudio, btnAudioSlow;
    HelpPopup helpPopup;


    public static P2Fragment newInstance(int questionNo, int totalQuestions, String data) {
        P2Fragment fragment = new P2Fragment();
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
        View view = inflater.inflate(R.layout.layout_question_type_p2, container, false);
        initializeView(view);
        return view;
    }

    private void initializeView(View view) {
        imgBtnHome = view.findViewById(R.id.imgBtnHome);
        imgBtnHelp = view.findViewById(R.id.imgBtnHelp);
        pbTop = view.findViewById(R.id.pbTop);
        tvQuestionNo = view.findViewById(R.id.tvQuestionNo);
        btnAudio = view.findViewById(R.id.btnAudio);
        btnAudioSlow = view.findViewById(R.id.btnAudioSlow);
        imgBtnAnsImage1 = view.findViewById(R.id.imgBtnAnsImage1);
        imgBtnAnsImage2 = view.findViewById(R.id.imgBtnAnsImage2);
        imgBtnAnsImage3 = view.findViewById(R.id.imgBtnAnsImage3);
        imgBtnAnsImage4 = view.findViewById(R.id.imgBtnAnsImage4);
        imgBtnHome.setOnClickListener(this::onClick);
        imgBtnHelp.setOnClickListener(this::onClick);
        btnAudio.setOnClickListener(this::onClick);
        btnAudioSlow.setOnClickListener(this::onClick);
        imgBtnAnsImage1.setOnClickListener(this::onClick);
        imgBtnAnsImage2.setOnClickListener(this::onClick);
        imgBtnAnsImage3.setOnClickListener(this::onClick);
        imgBtnAnsImage4.setOnClickListener(this::onClick);
        setClickableButton(false);
    }

    private void setClickableButton(boolean clickable) {
        imgBtnHelp.setClickable(clickable);
        btnAudio.setClickable(clickable);
        btnAudioSlow.setClickable(clickable);
        imgBtnAnsImage1.setClickable(clickable);
        imgBtnAnsImage2.setClickable(clickable);
        imgBtnAnsImage3.setClickable(clickable);
        imgBtnAnsImage4.setClickable(clickable);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(P2ViewModel.class);
        cf = new CommonFunction();
        audio = new Audio();
        // TODO: Use the ViewModel
        setTopBarState(getArguments().getInt(Utils.TAG_QUESTION_NO), getArguments().getInt(Utils.TAG_QUESTIONS_TOTAL));
        Gson g = new Gson();
        question = g.fromJson(getArguments().getString(Utils.TAG_QUESTION_TYPE), Question.class);
        if(question.getUseRefLang() == 0)
            imgBtnHelp.setVisibility(View.INVISIBLE);
        else
            helpPopup = new HelpPopup("l1",2,1,1,question.getCellValue());
        audio.playAudioFile(Utils.FILE_DESTINATION_PATH + File.separator + question.getAudioPath());
        int[] imageViewIds = {R.id.imgBtnAnsImage1, R.id.imgBtnAnsImage2, R.id.imgBtnAnsImage3, R.id.imgBtnAnsImage4};
        String[] wrongImagePaths = {Utils.FILE_DESTINATION_PATH + File.separator + question.getRightImagePath(), Utils.FILE_DESTINATION_PATH + File.separator + question.getWrongImagePath1(), Utils.FILE_DESTINATION_PATH + File.separator + question.getWrongImagePath2(), Utils.FILE_DESTINATION_PATH + File.separator + question.getWrongImagePath3()};
        cf.setShuffleImages(getActivity(), imageViewIds, wrongImagePaths, getView());
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
                if(question.getUseRefLang() == 1){
                    helpPopup.showPopupWindow(getView());
                }
                break;
            case R.id.btnAudio:
                audio.playAudioFile(Utils.FILE_DESTINATION_PATH + File.separator + question.getAudioPath());
                break;
            case R.id.btnAudioSlow:
                audio.playAudioSlow(Utils.FILE_DESTINATION_PATH + File.separator + question.getAudioPath());
                break;
            case R.id.imgBtnAnsImage1:
                if (cf.CheckAnswerImage(imgBtnAnsImage1.getTag().toString()))
                    QuestionsActivity.mPager.setCurrentItem(QuestionsActivity.mPager.getCurrentItem()+1);
                break;
            case R.id.imgBtnAnsImage2:
                if (cf.CheckAnswerImage(imgBtnAnsImage2.getTag().toString()))
                    QuestionsActivity.mPager.setCurrentItem(QuestionsActivity.mPager.getCurrentItem()+1);
                break;
            case R.id.imgBtnAnsImage3:
                if (cf.CheckAnswerImage(imgBtnAnsImage3.getTag().toString()))
                    QuestionsActivity.mPager.setCurrentItem(QuestionsActivity.mPager.getCurrentItem()+1);
                break;
            case R.id.imgBtnAnsImage4:
                if (cf.CheckAnswerImage(imgBtnAnsImage4.getTag().toString()))
                    QuestionsActivity.mPager.setCurrentItem(QuestionsActivity.mPager.getCurrentItem()+1);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

}