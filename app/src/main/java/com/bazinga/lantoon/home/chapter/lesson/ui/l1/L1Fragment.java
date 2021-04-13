package com.bazinga.lantoon.home.chapter.lesson.ui.l1;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.media.MediaPlayer;
import android.os.Build;
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

import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.Audio;
import com.bazinga.lantoon.Utils;
import com.bazinga.lantoon.home.chapter.lesson.QuestionsActivity;
import com.bazinga.lantoon.home.chapter.lesson.model.Question;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class L1Fragment extends Fragment implements View.OnClickListener {
    Audio audio;
    private L1ViewModel mViewModel;
    private ArrayList<Question> questions;
    TextView tvQuestionNo, tvQuestionName;
    ImageButton imgBtnHome, imgBtnHelp, imgBtnNext;
    ProgressBar pbTop;
    ImageView imbBtnQuestionImg1, imbBtnQuestionImg2, imbBtnQuestionImg3, imbBtnQuestionImg4;
    Button btnAudio1, btnAudio2, btnAudio3, btnAudio4, btnAudioSlow1, btnAudioSlow2, btnAudioSlow3, btnAudioSlow4;
    CommonFunction cf;

    public static L1Fragment newInstance(int questionNo, int totalQuestions, String data) {
        L1Fragment fragment = new L1Fragment();
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
        View view = inflater.inflate(R.layout.layout_question_type_l1, container, false);
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
        btnAudio1 = view.findViewById(R.id.btnAudio1);
        btnAudio2 = view.findViewById(R.id.btnAudio2);
        btnAudio3 = view.findViewById(R.id.btnAudio3);
        btnAudio4 = view.findViewById(R.id.btnAudio4);
        btnAudioSlow1 = view.findViewById(R.id.btnAudioSlow1);
        btnAudioSlow2 = view.findViewById(R.id.btnAudioSlow2);
        btnAudioSlow3 = view.findViewById(R.id.btnAudioSlow3);
        btnAudioSlow4 = view.findViewById(R.id.btnAudioSlow4);
        imbBtnQuestionImg1 = view.findViewById(R.id.imbBtnQuestionImg1);
        imbBtnQuestionImg2 = view.findViewById(R.id.imbBtnQuestionImg2);
        imbBtnQuestionImg3 = view.findViewById(R.id.imbBtnQuestionImg3);
        imbBtnQuestionImg4 = view.findViewById(R.id.imbBtnQuestionImg4);
        imgBtnHome.setOnClickListener(this::onClick);
        imgBtnHelp.setOnClickListener(this::onClick);
        imgBtnNext.setOnClickListener(this::onClick);
        btnAudio1.setOnClickListener(this::onClick);
        btnAudio2.setOnClickListener(this::onClick);
        btnAudio3.setOnClickListener(this::onClick);
        btnAudio4.setOnClickListener(this::onClick);
        btnAudioSlow1.setOnClickListener(this::onClick);
        btnAudioSlow2.setOnClickListener(this::onClick);
        btnAudioSlow3.setOnClickListener(this::onClick);
        btnAudioSlow4.setOnClickListener(this::onClick);
        imgBtnNext.setVisibility(View.GONE);
        setClickableButton(false);


    }
    private void setClickableButton(boolean clickable){
        btnAudio1.setClickable(clickable);
        btnAudio2.setClickable(clickable);
        btnAudio3.setClickable(clickable);
        btnAudio4.setClickable(clickable);
        btnAudioSlow1.setClickable(clickable);
        btnAudioSlow2.setClickable(clickable);
        btnAudioSlow3.setClickable(clickable);
        btnAudioSlow4.setClickable(clickable);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(L1ViewModel.class);
        // TODO: Use the ViewModel
        cf = new CommonFunction();
        //cf.fullScreen(getActivity().getWindow());
        audio = new Audio();
        try {
            setTopBarState(getArguments().getInt(Utils.TAG_QUESTION_NO), getArguments().getInt(Utils.TAG_QUESTIONS_TOTAL));

            questions = jsonStringToArray(getArguments().getString(Utils.TAG_QUESTION_TYPE));
            //PlayAudios(questions);
                imgBtnHelp.setVisibility(View.INVISIBLE);


            String imgFile = QuestionsActivity.strFilePath + File.separator + questions.get(0).getRightImagePath() + questions.get(0).getCellValue() + ".jpg";
            Log.d("Imagefile", imgFile);

            cf.setImage(getActivity(),QuestionsActivity.strFilePath + File.separator + questions.get(0).getRightImagePath(),imbBtnQuestionImg1);
            cf.setImage(getActivity(),QuestionsActivity.strFilePath + File.separator + questions.get(1).getRightImagePath(),imbBtnQuestionImg2);
            cf.setImage(getActivity(),QuestionsActivity.strFilePath + File.separator + questions.get(2).getRightImagePath(),imbBtnQuestionImg3);
            cf.setImage(getActivity(),QuestionsActivity.strFilePath + File.separator + questions.get(3).getRightImagePath(),imbBtnQuestionImg4);



            Log.d("data l1 ", questions.get(2).getQid());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void setTopBarState(int quesNo, int totalQues) {
        tvQuestionNo.setText(quesNo + "/" + totalQues);
        int percentage = cf.percent(quesNo, totalQues);
        Log.d("percentage", String.valueOf(percentage));
        pbTop.setProgress(cf.percent(quesNo, totalQues));
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        PlayAudios(questions);
    }

    private ArrayList<Question> jsonStringToArray(String jsonString) throws JSONException {

        ArrayList<Question> stringArray = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(jsonString);
        Gson g = new Gson();

        for (int i = 0; i < jsonArray.length(); i++) {
            stringArray.add(i, g.fromJson(jsonArray.get(i).toString(), Question.class));
        }

        return stringArray;
    }

    private void PlayAudios(ArrayList<Question> questions) {
        try {
            tvQuestionName.setText(questions.get(0).getWord());
            cf.shakeAnimation(imbBtnQuestionImg1, 1000);
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(QuestionsActivity.strFilePath + File.separator + questions.get(0).getAudioPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.stop();
                mp.release();
                mp = new MediaPlayer();
                try {
                    tvQuestionName.setText(questions.get(1).getWord());
                    cf.shakeAnimation(imbBtnQuestionImg2, 1000);
                    mp.setDataSource(QuestionsActivity.strFilePath + File.separator + questions.get(1).getAudioPath());
                    mp.prepare();
                    mp.start();
                    mp.setOnCompletionListener(mp1 -> {
                        mp1.stop();
                        mp1.release();
                        mp1 = new MediaPlayer();
                        try {
                            tvQuestionName.setText(questions.get(2).getWord());
                            cf.shakeAnimation(imbBtnQuestionImg3, 1000);
                            mp1.setDataSource(QuestionsActivity.strFilePath + File.separator + questions.get(2).getAudioPath());
                            mp1.prepare();
                            mp1.start();
                            mp1.setOnCompletionListener(mp11 -> {
                                mp11.stop();
                                mp11.release();
                                mp11 = new MediaPlayer();
                                try {
                                    tvQuestionName.setText(questions.get(3).getWord());
                                    cf.shakeAnimation(imbBtnQuestionImg4, 1000);
                                    mp11.setDataSource(QuestionsActivity.strFilePath + File.separator + questions.get(3).getAudioPath());
                                    mp11.prepare();
                                    mp11.start();
                                    mp11.setOnCompletionListener(mp111 -> {
                                        mp111.stop();
                                        mp111.release();
                                        setClickableButton(true);
                                        imgBtnNext.setVisibility(View.VISIBLE);
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
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
                cf.onClickHomeButton(getView(),getActivity(),getArguments().getInt(Utils.TAG_QUESTION_NO));
                break ;
            case R.id.imgBtnHelp:
                break;
            case R.id.imgBtnNext:
                QuestionsActivity.countMap.put(String.valueOf(getArguments().getInt(Utils.TAG_QUESTION_NO)),"0");
                QuestionsActivity.mPager.setCurrentItem(QuestionsActivity.mPager.getCurrentItem()+1);
                break;
            case R.id.btnAudio1:
                audio.playAudioFile(QuestionsActivity.strFilePath + File.separator + questions.get(0).getAudioPath());
                tvQuestionName.setText(questions.get(0).getWord());
                cf.shakeAnimation(imbBtnQuestionImg1, 1000);
                break;
            case R.id.btnAudio2:
                audio.playAudioFile(QuestionsActivity.strFilePath + File.separator + questions.get(1).getAudioPath());
                tvQuestionName.setText(questions.get(1).getWord());
                cf.shakeAnimation(imbBtnQuestionImg2, 1000);
                break;
            case R.id.btnAudio3:
                audio.playAudioFile(QuestionsActivity.strFilePath + File.separator + questions.get(2).getAudioPath());
                tvQuestionName.setText(questions.get(2).getWord());
                cf.shakeAnimation(imbBtnQuestionImg3, 1000);
                break;
            case R.id.btnAudio4:
                audio.playAudioFile(QuestionsActivity.strFilePath + File.separator + questions.get(3).getAudioPath());
                tvQuestionName.setText(questions.get(3).getWord());
                cf.shakeAnimation(imbBtnQuestionImg4, 1000);
                break;
            case R.id.btnAudioSlow1:
                audio.playAudioSlow(QuestionsActivity.strFilePath + File.separator + questions.get(0).getAudioPath());
                tvQuestionName.setText(questions.get(0).getWord());
                cf.shakeAnimation(imbBtnQuestionImg1, 1000);
                break;
            case R.id.btnAudioSlow2:
                audio.playAudioSlow(QuestionsActivity.strFilePath + File.separator + questions.get(1).getAudioPath());
                tvQuestionName.setText(questions.get(1).getWord());
                cf.shakeAnimation(imbBtnQuestionImg2, 1000);
                break;
            case R.id.btnAudioSlow3:
                audio.playAudioSlow(QuestionsActivity.strFilePath + File.separator + questions.get(2).getAudioPath());
                tvQuestionName.setText(questions.get(2).getWord());
                cf.shakeAnimation(imbBtnQuestionImg3, 1000);
                break;
            case R.id.btnAudioSlow4:
                audio.playAudioSlow(QuestionsActivity.strFilePath + File.separator + questions.get(3).getAudioPath());
                tvQuestionName.setText(questions.get(3).getWord());
                cf.shakeAnimation(imbBtnQuestionImg4, 1000);

                break;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}