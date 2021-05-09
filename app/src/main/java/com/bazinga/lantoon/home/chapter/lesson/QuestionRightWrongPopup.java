package com.bazinga.lantoon.home.chapter.lesson;

import android.app.Activity;
import android.graphics.drawable.Animatable2.AnimationCallback;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.GsonBuilder;


public class QuestionRightWrongPopup {
    AnimatedVectorDrawableCompat animatedVectorDrawableCompat;
    AnimatedVectorDrawable animatedVectorDrawable;
    MediaPlayer mediaPlayer;

    public QuestionRightWrongPopup() {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showPopup(Activity activity, final View view, boolean right, boolean isLast, int quesNo, int attemptCount, boolean isSpeech, int pMark, int nMark) {

        CommonFunction cf = new CommonFunction();
        LessonCompletedPopup lessonCompletedPopup = new LessonCompletedPopup();
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
                        QuestionsActivity.CalculateMarks(pMark, 0, pMark);
                        if (isLast) {
                            if (!QuestionsActivity.isRandomQuestion) {
                                QuestionsActivity.countMap.put(String.valueOf(quesNo), String.valueOf(attemptCount));
                                QuestionsActivity.score.setAttemptcount(QuestionsActivity.countMap);
                                QuestionsActivity.score.setCompletedques(String.valueOf(quesNo));
                                Log.d("attemptCount", QuestionsActivity.countMap.toString());
                            //LessonCompletedPopup lessonCompletedPopup = new LessonCompletedPopup();
                            //lessonCompletedPopup.showPopupWindow(view, activity);
                                QuestionsActivity.score.setSpentTime(QuestionsActivity.tvTimer.getText().toString());
                                cf.postLesson(view, activity, quesNo,QuestionsActivity.tvTimer.getText().toString());
                                System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(QuestionsActivity.score));
                            } else {

                                lessonCompletedPopup.showPopupWindow(view, activity, null, quesNo,QuestionsActivity.tvTimer.getText().toString());
                            }

                        } else {
                            QuestionsActivity.countMap.put(String.valueOf(quesNo), String.valueOf(attemptCount));
                            Log.d("attemptCount", QuestionsActivity.countMap.toString());
                            QuestionsActivity.mPager.setCurrentItem(QuestionsActivity.mPager.getCurrentItem() + 1);
                        }
                        System.out.println("Pmark " + QuestionsActivity.Pmark + "Nmark " + QuestionsActivity.Nmark + "OutOfTotal " + QuestionsActivity.OutOfTotal);
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
                        QuestionsActivity.CalculateMarks(pMark, 0, pMark);
                        if (isLast) {
                            if (!QuestionsActivity.isRandomQuestion) {
                                QuestionsActivity.countMap.put(String.valueOf(quesNo), String.valueOf(attemptCount));
                                QuestionsActivity.score.setAttemptcount(QuestionsActivity.countMap);
                                QuestionsActivity.score.setCompletedques(String.valueOf(quesNo));
                                Log.d("attemptCount", QuestionsActivity.countMap.toString());
                            //LessonCompletedPopup lessonCompletedPopup = new LessonCompletedPopup();
                            //lessonCompletedPopup.showPopupWindow(view, activity);
                                QuestionsActivity.score.setSpentTime(QuestionsActivity.tvTimer.getText().toString());
                                cf.postLesson(view, activity, quesNo,QuestionsActivity.tvTimer.getText().toString());
                                System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(QuestionsActivity.score));
                            } else {

                                lessonCompletedPopup.showPopupWindow(view, activity, null, quesNo,QuestionsActivity.tvTimer.getText().toString());
                            }
                        } else {
                            QuestionsActivity.countMap.put(String.valueOf(quesNo), String.valueOf(attemptCount));
                            Log.d("attemptCount", QuestionsActivity.countMap.toString());
                            QuestionsActivity.mPager.setCurrentItem(QuestionsActivity.mPager.getCurrentItem() + 1);
                        }
                        System.out.println("Pmark " + QuestionsActivity.Pmark + "Nmark " + QuestionsActivity.Nmark + "OutOfTotal " + QuestionsActivity.OutOfTotal);
                    }
                });
                animatedVectorDrawable.start();
            }
        } else {
            Log.d("checkerror ", "attemptCount"+attemptCount+isSpeech+isLast);
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

                        if (attemptCount == 2 && isSpeech) {
                            QuestionsActivity.CalculateMarks(0, nMark, pMark);
                            if (isLast) {
                                if (!QuestionsActivity.isRandomQuestion) {
                                    QuestionsActivity.countMap.put(String.valueOf(quesNo), "n");
                                    QuestionsActivity.score.setAttemptcount(QuestionsActivity.countMap);
                                    QuestionsActivity.score.setCompletedques(String.valueOf(quesNo));
                                    Log.d("attemptCount", QuestionsActivity.countMap.toString());
                            /*LessonCompletedPopup lessonCompletedPopup = new LessonCompletedPopup();
                            lessonCompletedPopup.showPopupWindow(view, activity);*/
                                    QuestionsActivity.score.setSpentTime(QuestionsActivity.tvTimer.getText().toString());
                                    cf.postLesson(view, activity, quesNo,QuestionsActivity.tvTimer.getText().toString());
                                    System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(QuestionsActivity.score));
                                } else {
                                    lessonCompletedPopup.showPopupWindow(view, activity, null, quesNo,QuestionsActivity.tvTimer.getText().toString());
                                }
                            } else {

                                QuestionsActivity.countMap.put(String.valueOf(quesNo), "n");
                                Log.d("attemptCount", QuestionsActivity.countMap.toString());
                                QuestionsActivity.mPager.setCurrentItem(QuestionsActivity.mPager.getCurrentItem() + 1);

                            }
                        } else {
                            QuestionsActivity.CalculateMarks(0, nMark, 0);
                        }
                        System.out.println("Pmark " + QuestionsActivity.Pmark + "Nmark " + QuestionsActivity.Nmark + "OutOfTotal " + QuestionsActivity.OutOfTotal);
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
                        QuestionsActivity.CalculateMarks(0, nMark, 0);
                        if (attemptCount == 2 && isSpeech) {
                            QuestionsActivity.CalculateMarks(0, nMark, pMark);
                            if (isLast) {
                                if (!QuestionsActivity.isRandomQuestion) {
                                    QuestionsActivity.countMap.put(String.valueOf(quesNo), "n");
                                    QuestionsActivity.score.setAttemptcount(QuestionsActivity.countMap);
                                    QuestionsActivity.score.setCompletedques(String.valueOf(quesNo));
                                    Log.d("attemptCount", QuestionsActivity.countMap.toString());
                            /*LessonCompletedPopup lessonCompletedPopup = new LessonCompletedPopup();
                            lessonCompletedPopup.showPopupWindow(view, activity);*/
                                    QuestionsActivity.score.setSpentTime(QuestionsActivity.tvTimer.getText().toString());
                                    cf.postLesson(view, activity, quesNo,QuestionsActivity.tvTimer.getText().toString());
                                    System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(QuestionsActivity.score));
                                } else {
                                    lessonCompletedPopup.showPopupWindow(view, activity, null, quesNo,QuestionsActivity.tvTimer.getText().toString());
                                }
                            } else {

                                QuestionsActivity.countMap.put(String.valueOf(quesNo), "n");
                                Log.d("attemptCount", QuestionsActivity.countMap.toString());
                                QuestionsActivity.mPager.setCurrentItem(QuestionsActivity.mPager.getCurrentItem() + 1);

                            }
                        } else {
                            QuestionsActivity.CalculateMarks(0, nMark, 0);
                        }
                        System.out.println("Pmark " + QuestionsActivity.Pmark + "Nmark " + QuestionsActivity.Nmark + "OutOfTotal " + QuestionsActivity.OutOfTotal);
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
