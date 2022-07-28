package com.bazinga.lantoonnew.home.chapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bazinga.lantoonnew.BuildConfig;
import com.bazinga.lantoonnew.CommonFunction;
import com.bazinga.lantoonnew.NetworkUtil;
import com.bazinga.lantoonnew.R;
import com.bazinga.lantoonnew.Tags;
import com.bazinga.lantoonnew.home.chapter.lesson.QuestionsActivity;
import com.bazinga.lantoonnew.home.chapter.lesson.model.ContinueNext;
import com.bazinga.lantoonnew.home.chapter.model.Chapter;
import com.bazinga.lantoonnew.home.chapter.model.ChapterResponse;
import com.bazinga.lantoonnew.login.SessionManager;
import com.google.gson.GsonBuilder;
import com.lukedeighton.wheelview.WheelView;
import com.lukedeighton.wheelview.adapter.WheelArrayAdapter;

import java.util.List;


public class ChapterFragmentNew extends Fragment implements View.OnClickListener {

    private ChapterViewModel chapterViewModel;
    //ChapterAdapter mChapterAdapter;
    //GridLayoutManager mLayoutManager;
    SessionManager sessionManager;
    //RecyclerView mRecyclerView;
    WheelView wheelView;
    CAdapter cAdapter;
    public static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 100;
    private boolean isLoading = false;
    int itemCount = 0;
    boolean fragmentDestroyed = false;
    ProgressBar progressBar;
    RatingBar ratingBar;
    ImageView iv_chapter, ivMaintenance;
    Vibrator v;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sessionManager = new SessionManager(getContext());

        @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        chapterViewModel = new ViewModelProvider(this,
                new ChapterViewModelFactory(sessionManager.getLearnLangId(), sessionManager.getUid(), deviceId)).get(ChapterViewModel.class);
        System.out.println("deviceid " + deviceId);
        View root = inflater.inflate(R.layout.fragment_chapter_new, container, false);
        wheelView = root.findViewById(R.id.wheelview);
        ratingBar = root.findViewById(R.id.ratingBar);
        iv_chapter = root.findViewById(R.id.iv_chapter);
        ivMaintenance = root.findViewById(R.id.ivMaintenance);

        progressBar = root.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        iv_chapter.setOnClickListener(this);

