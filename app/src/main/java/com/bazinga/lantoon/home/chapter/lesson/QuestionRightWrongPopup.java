package com.bazinga.lantoon.home.chapter.lesson;

import android.app.Activity;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Animatable2.AnimationCallback;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.bazinga.lantoon.R;

public class QuestionRightWrongPopup {
    AnimatedVectorDrawableCompat animatedVectorDrawableCompat;
    AnimatedVectorDrawable animatedVectorDrawable;
    MediaPlayer mediaPlayer;

    public QuestionRightWrongPopup() {

    }

    public void showPopup(Activity activity, final View view, boolean right, boolean isLast) {

        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_question_answer_anim, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler
        if (right) {
            mediaPlayer = MediaPlayer.create(activity, R.raw.right_answer);
            mediaPlayer.start();
            ImageView imageViewRight = popupView.findViewById(R.id.imageViewRight);
            imageViewRight.setVisibility(View.VISIBLE);
            Drawable drawable = imageViewRight.getDrawable();
            if (drawable instanceof AnimatedVectorDrawableCompat) {
                animatedVectorDrawableCompat = (AnimatedVectorDrawableCompat) drawable;
                animatedVectorDrawableCompat.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                    @Override
                    public void onAnimationEnd(Drawable drawable) {
                        super.onAnimationEnd(drawable);
                        mediaPlayer.release();
                        popupWindow.dismiss();
                        if (isLast) {
                            LessonCompletedPopup lessonCompletedPopup = new LessonCompletedPopup();
                            lessonCompletedPopup.showPopupWindow(view, activity);
                        } else {
                            QuestionsActivity.mPager.setCurrentItem(QuestionsActivity.mPager.getCurrentItem() + 1);
                        }
                    }
                });
                animatedVectorDrawableCompat.start();

            } else if (drawable instanceof AnimatedVectorDrawable) {
                animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
                animatedVectorDrawable.registerAnimationCallback(new AnimationCallback() {
                    @Override
                    public void onAnimationEnd(Drawable drawable) {
                        super.onAnimationEnd(drawable);
                        mediaPlayer.release();
                        popupWindow.dismiss();
                        if (isLast) {
                            LessonCompletedPopup lessonCompletedPopup = new LessonCompletedPopup();
                            lessonCompletedPopup.showPopupWindow(view, activity);
                        } else {
                            QuestionsActivity.mPager.setCurrentItem(QuestionsActivity.mPager.getCurrentItem() + 1);
                        }
                    }
                });
                animatedVectorDrawable.start();
            }
        } else {
            mediaPlayer = MediaPlayer.create(activity, R.raw.wrong_answer);
            mediaPlayer.start();
            ImageView imageViewWrong = popupView.findViewById(R.id.imageViewWrong);
            imageViewWrong.setVisibility(View.VISIBLE);
            Drawable drawable1 = imageViewWrong.getDrawable();
            if (drawable1 instanceof AnimatedVectorDrawableCompat) {
                animatedVectorDrawableCompat = (AnimatedVectorDrawableCompat) drawable1;
                animatedVectorDrawableCompat.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                    @Override
                    public void onAnimationEnd(Drawable drawable1) {
                        super.onAnimationEnd(drawable1);
                        mediaPlayer.release();
                        popupWindow.dismiss();

                    }
                });
                animatedVectorDrawableCompat.start();

            } else if (drawable1 instanceof AnimatedVectorDrawable) {
                animatedVectorDrawable = (AnimatedVectorDrawable) drawable1;
                animatedVectorDrawable.registerAnimationCallback(new AnimationCallback() {
                    @Override
                    public void onAnimationEnd(Drawable drawable1) {
                        super.onAnimationEnd(drawable1);
                        mediaPlayer.release();
                        popupWindow.dismiss();

                    }
                });
                animatedVectorDrawable.start();
            }
        }
        //Handler for clicking on the inactive zone of the window

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mediaPlayer.release();
                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });

    }

}
