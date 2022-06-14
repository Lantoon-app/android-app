package com.bazinga.lantoonnew.home.chapter.lesson.ui.l1;

import android.animation.Animator;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bazinga.lantoonnew.Audio;
import com.bazinga.lantoonnew.CommonFunction;
import com.bazinga.lantoonnew.PlayPauseView;
import com.bazinga.lantoonnew.R;
import com.bazinga.lantoonnew.Tags;
import com.bazinga.lantoonnew.home.chapter.lesson.QuestionsActivity;
import com.bazinga.lantoonnew.home.chapter.lesson.model.Question;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class L1Fragment extends Fragment implements View.OnClickListener {
    Audio audio;
    private L1ViewModel mViewModel;
    private ArrayList<Question> questions;
    LinearLayout linearLayout;
    TextView tvQuestionNo, tvQuestionName;
    ImageButton imgBtnHome, imgBtnHelp, imgBtnNext;
    ProgressBar pbTop;
    ImageView imbBtnQuestionImg1, imbBtnQuestionImg2, imbBtnQuestionImg3, imbBtnQuestionImg4, expandedImageView;
    Button btnAudioSlow1, btnAudioSlow2, btnAudioSlow3, btnAudioSlow4;
    PlayPauseView btnAudio1, btnAudio2, btnAudio3, btnAudio4;
    CommonFunction cf;
    //MediaPlayer cf.mediaPlayer;
    private Animator currentAnimator;
    private int shortAnimationDuration;
    View containerView;

    public static L1Fragment newInstance(int questionNo, int totalQuestions, String data) {
        L1Fragment fragment = new L1Fragment();
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
        View view = inflater.inflate(R.layout.layout_question_type_l1, container, false);
        initializeView(view);
        return view;
    }

    private void initializeView(View view) {

        containerView = view;
        expandedImageView = view.findViewById(R.id.expanded_image);
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
        linearLayout = view.findViewById(R.id.linearLayout2);

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
        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        /*imbBtnQuestionImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomImageIn(imbBtnQuestionImg1);
            }
        });*/
    }

    private void setClickableButton(boolean clickable) {
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
            setTopBarState(getArguments().getInt(Tags.TAG_QUESTION_NO), getArguments().getInt(Tags.TAG_QUESTIONS_TOTAL));

            questions = jsonStringToArray(getArguments().getString(Tags.TAG_QUESTION_TYPE));
            //PlayAudios(questions);
            imgBtnHelp.setVisibility(View.INVISIBLE);


            String imgFile = QuestionsActivity.strFilePath + questions.get(0).getRightImagePath() + questions.get(0).getCellValue() + ".jpg";
            Log.d("Imagefile", imgFile);

            cf.setImage(getActivity(), QuestionsActivity.strFilePath + questions.get(0).getRightImagePath(), imbBtnQuestionImg1);
            cf.setImage(getActivity(), QuestionsActivity.strFilePath + questions.get(1).getRightImagePath(), imbBtnQuestionImg2);
            cf.setImage(getActivity(), QuestionsActivity.strFilePath + questions.get(2).getRightImagePath(), imbBtnQuestionImg3);
            cf.setImage(getActivity(), QuestionsActivity.strFilePath + questions.get(3).getRightImagePath(), imbBtnQuestionImg4);

            Log.d("data l1 ", questions.get(2).getQid());


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        //PlayAudios(questions);
        new Thread(play).start();
    }

    private Runnable play = new Runnable() {
        int ThreadDelay = 1500;
        int Duration;

        @Override
        public void run() {
            try {
                cf.mediaPlayer = new MediaPlayer();
                cf.mediaPlayer.setDataSource(QuestionsActivity.strFilePath + questions.get(0).getAudioPath());
                cf.mediaPlayer.prepare();
                Duration = cf.mediaPlayer.getDuration() + ThreadDelay;
                Log.d("duration ", String.valueOf(Duration));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                sleep(ThreadDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    tvQuestionName.setText(questions.get(0).getWord());
                    //cf.shakeAnimation(imbBtnQuestionImg1, 1000);
                    zoomImageIn(imbBtnQuestionImg1);
                    btnAudio1.setState(PlayPauseView.STATE_PLAY);
                    cf.mediaPlayer.start();
                    cf.mediaPlayer.setOnCompletionListener(mp -> {
                        mp.stop();
                        mp.release();
                        zoomImageOut(imbBtnQuestionImg1);
                        btnAudio1.setState(PlayPauseView.STATE_PAUSE);
                        btnAudio1.setImageDrawable(getActivity().getDrawable(R.drawable.anim_vector_play));
                    });
                }

            });
            try {
                sleep(Duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("duration", "end");
            try {
                cf.mediaPlayer = new MediaPlayer();
                cf.mediaPlayer.setDataSource(QuestionsActivity.strFilePath + questions.get(1).getAudioPath());
                cf.mediaPlayer.prepare();
                Duration = cf.mediaPlayer.getDuration() + ThreadDelay;
                Log.d("duration ", String.valueOf(Duration));
            } catch (IOException e) {
                e.printStackTrace();
            }
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    tvQuestionName.setText(questions.get(1).getWord());
                    //cf.shakeAnimation(imbBtnQuestionImg1, 1000);
                    zoomImageIn(imbBtnQuestionImg2);
                    btnAudio2.setState(PlayPauseView.STATE_PLAY);
                    cf.mediaPlayer.start();
                    cf.mediaPlayer.setOnCompletionListener(mp -> {
                        mp.stop();
                        mp.release();
                        zoomImageOut(imbBtnQuestionImg2);
                        btnAudio2.setState(PlayPauseView.STATE_PAUSE);
                        btnAudio2.setImageDrawable(getActivity().getDrawable(R.drawable.anim_vector_play));

                    });
                }
            });
            try {
                sleep(Duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("duration", "end");
            try {
                cf.mediaPlayer = new MediaPlayer();
                cf.mediaPlayer.setDataSource(QuestionsActivity.strFilePath + questions.get(2).getAudioPath());
                cf.mediaPlayer.prepare();
                Duration = cf.mediaPlayer.getDuration() + ThreadDelay;
                Log.d("duration ", String.valueOf(Duration));
            } catch (IOException e) {
                e.printStackTrace();
            }
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    tvQuestionName.setText(questions.get(2).getWord());
                    //cf.shakeAnimation(imbBtnQuestionImg1, 1000);
                    zoomImageIn(imbBtnQuestionImg3);
                    btnAudio3.setState(PlayPauseView.STATE_PLAY);
                    cf.mediaPlayer.start();
                    cf.mediaPlayer.setOnCompletionListener(mp -> {
                        mp.stop();
                        mp.release();
                        zoomImageOut(imbBtnQuestionImg3);
                        btnAudio3.setState(PlayPauseView.STATE_PAUSE);
                        btnAudio3.setImageDrawable(getActivity().getDrawable(R.drawable.anim_vector_play));

                    });
                }
            });
            try {
                sleep(Duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("duration", "end");
            try {
                cf.mediaPlayer = new MediaPlayer();
                cf.mediaPlayer.setDataSource(QuestionsActivity.strFilePath + questions.get(3).getAudioPath());
                cf.mediaPlayer.prepare();
                Duration = cf.mediaPlayer.getDuration() + ThreadDelay;
                Log.d("duration ", String.valueOf(Duration));
            } catch (IOException e) {
                e.printStackTrace();
            }
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    tvQuestionName.setText(questions.get(3).getWord());
                    //cf.shakeAnimation(imbBtnQuestionImg1, 1000);
                    zoomImageIn(imbBtnQuestionImg4);
                    btnAudio4.setState(PlayPauseView.STATE_PLAY);

                    cf.mediaPlayer.start();
                    cf.mediaPlayer.setOnCompletionListener(mp -> {
                        mp.stop();
                        mp.release();
                        zoomImageOut(imbBtnQuestionImg4);
                        btnAudio4.setState(PlayPauseView.STATE_PAUSE);
                        btnAudio4.setImageDrawable(getActivity().getDrawable(R.drawable.anim_vector_play));

                    });
                }
            });
            try {
                sleep(Duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("duration", "final end");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setClickableButton(true);
                    imgBtnNext.setVisibility(View.VISIBLE);
                }
            });
        }
    };


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
            //cf.shakeAnimation(imbBtnQuestionImg1, 1000);
            zoomImageIn(imbBtnQuestionImg1);
            btnAudio1.setState(PlayPauseView.STATE_PLAY);
            cf.mediaPlayer = new MediaPlayer();
            cf.mediaPlayer.setDataSource(QuestionsActivity.strFilePath + questions.get(0).getAudioPath());
            cf.mediaPlayer.prepare();
            cf.mediaPlayer.start();
            cf.mediaPlayer.setOnCompletionListener(mp -> {
                mp.stop();
                mp.release();
                zoomImageOut(imbBtnQuestionImg1);
                btnAudio1.setState(PlayPauseView.STATE_PAUSE);
                btnAudio1.setImageDrawable(getActivity().getDrawable(R.drawable.anim_vector_play));
                btnAudio2.setState(PlayPauseView.STATE_PLAY);
                cf.mediaPlayer = new MediaPlayer();
                try {
                    tvQuestionName.setText(questions.get(1).getWord());
                    //cf.shakeAnimation(imbBtnQuestionImg2, 1000);
                    zoomImageIn(imbBtnQuestionImg2);
                    cf.mediaPlayer.setDataSource(QuestionsActivity.strFilePath + questions.get(1).getAudioPath());
                    cf.mediaPlayer.prepare();
                    cf.mediaPlayer.start();
                    cf.mediaPlayer.setOnCompletionListener(mp1 -> {
                        mp1.stop();
                        mp1.release();
                        zoomImageOut(imbBtnQuestionImg2);
                        btnAudio2.setState(PlayPauseView.STATE_PAUSE);
                        btnAudio2.setImageDrawable(getActivity().getDrawable(R.drawable.anim_vector_play));
                        btnAudio3.setState(PlayPauseView.STATE_PLAY);
                        cf.mediaPlayer = new MediaPlayer();
                        try {
                            tvQuestionName.setText(questions.get(2).getWord());
                            //cf.shakeAnimation(imbBtnQuestionImg3, 1000);
                            zoomImageIn(imbBtnQuestionImg3);
                            cf.mediaPlayer.setDataSource(QuestionsActivity.strFilePath + questions.get(2).getAudioPath());
                            cf.mediaPlayer.prepare();
                            cf.mediaPlayer.start();
                            cf.mediaPlayer.setOnCompletionListener(mp11 -> {
                                mp11.stop();
                                mp11.release();
                                zoomImageOut(imbBtnQuestionImg3);
                                btnAudio3.setState(PlayPauseView.STATE_PAUSE);
                                btnAudio3.setImageDrawable(getActivity().getDrawable(R.drawable.anim_vector_play));
                                btnAudio4.setState(PlayPauseView.STATE_PLAY);
                                cf.mediaPlayer = new MediaPlayer();
                                try {
                                    tvQuestionName.setText(questions.get(3).getWord());
                                    //cf.shakeAnimation(imbBtnQuestionImg4, 1000);
                                    zoomImageIn(imbBtnQuestionImg4);
                                    cf.mediaPlayer.setDataSource(QuestionsActivity.strFilePath + questions.get(3).getAudioPath());
                                    cf.mediaPlayer.prepare();
                                    cf.mediaPlayer.start();
                                    cf.mediaPlayer.setOnCompletionListener(mp111 -> {
                                        mp111.stop();
                                        mp111.release();
                                        zoomImageOut(imbBtnQuestionImg4);
                                        btnAudio4.setState(PlayPauseView.STATE_PAUSE);
                                        btnAudio4.setImageDrawable(getActivity().getDrawable(R.drawable.anim_vector_play));
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
                cf.onClickHomeButton(getView(), getActivity(), getArguments().getInt(Tags.TAG_QUESTION_NO));
                break;
            case R.id.imgBtnHelp:
                break;
            case R.id.imgBtnNext:
                QuestionsActivity.countMap.put(String.valueOf(getArguments().getInt(Tags.TAG_QUESTION_NO)), "0");
                QuestionsActivity.mPager.setCurrentItem(QuestionsActivity.mPager.getCurrentItem() + 1);
                break;
            case R.id.btnAudio1:
                audio.playAudioFileAnim(getActivity(), QuestionsActivity.strFilePath + questions.get(0).getAudioPath(), btnAudio1);
                tvQuestionName.setText(questions.get(0).getWord());
                cf.shakeAnimation(imbBtnQuestionImg1, 1000);
                break;
            case R.id.btnAudio2:
                audio.playAudioFileAnim(getActivity(), QuestionsActivity.strFilePath + questions.get(1).getAudioPath(), btnAudio2);
                tvQuestionName.setText(questions.get(1).getWord());
                cf.shakeAnimation(imbBtnQuestionImg2, 1000);
                break;
            case R.id.btnAudio3:
                audio.playAudioFileAnim(getActivity(), QuestionsActivity.strFilePath + questions.get(2).getAudioPath(), btnAudio3);
                tvQuestionName.setText(questions.get(2).getWord());
                cf.shakeAnimation(imbBtnQuestionImg3, 1000);
                break;
            case R.id.btnAudio4:
                audio.playAudioFileAnim(getActivity(), QuestionsActivity.strFilePath + questions.get(3).getAudioPath(), btnAudio4);
                tvQuestionName.setText(questions.get(3).getWord());
                cf.shakeAnimation(imbBtnQuestionImg4, 1000);
                break;
            case R.id.btnAudioSlow1:
                audio.playAudioSlow(getActivity(), QuestionsActivity.strFilePath + questions.get(0).getAudioPath());
                tvQuestionName.setText(questions.get(0).getWord());
                cf.shakeAnimation(imbBtnQuestionImg1, 1000);
                break;
            case R.id.btnAudioSlow2:
                audio.playAudioSlow(getActivity(), QuestionsActivity.strFilePath + questions.get(1).getAudioPath());
                tvQuestionName.setText(questions.get(1).getWord());
                cf.shakeAnimation(imbBtnQuestionImg2, 1000);
                break;
            case R.id.btnAudioSlow3:
                audio.playAudioSlow(getActivity(), QuestionsActivity.strFilePath + questions.get(2).getAudioPath());
                tvQuestionName.setText(questions.get(2).getWord());
                cf.shakeAnimation(imbBtnQuestionImg3, 1000);
                break;
            case R.id.btnAudioSlow4:
                audio.playAudioSlow(getActivity(), QuestionsActivity.strFilePath + questions.get(3).getAudioPath());
                tvQuestionName.setText(questions.get(3).getWord());
                cf.shakeAnimation(imbBtnQuestionImg4, 1000);

                break;
        }

    }

    //https://developer.android.com/training/animation/zoom.html
    private void zoomImageIn(ImageView thumbView) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.

        expandedImageView.setImageDrawable(thumbView.getDrawable());


        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        containerView
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
       /* AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;*/

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //zoomImageOut(thumbView);
            }
        });
    }

    private void zoomImageOut(View thumbView) {
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        containerView
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }
        // Animate the four positioning/sizing properties in parallel,
        // back to their original values.
        final float startScaleFinal = startScale;
        /*AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator
                .ofFloat(expandedImageView, View.X, startBounds.left))
                .with(ObjectAnimator
                        .ofFloat(expandedImageView,
                                View.Y, startBounds.top))
                .with(ObjectAnimator
                        .ofFloat(expandedImageView,
                                View.SCALE_X, startScaleFinal))
                .with(ObjectAnimator
                        .ofFloat(expandedImageView,
                                View.SCALE_Y, startScaleFinal));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                thumbView.setAlpha(1f);
                expandedImageView.setVisibility(View.GONE);
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                thumbView.setAlpha(1f);
                expandedImageView.setVisibility(View.GONE);
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;*/
        thumbView.setAlpha(1f);
        expandedImageView.setVisibility(View.GONE);
    }
}