        preparedListItem();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // get the VIBRATOR_SERVICE system service
        v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        wheelView.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
            @Override
            public void onWheelItemClick(WheelView parent, int position, boolean isSelected) {
                onClickChapter(position);
            }
        });
        wheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectListener() {
            @Override
            public void onWheelItemSelected(WheelView parent, Drawable itemDrawable, int position) {
                selectWheel(position);
                iv_chapter.setTag(position);
            }
        });
        wheelView.setOnWheelAngleChangeListener(new WheelView.OnWheelAngleChangeListener() {
            @Override
            public void onWheelAngleChange(float angle) {
                //the new angle of the wheel
                //Log.d("onWheelAngleChange",String.valueOf(angle));
            }
        });

    }

    private void onClickChapter(int position) {
        Chapter chapter = cAdapter.getItem(position);
        ContinueNext continueNext = cAdapter.continueNext;
        Log.d("Chapter ", new GsonBuilder().setPrettyPrinting().create().toJson(cAdapter.getItem(position)));
/*
                continueNext.setTargetType("evaluation");
                chapter.setEvaluationId("1");
                chapter.setPosition(1);*/

        if (NetworkUtil.getConnectivityStatus(getActivity()) != 0) {
            if (chapter.getPosition() == 1) {
                Intent intent = new Intent(getActivity(), QuestionsActivity.class);
                if (continueNext.getTargetType().equals("chapter")) {
                    intent.putExtra(Tags.TAG_IS_EVALUATION_QUESTIONS, false);
                    intent.putExtra(Tags.TAG_CHAPTER_TYPE, chapter.getChapterType());
                    intent.putExtra(Tags.TAG_CHAPTER_NO, continueNext.getChapterno());
                } else if (continueNext.getTargetType().equals("evaluation")) {
                    intent.putExtra(Tags.TAG_IS_EVALUATION_QUESTIONS, true);
                    intent.putExtra(Tags.TAG_CHAPTER_TYPE, 2);
                    intent.putExtra(Tags.TAG_CHAPTER_NO, chapter.getEvaluationId());
                }
                intent.putExtra(Tags.TAG_TARGET_TYPE, continueNext.getTargetType());
                intent.putExtra(Tags.TAG_IS_NEW_CHAPTER, true);
                intent.putExtra(Tags.TAG_IS_RANDOM_QUESTIONS, false);
                intent.putExtra(Tags.TAG_LANGUAGE_ID, continueNext.getLangid());
                intent.putExtra(Tags.TAG_LESSON_NO, continueNext.getLessonno());
                intent.putExtra(Tags.TAG_SPENT_TIME, continueNext.getSpenttime());
                intent.putExtra(Tags.TAG_START_QUESTION_NO, continueNext.getStartingquesno());
                getActivity().startActivity(intent);
            }
        } else {
            CommonFunction.netWorkErrorAlert(getActivity());
        }
                /*if (Integer.valueOf(chapter.getChapterNo()) <= continueNext.getChapterno() && Integer.valueOf(chapter.getChapterNo()) <= Integer.valueOf(continueNext.getUnlockedChapters())) {

                    if (NetworkUtil.getConnectivityStatus(getActivity()) != 0) {
                        Intent intent = new Intent(getActivity(), QuestionsActivity.class);
                        if (Integer.valueOf(chapter.getChapterNo()) == continueNext.getChapterno()) {
                            intent.putExtra(Tags.TAG_IS_NEW_CHAPTER, true);
                            intent.putExtra(Tags.TAG_IS_RANDOM_QUESTIONS, false);
                            intent.putExtra(Tags.TAG_LANGUAGE_ID, continueNext.getLangid());
                            intent.putExtra(Tags.TAG_CHAPTER_NO, continueNext.getChapterno());
                            intent.putExtra(Tags.TAG_LESSON_NO, continueNext.getLessonno());
                            intent.putExtra(Tags.TAG_SPENT_TIME, continueNext.getSpenttime());
                            intent.putExtra(Tags.TAG_START_QUESTION_NO, continueNext.getStartingquesno());
                            intent.putExtra(Tags.TAG_CHAPTER_TYPE, chapter.getChapterType());
                        } else if (chapter.getActiveLesson() != null && chapter.getActiveLesson().getLessonno() > 0) {
                            intent.putExtra(Tags.TAG_IS_NEW_CHAPTER, false);
                            intent.putExtra(Tags.TAG_IS_RANDOM_QUESTIONS, false);
                            intent.putExtra(Tags.TAG_LANGUAGE_ID, Integer.valueOf(chapter.getLanguageName()));
                            intent.putExtra(Tags.TAG_CHAPTER_NO, Integer.valueOf(chapter.getChapterNo()));
                            intent.putExtra(Tags.TAG_LESSON_NO, chapter.getActiveLesson().getLessonno());
                            intent.putExtra(Tags.TAG_SPENT_TIME, "0");
                            intent.putExtra(Tags.TAG_START_QUESTION_NO, chapter.getActiveLesson().getStartingquesno());
                            intent.putExtra(Tags.TAG_CHAPTER_TYPE, chapter.getChapterType());
                        } else if (chapter.getActiveLesson() != null && chapter.getActiveLesson().getLessonno() == 0) {
                            Log.d("isRandomQuestion adap", "true");
                            Log.d("isRandomQuestion gem", chapter.getGemcount().toString());
                            intent.putExtra(Tags.TAG_IS_NEW_CHAPTER, false);
                            intent.putExtra(Tags.TAG_IS_RANDOM_QUESTIONS, true);
                            intent.putExtra(Tags.TAG_LANGUAGE_ID, Integer.valueOf(chapter.getLanguageName()));
                            intent.putExtra(Tags.TAG_CHAPTER_NO, Integer.valueOf(chapter.getChapterNo()));
                            intent.putExtra(Tags.TAG_LESSON_NO, 0);
                            intent.putExtra(Tags.TAG_SPENT_TIME, "0");
                            intent.putExtra(Tags.TAG_START_QUESTION_NO, 0);
                            intent.putExtra(Tags.TAG_CHAPTER_TYPE, chapter.getChapterType());
                        }
                        getActivity().startActivity(intent);
                    } else {
                        CommonFunction.netWorkErrorAlert(getActivity());
                    }

                }*/

    }

    private void selectWheel(int position) {
        Log.d("onWheelItemSelected", String.valueOf(position));
        Chapter chapter = cAdapter.getItem(position);
        Log.d("Chapter ", new GsonBuilder().setPrettyPrinting().create().toJson(cAdapter.getItem(position)));
        assert v != null;
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.EFFECT_HEAVY_CLICK);
        } else {
            //deprecated in API 26
            v.vibrate(2000);
        }
        if (chapter.getChapterID() != null) {


            switch (chapter.getCompletedLessons()) {
                case 0:
                    iv_chapter.setBackground(getActivity().getDrawable(R.drawable.chapter_not_started));
                    break;
                case 25:
                    iv_chapter.setBackground(getActivity().getDrawable(R.drawable.chapter_lession1_completed_circle));
                    break;
                case 50:
                    iv_chapter.setBackground(getActivity().getDrawable(R.drawable.chapter_lession2_completed_circle));
                    break;
                case 75:
                    iv_chapter.setBackground(getActivity().getDrawable(R.drawable.chapter_lession3_completed_circle));
                    break;
                case 100:
                    iv_chapter.setBackground(getActivity().getDrawable(R.drawable.chapter_completed));
                    break;
            }
            switch (Integer.parseInt(chapter.getChapterLevel())) {
                case 1:
                    iv_chapter.setImageDrawable(getActivity().getDrawable(R.drawable.chapter_level_1));
                    break;
                case 2:
                    iv_chapter.setImageDrawable(getActivity().getDrawable(R.drawable.chapter_level_2));
                    break;
                case 3:
                    iv_chapter.setImageDrawable(getActivity().getDrawable(R.drawable.chapter_level_3));
                    break;
                case 4:
                    iv_chapter.setImageDrawable(getActivity().getDrawable(R.drawable.chapter_level_4));
                    break;
                case 5:
                    iv_chapter.setImageDrawable(getActivity().getDrawable(R.drawable.chapter_level_5));
                    break;

            }
            ratingBar.setRating(chapter.getGemcount());
        }
        if (chapter.getEvaluationId() != null) {
            switch (chapter.getCompleted()) {
                case 0:
                    iv_chapter.setBackground(getActivity().getDrawable(R.drawable.chapter_not_started));
                    iv_chapter.setImageDrawable(null);
                    break;
                case 100:
                    iv_chapter.setBackground(getActivity().getDrawable(R.drawable.evaluation_completed));
                    iv_chapter.setImageDrawable(null);
                    break;
            }
        }
    }

    private void preparedListItem() {
        System.out.println("Chapter preparedListItem page no = " + String.valueOf(currentPage));
        if (NetworkUtil.getConnectivityStatus(getContext()) != 0) {
            if (isLoading == false)
                progressBar.setVisibility(View.VISIBLE);
            chapterViewModel.getChapters().observe(getActivity(), new Observer<ChapterResponse>() {

                @Override
                public void onChanged(ChapterResponse chapterResponse) {
                    progressBar.setVisibility(View.GONE);
                    if (!fragmentDestroyed) {
                        if (chapterResponse == null) {

                        } else {
                            if (chapterResponse.getStatus().getCode() == 1032) {
                               /* chapterResponse.getData().get(1).setGemcount(1);
                                chapterResponse.getData().get(2).setGemcount(2);
                                chapterResponse.getData().get(3).setGemcount(3);
                                chapterResponse.getData().get(4).setGemcount(4);
                                chapterResponse.getData().get(5).setGemcount(5);
                                chapterResponse.getData().get(1).setCompletedLessons(25);
                                chapterResponse.getData().get(2).setCompletedLessons(50);
                                chapterResponse.getData().get(3).setCompletedLessons(75);
                                chapterResponse.getData().get(4).setCompletedLessons(100);*/

                                cAdapter = new CAdapter(chapterResponse.getData(), chapterResponse.getContinuenext(), getContext());
                                wheelView.setAdapter(cAdapter);
                                for (int i = 0; i < chapterResponse.getData().size(); i++) {
                                    System.out.println(chapterResponse.getData().get(i));
                                    if (chapterResponse.getData().get(i).getPosition() == 1) {
                                        wheelView.setSelected(i);
                                        selectWheel(i);
                                        iv_chapter.setTag(i);
                                    }

                                }
                            } else if (chapterResponse.getStatus().getCode() == 1111) {
                                appUpdateAlert(chapterResponse.getStatus().getMessage());
                            } else if (chapterResponse.getStatus().getCode() == 2043) {
                                Toast.makeText(getContext(), chapterResponse.getStatus().getMessage(), Toast.LENGTH_LONG).show();
                                sessionManager.logoutUser();
                            } else if (chapterResponse.getStatus().getCode() == 20008) {
                                ivMaintenance.setVisibility(View.VISIBLE);
                                //appUpdateAlert(chapterResponse.getStatus().getMessage());
                            }

                        }
                    }
                }
            });
        } else {
            //CommonFunction.netWorkErrorAlert(getActivity());
            // Snackbar.make(activity.getCurrentFocus().getRootView(), activity.getString(R.string.msg_network_failed), Snackbar.LENGTH_SHORT).show();
        }

    }

    private void appUpdateAlert(String msg) {
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_app_update, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = false;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler

        TextView tvMessage = popupView.findViewById(R.id.tvMessage);
        Button btnExit = popupView.findViewById(R.id.btnExit);
        Button btnUpdate = popupView.findViewById(R.id.btnUpdate);


        tvMessage.setText(msg);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                getActivity().finishAffinity();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // popupWindow.dismiss();
                final String appPackageName = BuildConfig.APPLICATION_ID; // getPackageName() from Context or Activity object
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        });


        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //popupWindow.dismiss();
                return true;
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentDestroyed = true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_chapter:

                onClickChapter(Integer.parseInt(iv_chapter.getTag().toString()));
                break;
        }
    }

    static class CAdapter extends WheelArrayAdapter<Chapter> {
        Context context;
        ContinueNext continueNext;

        CAdapter(List<Chapter> chapters, ContinueNext continueNext, Context context) {
            super(chapters);
            this.continueNext = continueNext;
            this.context = context;
        }

        @Override
        public Drawable getDrawable(int position) {
            //getItem(3).setCompleted(100);
            if (getItem(position).getChapterID() != null) {
                if (getItem(position).getPosition() == 1) {
                    return new LayerDrawable(new Drawable[]{
                            createOvalDrawable(context.getColor(R.color.chapter_in_progress_inner), context.getColor(R.color.chapter_in_progress_outer)),
                            new TextDrawable(getItem(position).getChapterNo(), context.getColor(R.color.black))
                    });
                } else if (getItem(position).getCompletedLessons() == 100)
                    return new LayerDrawable(new Drawable[]{
                            createOvalDrawable(context.getColor(R.color.chapter_progress_completed), 0),
                            new TextDrawable(getItem(position).getChapterNo(), context.getColor(R.color.white))
                    });
                else
                    return new LayerDrawable(new Drawable[]{
                            createOvalDrawable(context.getColor(R.color.chapter_disabled), 0),
                            new TextDrawable(getItem(position).getChapterNo(), context.getColor(R.color.white))
                    });
            }
            if (getItem(position).getEvaluationId() != null) {
                if (getItem(position).getPosition() == 1) {
                    return new LayerDrawable(new Drawable[]{
                            createOvalDrawable(context.getColor(R.color.chapter_in_progress_inner), context.getColor(R.color.chapter_in_progress_outer)),
                            new TextDrawable(getItem(position).getChapterNo(), context.getColor(R.color.black))
                    });
                } else if (getItem(position).getCompleted() == 100)
                    return new LayerDrawable(new Drawable[]{
                            createOvalDrawable(context.getColor(R.color.chapter_evaluation_progress_completed), 0),
                            new TextDrawable(getItem(position).getEvaluationId(), context.getColor(R.color.white))
                    });
                else
                    return new LayerDrawable(new Drawable[]{
                            createOvalDrawable(context.getColor(R.color.chapter_evaluation_disabled), 0),
                            new TextDrawable(getItem(position).getEvaluationId(), context.getColor(R.color.white))
                    });
            }


            /*
            if (Integer.parseInt(getItem(position).getChapterNo()) <= continueNext.getChapterno() && Integer.parseInt(getItem(position).getChapterNo()) <= Integer.parseInt(continueNext.getUnlockedChapters())) {
                if (getItem(position).getCompletedLessons() == 100)
                    return new LayerDrawable(new Drawable[]{
                            createOvalDrawable(context.getColor(R.color.chapter_progress_completed), 0),
                            new TextDrawable(getItem(position).getChapterNo(), context.getColor(R.color.white))
                    });
                else
                    return new LayerDrawable(new Drawable[]{
                            createOvalDrawable(context.getColor(R.color.chapter_in_progress_inner), context.getColor(R.color.chapter_in_progress_outer)),
                            new TextDrawable(getItem(position).getChapterNo(), context.getColor(R.color.black))
                    });
            } else
                return new LayerDrawable(new Drawable[]{
                        createOvalDrawable(context.getColor(R.color.chapter_disabled), 0),
                        new TextDrawable(getItem(position).getChapterNo(), context.getColor(R.color.white))
                });
*/
            return null;
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Override
        public Chapter getItem(int position) {
            return super.getItem(position);
        }

        private Drawable createOvalDrawable(int colorInner, int colorOuter) {
            int strokeWidth = 20;
            GradientDrawable gD = new GradientDrawable();
            gD.setColor(colorInner);
            gD.setShape(GradientDrawable.OVAL);
            if (colorOuter != 0)
                gD.setStroke(strokeWidth, colorOuter);
           /* ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
            shapeDrawable.getPaint().setColor(color);*/
            return gD;
        }
    }
}