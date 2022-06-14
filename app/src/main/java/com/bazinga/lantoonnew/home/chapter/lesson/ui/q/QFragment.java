package com.bazinga.lantoonnew.home.chapter.lesson.ui.q;

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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bazinga.lantoonnew.Audio;
import com.bazinga.lantoonnew.CommonFunction;
import com.bazinga.lantoonnew.PlayPauseView;
import com.bazinga.lantoonnew.R;
import com.bazinga.lantoonnew.Tags;
import com.bazinga.lantoonnew.home.chapter.lesson.ReferencePopup;
import com.bazinga.lantoonnew.home.chapter.lesson.QuestionsActivity;
import com.bazinga.lantoonnew.home.chapter.lesson.model.Question;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class QFragment extends Fragment implements View.OnClickListener {

    private QViewModel mViewModel;
    Audio audio;
    Question question;
    TextView tvQuestionNo, tvQuestionName, tvQuestionAnswer;
    ImageButton imgBtnHome, imgBtnHelp, imgBtnNext;
    ProgressBar pbTop;
    ImageView imbBtnQuestionImg, imgBtnAnsImage;
    Button btnAudioSlow1, btnAudioSlow2;
    PlayPauseView btnAudio1, btnAudio2;
    CommonFunction cf;
    ReferencePopup referencePopup;
    int quesNo, totalQues;
    //MediaPlayer mediaPlayer;
    public static QFragment newInstance(int questionNo, int totalQuestions, String data) {
        QFragment fragment = new QFragment();
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
        tvQuestionName = view.findViewById(R.id.tvQuestionName);
        tvQuestionAnswer = view.findViewById(R.id.tvQuestionAnswer);
        btnAudio1 = view.findViewById(R.id.btnAudio1);
        btnAudio2 = view.findViewById(R.id.btnAudio2);
        btnAudioSlow1 = view.findViewById(R.id.btnAudioSlow1);
        btnAudioSlow2 = view.findViewById(R.id.btnAudioSlow2);
        imbBtnQuestionImg = view.findViewById(R.id.imbBtnQuestionImg);
        imgBtnAnsImage = view.findViewById(R.id.imgBtnAnsImage);
        imgBtnHome.setOnClickListener(this::onClick);
        imgBtnHelp.setOnClickListener(this::onClick);
        imgBtnNext.setOnClickListener(this::onClick);
        btnAudio1.setOnClickListener(this::onClick);
        btnAudio2.setOnClickListener(this::onClick);
        btnAudioSlow1.setOnClickListener(this::onClick);
        btnAudioSlow2.setOnClickListener(this::onClick);
        imgBtnNext.setVisibility(View.GONE);
        setClickableButton(false);
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

        quesNo = getArguments().getInt(Tags.TAG_QUESTION_NO);
        totalQues = getArguments().getInt(Tags.TAG_QUESTIONS_TOTAL);
        setTopBarState(quesNo, totalQues);
        Gson g = new Gson();
        question = g.fromJson(getArguments().getString(Tags.TAG_QUESTION_TYPE), Question.class);
        //PlayAudios(question);
        if(question.getReference() == null)
            imgBtnHelp.setVisibility(View.INVISIBLE);
        else
            referencePopup = new ReferencePopup( question.getReference());
        cf.setImage(getActivity(),QuestionsActivity.strFilePath + question.getQtypeImagePath(),imbBtnQuestionImg);
        cf.setImage(getActivity(),QuestionsActivity.strFilePath + question.getRightImagePath(),imgBtnAnsImage);


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
        PlayAudios(question);
    }

    private void PlayAudios(Question question) {
        try {
            tvQuestionName.setText(question.getWord());
            cf.shakeAnimation(imbBtnQuestionImg, 1000);
            btnAudio1.setState(PlayPauseView.STATE_PLAY);
            cf.mediaPlayer = new MediaPlayer();
            cf.mediaPlayer.setDataSource(QuestionsActivity.strFilePath + question.getAudioPath());
            cf.mediaPlayer.prepare();
            cf.mediaPlayer.start();
            cf.mediaPlayer.setOnCompletionListener(mp -> {
                mp.stop();
                mp.release();
                btnAudio1.setState(PlayPauseView.STATE_PAUSE);
                btnAudio1.setImageDrawable(getActivity().getDrawable(R.drawable.anim_vector_play));
                btnAudio2.setState(PlayPauseView.STATE_PLAY);
                cf.mediaPlayer = new MediaPlayer();
                try {
                    tvQuestionAnswer.setText(question.getAnsWord());
                    cf.shakeAnimation(imgBtnAnsImage, 1000);
                    cf.mediaPlayer.setDataSource(QuestionsActivity.strFilePath + question.getAnsAudioPath());
                    cf.mediaPlayer.prepare();
                    cf.mediaPlayer.start();
                    cf.mediaPlayer.setOnCompletionListener(mp1 -> {
                        mp1.stop();
                        mp1.release();
                        btnAudio2.setState(PlayPauseView.STATE_PAUSE);
                        btnAudio2.setImageDrawable(getActivity().getDrawable(R.drawable.anim_vector_play));
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
                cf.onClickHomeButton(getView(),getActivity(),getArguments().getInt(Tags.TAG_QUESTION_NO));
                break;
            case R.id.imgBtnHelp:
                if(question.getReference() != null) {
                    referencePopup.showPopupWindow(getView());
                }
                break;
            case R.id.imgBtnNext:
                QuestionsActivity.countMap.put(String.valueOf(getArguments().getInt(Tags.TAG_QUESTION_NO)),"0");
                QuestionsActivity.mPager.setCurrentItem(QuestionsActivity.mPager.getCurrentItem()+1);
                break;
            case R.id.btnAudio1:
                audio.playAudioFileAnim(getActivity(),QuestionsActivity.strFilePath + question.getAudioPath(),btnAudio1);
                tvQuestionName.setText(question.getWord());
                cf.shakeAnimation(imbBtnQuestionImg, 1000);
                break;
            case R.id.btnAudio2:
                audio.playAudioFileAnim(getActivity(),QuestionsActivity.strFilePath + question.getAnsAudioPath(),btnAudio2);
                tvQuestionAnswer.setText(question.getAnsWord());
                cf.shakeAnimation(imgBtnAnsImage, 1000);
                break;
            case R.id.btnAudioSlow1:
                audio.playAudioSlow(getActivity(),QuestionsActivity.strFilePath + question.getAudioPath());
                tvQuestionName.setText(question.getWord());
                cf.shakeAnimation(imbBtnQuestionImg, 1000);
                break;
            case R.id.btnAudioSlow2:
                audio.playAudioSlow(getActivity(),QuestionsActivity.strFilePath + question.getAnsAudioPath());
                tvQuestionAnswer.setText(question.getAnsWord());
                cf.shakeAnimation(imgBtnAnsImage, 1000);
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