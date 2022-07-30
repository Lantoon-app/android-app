package com.bazinga.lantoonnew.home.chapter.lesson;

import android.app.Activity;
import android.graphics.drawable.Animatable2.AnimationCallback;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.RequiresApi;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.bazinga.lantoonnew.Audio;
import com.bazinga.lantoonnew.CommonFunction;
import com.bazinga.lantoonnew.PlayPauseView;
import com.bazinga.lantoonnew.R;
import com.bazinga.lantoonnew.home.chapter.lesson.model.Question;
import com.google.gson.GsonBuilder;


public class QuestionRightWrongPopup {
    AnimatedVectorDrawableCompat animatedVectorDrawableCompat;
    AnimatedVectorDrawable animatedVectorDrawable;
    MediaPlayer mediaPlayer;

    public QuestionRightWrongPopup() {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showPopup(Activity activity, final View view, boolean right, boolean isLast, int quesNo, int attemptCount, boolean isSpeech, Question question, Audio audio, PlayPauseView btnAudio, ImageView ansImageView, int[] imageViewIds, String[] imagePaths, boolean isEvaluation) {

        CommonFunction cf = new CommonFunction();
        LessonCompletedPopup lessonCompletedPopup = new LessonCompletedPopup();
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_question_answer_anim, null);


        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = false;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);


