package com.bazinga.lantoonnew.home.chapter.lesson;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bazinga.lantoonnew.R;
import com.bazinga.lantoonnew.home.chapter.lesson.model.PostLessonResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class ChapterCompletedPopup {
    PopupWindow popupWindow;
    ProgressBar progressBar, pbChapterNew;
    TextView tvChapterNumber, tvChapterNumberNew;
    ImageView ivGif, ivDisabled, ivLock;
    final int[] pStatus = {0};
    Handler handler = new Handler();
    FrameLayout frame1, frame2;

    @SuppressLint("SetTextI18n")
    public void showPopupWindow(View view, Activity activity, PostLessonResponse postLessonResponse, int quesNo, String strTimeSpent) {

        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_chapter_completed, null);


        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = false;

        //Create a window with our parameters
        popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.setOutsideTouchable(false);
        // ViewGroup transitionsContainer = popupView.findViewById(R.id.transitions_container);
        frame1 = popupView.findViewById(R.id.frame1);
        frame2 = popupView.findViewById(R.id.frame2);
        progressBar = popupView.findViewById(R.id.pbChapter);
        pbChapterNew = popupView.findViewById(R.id.pbChapterNew);
        tvChapterNumber = popupView.findViewById(R.id.tvChapterNumber);
        tvChapterNumberNew = popupView.findViewById(R.id.tvChapterNumberNew);
        ivGif = popupView.findViewById(R.id.ivGif);
        ivDisabled = popupView.findViewById(R.id.ivDisabled);
        ivLock = popupView.findViewById(R.id.ivLock);
        if (postLessonResponse != null)
            tvChapterNumber.setText("CHAPTER " + String.valueOf(postLessonResponse.getContinuenext().getChapterno() - 1));
        else tvChapterNumber.setText("CHAPTER 1");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (pStatus[0] <= 100) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(pStatus[0]);
                            if (progressBar.getProgress() == 100) {
                                showCompletedGif(view, activity, postLessonResponse, quesNo, strTimeSpent);
                            }
                        }
                    });
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pStatus[0]++;
                }
            }
        }).start();

       /* popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });*/

    }

    private void showCompletedGif(View view, Activity activity, PostLessonResponse postLessonResponse, int quesNo, String strTimeSpent) {
        Glide.with(activity).load(R.drawable.gif_right_answer).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (resource instanceof GifDrawable) {
                    ((GifDrawable) resource).setLoopCount(1);
                    ((GifDrawable) resource).registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                        @Override
                        public void onAnimationEnd(Drawable drawable) {
                            super.onAnimationEnd(drawable);
                            pbChapterNew.setProgress(0);
                            ivDisabled.setVisibility(View.GONE);
                            ivLock.setVisibility(View.GONE);
                            frame1.animate()
                                    .translationY(0)
                                    .alpha(0.0f)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            frame1.setVisibility(View.GONE);
                                            frame2.animate().translationX(Gravity.CENTER).setListener(new AnimatorListenerAdapter() {
                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    super.onAnimationEnd(animation);
                                                    if (postLessonResponse != null)
                                                        tvChapterNumberNew.setText("CHAPTER " + String.valueOf(postLessonResponse.getContinuenext().getChapterno()));
                                                    else tvChapterNumberNew.setText("CHAPTER 2");
                                                    tvChapterNumberNew.animate().translationZ(0).setDuration(1000).setListener(new AnimatorListenerAdapter() {
                                                        @Override
                                                        public void onAnimationEnd(Animator animation) {
                                                            super.onAnimationEnd(animation);
                                                            popupWindow.dismiss();
                                                            LessonCompletedPopup lessonCompletedPopup = new LessonCompletedPopup();
                                                            lessonCompletedPopup.showPopupWindow(view, activity, postLessonResponse, quesNo, strTimeSpent);
                                                        }
                                                    });

                                                }
                                            });

                                        }
                                    });


                        }
                    });
                }
                return false;
            }


        }).into(ivGif);

    }

}
