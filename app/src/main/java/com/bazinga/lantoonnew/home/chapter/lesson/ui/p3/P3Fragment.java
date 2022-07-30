package com.bazinga.lantoonnew.home.chapter.lesson.ui.p3;

import static com.bazinga.lantoonnew.home.chapter.lesson.QuestionsActivity.isEvaluation;

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
import com.bazinga.lantoonnew.home.chapter.lesson.QuestionsActivity;
import com.bazinga.lantoonnew.home.chapter.lesson.ReferencePopup;
import com.bazinga.lantoonnew.home.chapter.lesson.model.Question;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import me.ibrahimsn.lib.CirclesLoadingView;

public class P3Fragment extends Fragment implements View.OnClickListener {

    private P3ViewModel mViewModel;
    CommonFunction cf;
    Audio audio;
    Question question;
    TextView tvQuestionNo, tvQuestionName, tvRecText;
    ImageButton imgBtnHome, imgBtnHelp;
    ImageView imbBtnQuestionImg;
    ProgressBar pbTop;
    Button  btnAudioSlow, btnMic;
    CirclesLoadingView circlesLoadingView;
    Button btnAudio;
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
        circlesLoadingView = view.findViewById(R.id.lvLoading);
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
        cf.setImage(getActivity(), QuestionsActivity.strFilePath + question.getRightImagePath(), imbBtnQuestionImg);
        //audio.playAudioFile(QuestionsActivity.strFilePath + question.getAudioPath());

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
        PlayAudios(question);
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
                audio.playAudioFileAnim(getActivity(),QuestionsActivity.strFilePath + question.getAudioPath(),null);
                break;
            case R.id.btnAudioSlow:
                audio.playAudioSlow(getActivity(), QuestionsActivity.strFilePath + question.getAudioPath());
                break;
            case R.id.btnMic:
                //String withoutSplChar = "t.e?s!t. le.tt!er ";
                //String ansWrd =question.getAnsWord().replaceAll("[^ .,a-zA-Z0-9]", "").replace(".","").trim();
                String ansWrd =question.getAnsWord().replace("?","").replace("!","").replace(".","").trim();

                System.out.println("withoutSplChar "+ansWrd);
                if (quesNo == totalQues)
                    cf.speechToText(getContext(), tvRecText,circlesLoadingView, ansWrd, true, getView(), getActivity(), quesNo, question,audio,null,isEvaluation);
                else
                    cf.speechToText(getContext(), tvRecText,circlesLoadingView, ansWrd, false, getView(), getActivity(), quesNo, question,audio,null,isEvaluation);

                break;
        }
    }
    private void PlayAudios(Question question) {
        try {
            tvQuestionName.setText(question.getWord());
            cf.setImageBorder(imbBtnQuestionImg, 20, getActivity().getDrawable(R.drawable.bg_all_answer_imgs));
            //cf.shakeAnimation(imbBtnQuestionImg, 1000);
            //btnAudio1.setState(PlayPauseView.STATE_PLAY);
            cf.mediaPlayer = new MediaPlayer();
            cf.mediaPlayer.setDataSource(QuestionsActivity.strFilePath + question.getAudioPath());
            cf.mediaPlayer.prepare();
            cf.mediaPlayer.start();
            cf.mediaPlayer.setOnCompletionListener(mp -> {
                mp.stop();
                mp.release();
                cf.setImageBorder(imbBtnQuestionImg, 0, null);
                cf.mikeAnimation(btnMic, 2000);
                //btnAudio1.setState(PlayPauseView.STATE_PAUSE);
                //btnAudio1.setImageDrawable(getActivity().getDrawable(R.drawable.anim_vector_play));
                //btnAudio2.setState(PlayPauseView.STATE_PLAY);
            });

        } catch (Exception e) {
            e.printStackTrace();
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