        //Set the location of the window on the screen
        popupWindow.setOutsideTouchable(false);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler
        if (right) {
            if (ansImageView != null) {
                ansImageView.setBackground(activity.getDrawable(R.drawable.bg_all_answer_imgs_right));
                ansImageView.setPadding(20, 20, 20, 20);
            }
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
                        answerRight(cf, activity,  view, ansImageView, attemptCount, question, isLast, quesNo, lessonCompletedPopup, popupWindow, isEvaluation);
                    }
                });
                animatedVectorDrawableCompat.start();

            } else if (drawable instanceof AnimatedVectorDrawable) {
                animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
                animatedVectorDrawable.registerAnimationCallback(new AnimationCallback() {
                    @Override
                    public void onAnimationEnd(Drawable drawable) {
                        super.onAnimationEnd(drawable);
                        answerRight(cf, activity, view, ansImageView, attemptCount, question, isLast, quesNo, lessonCompletedPopup, popupWindow, isEvaluation);
                    }
                });
                animatedVectorDrawable.start();
            }
        } else {

            if (ansImageView != null) {
                ansImageView.setBackground(activity.getDrawable(R.drawable.bg_all_answer_imgs_wrong));
                ansImageView.setPadding(20, 20, 20, 20);
            }
            Log.d("checkerror ", "attemptCount" + attemptCount + isSpeech + isLast);
            mediaPlayer = MediaPlayer.create(activity, R.raw.wrong_answer);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (attemptCount == 2 && isSpeech) {

                    } else
                        audio.playAudioFileAnim(activity, QuestionsActivity.strFilePath + question.getAudioPath(), btnAudio);
                }
            });
            ImageView imageViewWrong = popupView.findViewById(R.id.imageViewWrong);
            imageViewWrong.setVisibility(View.VISIBLE);
            Drawable drawable1 = imageViewWrong.getDrawable();
            if (drawable1 instanceof AnimatedVectorDrawableCompat) {
                animatedVectorDrawableCompat = (AnimatedVectorDrawableCompat) drawable1;
                animatedVectorDrawableCompat.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                    @Override
                    public void onAnimationEnd(Drawable drawable1) {
                        super.onAnimationEnd(drawable1);
                        Log.d("test answerWrong", String.valueOf(isLast));
                        answerWrong(cf, activity, imageViewIds, imagePaths, view, ansImageView, attemptCount, question, isLast, isSpeech, quesNo, lessonCompletedPopup, popupWindow, isEvaluation);
                    }
                });
                animatedVectorDrawableCompat.start();

            } else if (drawable1 instanceof AnimatedVectorDrawable) {
                animatedVectorDrawable = (AnimatedVectorDrawable) drawable1;
                animatedVectorDrawable.registerAnimationCallback(new AnimationCallback() {
                    @Override
                    public void onAnimationEnd(Drawable drawable1) {
                        super.onAnimationEnd(drawable1);
                        Log.d("test answerWrong", String.valueOf(isLast));
                        answerWrong(cf, activity, imageViewIds, imagePaths, view, ansImageView, attemptCount, question, isLast, isSpeech, quesNo, lessonCompletedPopup, popupWindow, isEvaluation);
                    }
                });
                animatedVectorDrawable.start();
            }
        }
    }

    private void answerRight(CommonFunction cf, Activity activity,View view, ImageView ansImageView, int attemptCount, Question question, boolean isLast,int quesNo, LessonCompletedPopup lessonCompletedPopup, PopupWindow popupWindow, boolean isEvaluation) {
        if (ansImageView != null) {
            ansImageView.setPadding(0, 0, 0, 0);
        }
        QuestionsActivity.CalculateMarks(question.getPlusMark(), 0, question.getPlusMark());
        if(isEvaluation){
            QuestionsActivity.addEvaluationScore(question.getChapterNo(),1,0);
        }
        Log.d("test answerRight isLast", String.valueOf(isLast));
        if (isLast) {
            if (!QuestionsActivity.isRandomQuestion) {
                QuestionsActivity.countMap.put(String.valueOf(quesNo), String.valueOf(attemptCount));
                QuestionsActivity.score.setAttemptcount(QuestionsActivity.countMap);
                QuestionsActivity.score.setCompletedques(String.valueOf(quesNo));
                Log.d("attemptCount", QuestionsActivity.countMap.toString());
                //LessonCompletedPopup lessonCompletedPopup = new LessonCompletedPopup();
                //lessonCompletedPopup.showPopupWindow(view, activity);
                QuestionsActivity.score.setSpentTime(QuestionsActivity.tvTimer.getText().toString());
                cf.postLesson(isEvaluation,view, activity, quesNo, QuestionsActivity.tvTimer.getText().toString(),QuestionsActivity.scoreDetailsList);
                System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(QuestionsActivity.score));
            } else {

                lessonCompletedPopup.showPopupWindow(view, activity, null, quesNo, QuestionsActivity.tvTimer.getText().toString());
            }

        } else {
            QuestionsActivity.countMap.put(String.valueOf(quesNo), String.valueOf(attemptCount));
            Log.d("attemptCount", QuestionsActivity.countMap.toString());
            QuestionsActivity.mPager.setCurrentItem(QuestionsActivity.mPager.getCurrentItem() + 1);
        }
        System.out.println("Pmark " + QuestionsActivity.Pmark + "Nmark " + QuestionsActivity.Nmark + "OutOfTotal " + QuestionsActivity.OutOfTotal);
        mediaPlayer.release();
        popupWindow.dismiss();
        cf.isCheckImageQuestion = true;
    }

    private void answerWrong(CommonFunction cf, Activity activity, int[] imageViewIds, String[] imagePaths, View view, ImageView ansImageView, int attemptCount, Question question, boolean isLast, boolean isSpeech, int quesNo, LessonCompletedPopup lessonCompletedPopup, PopupWindow popupWindow, boolean isEvaluation) {
        if (ansImageView != null) {
            if (!isEvaluation)
                cf.setShuffleImages(activity, imageViewIds, imagePaths, view);
            ansImageView.setPadding(0, 0, 0, 0);
            Log.d("test", "1");
        }
        Log.d("test answerWrong isLast", String.valueOf(isLast));
        if(isEvaluation){
            QuestionsActivity.addEvaluationScore(question.getChapterNo(),0,1);
            if(isLast){
                QuestionsActivity.score.setSpentTime(QuestionsActivity.tvTimer.getText().toString());
                cf.postLesson(isEvaluation,view, activity, quesNo, QuestionsActivity.tvTimer.getText().toString(),QuestionsActivity.scoreDetailsList);
            }else {
                QuestionsActivity.mPager.setCurrentItem(QuestionsActivity.mPager.getCurrentItem() + 1);

            }

        }

        if (attemptCount == 2 && isSpeech && !isEvaluation) {
            Log.d("test", "2");
            QuestionsActivity.CalculateMarks(0, question.getMinusMark(), question.getPlusMark());
            if (isLast) {
                Log.d("test", "3");
                if (!QuestionsActivity.isRandomQuestion) {
                    Log.d("test", "4");
                    QuestionsActivity.countMap.put(String.valueOf(quesNo), "n");
                    QuestionsActivity.score.setAttemptcount(QuestionsActivity.countMap);
                    QuestionsActivity.score.setCompletedques(String.valueOf(quesNo));
                    Log.d("attemptCount", QuestionsActivity.countMap.toString());
                            /*LessonCompletedPopup lessonCompletedPopup = new LessonCompletedPopup();
                            lessonCompletedPopup.showPopupWindow(view, activity);*/
                    QuestionsActivity.score.setSpentTime(QuestionsActivity.tvTimer.getText().toString());
                    cf.postLesson(isEvaluation, view, activity, quesNo, QuestionsActivity.tvTimer.getText().toString(), QuestionsActivity.scoreDetailsList);
                    System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(QuestionsActivity.score));
                } else {
                    Log.d("test", "5");
                    lessonCompletedPopup.showPopupWindow(view, activity, null, quesNo, QuestionsActivity.tvTimer.getText().toString());
                }
            } else {
                Log.d("test", "6");
                QuestionsActivity.countMap.put(String.valueOf(quesNo), "n");
                Log.d("attemptCount", QuestionsActivity.countMap.toString());
                QuestionsActivity.mPager.setCurrentItem(QuestionsActivity.mPager.getCurrentItem() + 1);

            }
        } else {
            Log.d("test", "7");
            QuestionsActivity.CalculateMarks(0, question.getMinusMark(), 0);

        }
        Log.d("test", "9");
        System.out.println("Pmark " + QuestionsActivity.Pmark + "Nmark " + QuestionsActivity.Nmark + "OutOfTotal " + QuestionsActivity.OutOfTotal);
        mediaPlayer.release();
        popupWindow.dismiss();
        cf.isCheckImageQuestion = true;
    }


}
