package com.bazinga.lantoonnew.home.chapter.lesson.ui;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bazinga.lantoonnew.Audio;
import com.bazinga.lantoonnew.CommonFunction;
import com.bazinga.lantoonnew.R;
import com.bazinga.lantoonnew.Tags;
import com.bazinga.lantoonnew.home.chapter.lesson.QuestionsActivity;
import com.bazinga.lantoonnew.home.chapter.lesson.ReferencePopup;
import com.bazinga.lantoonnew.home.chapter.lesson.model.Question;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

public class D2Fragment extends Fragment implements View.OnClickListener {


    CommonFunction cf;
    Audio audio;
    Question question;
    ReferencePopup referencePopup;
    int quesNo, totalQues;
    TextView tvQuestionNo, tvFibQuestion;
    ImageButton imgBtnHome, imgBtnHelp;
    ImageView imbBtnQuestionImg;
    LinearLayout ll_D2_Ans;
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
        ll_D2_Ans = view.findViewById(R.id.ll_D2_Ans);

        imgBtnHome.setOnClickListener(this::onClick);
        btnAudio.setOnClickListener(this::onClick);
        btnAudioSlow.setOnClickListener(this::onClick);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
            referencePopup = new ReferencePopup(question.getReference());
        tvFibQuestion.setText(question.getFibQues());
        cf.setImage(getActivity(), QuestionsActivity.strFilePath + question.getRightImagePath(), imbBtnQuestionImg);
        if (question.getFibOpt() != null)
            for (int i = 0; i < question.getFibOpt().size(); i++) {
                Log.d("FibOpt", question.getFibOpt().get(i));
                TextView textView = new TextView(getContext());
                textView.setTextAppearance(R.style.tvStyleD2answer);
                textView.setTag(question.getFibOpt().get(i));
                textView.setBackground(getContext().getDrawable(R.drawable.bg_tv_ref_inner));
                textView.setText(question.getFibOpt().get(i));
                ll_D2_Ans.addView(textView);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());
                        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

                        ClipData dragData = new ClipData(view.getTag().toString(), mimeTypes, item);
                        View.DragShadowBuilder myShadow = new View.DragShadowBuilder(textView);

                        view.startDrag(dragData, myShadow, null, 0);
                    }
                });
                setDragDrop(textView);
            }

        Log.d("getChild", String.valueOf(ll_D2_Ans.getChildCount()));
        Log.d("data d2 ", new GsonBuilder().setPrettyPrinting().create().toJson(question));
    }

    private void setDragDrop(View view) {
        // Set the drag event listener for the View.
        view.setOnDragListener((v, event) -> {

            // Handles each of the expected events.
            switch (event.getAction()) {

                case DragEvent.ACTION_DRAG_STARTED:
                    //layoutParams = (RelativeLayout.LayoutParams)v.getLayoutParams();
                    Log.d("msg", "Action is DragEvent.ACTION_DRAG_STARTED");

                    // Do nothing
                    break;

                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.d("msg", "Action is DragEvent.ACTION_DRAG_ENTERED");
                    int x_cord = (int) event.getX();
                    int y_cord = (int) event.getY();
                    break;

                case DragEvent.ACTION_DRAG_EXITED :
                    Log.d("msg", "Action is DragEvent.ACTION_DRAG_EXITED");
                    x_cord = (int) event.getX();
                    y_cord = (int) event.getY();
                    break;

                case DragEvent.ACTION_DRAG_LOCATION  :
                    Log.d("msg", "Action is DragEvent.ACTION_DRAG_LOCATION");
                    x_cord = (int) event.getX();
                    y_cord = (int) event.getY();
                    break;

                case DragEvent.ACTION_DRAG_ENDED   :
                    Log.d("msg", "Action is DragEvent.ACTION_DRAG_ENDED");

                    // Do nothing
                    break;

                case DragEvent.ACTION_DROP:
                    Log.d("msg", "ACTION_DROP event");

                    // Do nothing
                    break;
                default: break;
            }

            return false;

        });
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
                cf.onClickHomeButton(getView(), getActivity(), getArguments().getInt(Tags.TAG_QUESTION_NO));
                break;
            case R.id.imgBtnHelp:
                if (question.getReference() != null) {
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