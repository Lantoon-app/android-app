package com.bazinga.lantoon.home.chapter.lesson.ui.q;

import androidx.lifecycle.ViewModelProvider;

import android.media.MediaPlayer;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bazinga.lantoon.Audio;
import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.Utils;
import com.bazinga.lantoon.home.chapter.lesson.model.Question;
import com.bazinga.lantoon.home.chapter.lesson.ui.l1.L1Fragment;
import com.bazinga.lantoon.home.chapter.lesson.ui.l1.L1ViewModel;
import com.bazinga.lantoon.home.chapter.lesson.ui.p3.P3Fragment;
import com.bazinga.lantoon.home.chapter.lesson.ui.p3.P3ViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class QFragment extends Fragment implements View.OnClickListener {
    Audio audio;
    private QViewModel mViewModel;
    Question question;
    TextView tvQuestionNo, tvQuestionName1, tvQuestionName2;
    ImageButton imgBtnHome, imgBtnHelp, imgBtnNext;
    ProgressBar pbTop;
    ImageButton imbBtnQuestionImg1, imbBtnQuestionImg2;
    Button btnAudio1, btnAudio2, btnAudioSlow1, btnAudioSlow2;
    CommonFunction cf;

    public static QFragment newInstance(int questionNo, int totalQuestions, String data) {
        QFragment fragment = new QFragment();
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
        View view = inflater.inflate(R.layout.layout_question_type_q, container, false);
        initializeView(view);
        return view;
    }

    private void initializeView(View view) {


        imgBtnHome = view.findViewById(R.id.imgBtnHome);
        imgBtnHelp = view.findViewById(R.id.imgBtnHelp);
        imgBtnNext = view.findViewById(R.id.imgBtnNext);
        pbTop = view.findViewById(R.id.pbTop);
        tvQuestionNo = view.findViewById(R.id.tvQuestionNo);
        tvQuestionName1 = view.findViewById(R.id.tvQuestionName1);
        tvQuestionName2 = view.findViewById(R.id.tvQuestionName2);
        btnAudio1 = view.findViewById(R.id.btnAudio1);
        btnAudio2 = view.findViewById(R.id.btnAudio2);
        btnAudioSlow1 = view.findViewById(R.id.btnAudioSlow1);
        btnAudioSlow2 = view.findViewById(R.id.btnAudioSlow2);
        imbBtnQuestionImg1 = view.findViewById(R.id.imbBtnQuestionImg1);
        imbBtnQuestionImg2 = view.findViewById(R.id.imbBtnQuestionImg2);
        imgBtnHome.setOnClickListener(this::onClick);
        imgBtnHelp.setOnClickListener(this::onClick);
        imgBtnNext.setOnClickListener(this::onClick);
        btnAudio1.setOnClickListener(this::onClick);
        btnAudio2.setOnClickListener(this::onClick);
        btnAudioSlow1.setOnClickListener(this::onClick);
        btnAudioSlow2.setOnClickListener(this::onClick);
        imgBtnNext.setVisibility(View.GONE);
        //setClickableButton(false);
    }

    private void setClickableButton(boolean clickable) {
        btnAudio1.setClickable(clickable);
        btnAudio2.setClickable(clickable);
        btnAudioSlow1.setClickable(clickable);
        btnAudioSlow2.setClickable(clickable);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(QViewModel.class);
        // TODO: Use the ViewModel
        cf = new CommonFunction();
        //cf.fullScreen(getActivity().getWindow());
        audio = new Audio();

        setTopBarState(getArguments().getInt(Utils.TAG_QUESTION_NO), getArguments().getInt(Utils.TAG_QUESTIONS_TOTAL));
        Gson g = new Gson();
        question = g.fromJson(getArguments().getString(Utils.TAG_QUESTION_TYPE), Question.class);
        //PlayAudios(question);
        cf.setImagefromLocalFolder(getActivity(),Utils.FILE_DESTINATION_PATH + File.separator + question.getQtypeImagePath(),imbBtnQuestionImg1);
        cf.setImagefromLocalFolder(getActivity(),Utils.FILE_DESTINATION_PATH + File.separator + question.getRightImagePath(),imbBtnQuestionImg2);


    }

    private void setTopBarState(int quesNo, int totalQues) {
        tvQuestionNo.setText(quesNo + "/" + totalQues);
        int percentage = cf.percent(quesNo, totalQues);
        Log.d("percentage", String.valueOf(percentage));
        pbTop.setProgress(percentage);
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    private void PlayAudios(Question question) {
        try {
            tvQuestionName1.setText(question.getWord());
            cf.shakeAnimation(imbBtnQuestionImg1, 1000);
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(Utils.FILE_DESTINATION_PATH + File.separator + question.getAudioPath() + question.getCellValue() + ".mp3");
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.stop();
                mp.release();
                mp = new MediaPlayer();
                try {
                    tvQuestionName2.setText(question.getAnsWord());
                    cf.shakeAnimation(imbBtnQuestionImg2, 1000);
                    mp.setDataSource(Utils.FILE_DESTINATION_PATH + File.separator + question.getAnsAudioPath() + question.getCellValue() + ".mp3");
                    mp.prepare();
                    mp.start();
                    mp.setOnCompletionListener(mp1 -> {
                        mp1.stop();
                        mp1.release();
                        setClickableButton(true);
                        imgBtnNext.setVisibility(View.VISIBLE);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("qfragment error",e.getMessage());
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtnHome:
                break;
            case R.id.imgBtnHelp:
                break;
            case R.id.imgBtnNext:
                break;
            case R.id.btnAudio1:
                //cf.playAudio();Audio(Utils.FILE_DESTINATION_PATH + File.separator + question.getAudioPath() + question.getCellValue() + ".mp3");
                audio.playAudioFile(Utils.FILE_DESTINATION_PATH + File.separator + question.getAudioPath());
                tvQuestionName1.setText(question.getWord());
                cf.shakeAnimation(imbBtnQuestionImg1, 1000);
                break;
            case R.id.btnAudio2:
                //cf.playAudio();Audio(Utils.FILE_DESTINATION_PATH + File.separator + question.getAudioPath() + question.getCellValue() + ".mp3");
                audio.playAudioFile(Utils.FILE_DESTINATION_PATH + File.separator + question.getAnsAudioPath());
                tvQuestionName2.setText(question.getAnsWord());
                cf.shakeAnimation(imbBtnQuestionImg2, 1000);
                break;
            case R.id.btnAudioSlow1:
                //cf.playAudio();AudioSlow(getActivity(), Utils.FILE_DESTINATION_PATH + File.separator + question.getAudioPath() + question.getCellValue() + ".mp3");
                audio.playAudioSlow(Utils.FILE_DESTINATION_PATH + File.separator + question.getAudioPath());
                tvQuestionName1.setText(question.getWord());
                cf.shakeAnimation(imbBtnQuestionImg1, 1000);
                break;
            case R.id.btnAudioSlow2:
                //cf.playAudio();AudioSlow(getActivity(), Utils.FILE_DESTINATION_PATH + File.separator + question.getAudioPath() + question.getCellValue() + ".mp3");
                audio.playAudioSlow(Utils.FILE_DESTINATION_PATH + File.separator + question.getAnsAudioPath());
                tvQuestionName2.setText(question.getAnsWord());
                cf.shakeAnimation(imbBtnQuestionImg2, 1000);
                break;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

}