package com.bazinga.lantoon;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bazinga.lantoon.home.HomeActivity;
import com.bazinga.lantoon.home.chapter.ChapterCompletedPopup;
import com.bazinga.lantoon.home.chapter.ChapterFragment;
import com.bazinga.lantoon.home.chapter.lesson.LessonCompletedPopup;
import com.bazinga.lantoon.home.chapter.lesson.QuestionRightWrongPopup;
import com.bazinga.lantoon.home.chapter.lesson.QuestionRightWrongPopup;
import com.bazinga.lantoon.home.chapter.lesson.QuestionsActivity;
import com.bazinga.lantoon.home.chapter.lesson.model.PostLessonResponse;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonFunction {

    int attemptCount = 0;
    public static MediaPlayer mediaPlayer;
    public static boolean isCheckImageQuestion = true;

    public void fullScreen(Window window) {
        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = window.getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
        }
    }

    public int percent(double quesNo, double totalQues) {

        // percent
        double per = (quesNo / totalQues) * 100;
        ;

        Log.d("percentage", String.valueOf(Math.round(per)));
        return (int) per;
    }

    public void shakeAnimation(View view, int duration) {
        ObjectAnimator
                .ofFloat(view, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                .setDuration(duration)
                .start();
    }

    public void mikeAnimation(View view, int duration) {
        ObjectAnimator
                .ofFloat(view, "translationY", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                .setDuration(duration)
                .start();

    }

    public void setImage(Activity activity, String filePath, ImageView imageView) {

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
        Glide.with(activity).load(filePath).apply(requestOptions).into(imageView);

    }

    public void setShuffleImages(Activity activity, int[] imageViewIds, String[] imagePaths, View view) {
        Random rng = new Random();
        List<Integer> generated = new ArrayList<Integer>();
        for (int i = 0; i < imageViewIds.length; i++) {
            while (true) {
                Integer next = rng.nextInt(imageViewIds.length);
                if (!generated.contains(next)) {
                    generated.add(next);
                    ImageView iv = view.findViewById(imageViewIds[i]);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
                    iv.setTag(imagePaths[next]);
                    Glide.with(activity).load(imagePaths[next]).apply(requestOptions).into(iv);
                    break;
                }
            }
        }

    }

    public void checkQuestion(String tag, int quesNo, int totalQues, View view, Activity activity, int[] imageViewIds, String[] imagePaths, int pMark, int nMark) {
        if(isCheckImageQuestion) {
            isCheckImageQuestion = false;
            attemptCount++;
            QuestionRightWrongPopup qrwp = new QuestionRightWrongPopup();
            if (CheckAnswerImage(tag)) {
                if (quesNo == totalQues) {

                    qrwp.showPopup(activity, view, CheckAnswerImage(tag), true, quesNo, attemptCount, false, pMark, nMark);

                } else {
                    qrwp.showPopup(activity, view, CheckAnswerImage(tag), false, quesNo, attemptCount, false, pMark, nMark);
                }

            } else {
                qrwp.showPopup(activity, view, CheckAnswerImage(tag), false, quesNo, attemptCount, false, pMark, nMark);
                setShuffleImages(activity, imageViewIds, imagePaths, view);
            }
            System.out.println("attemptCount " + attemptCount);
        }

    }

    public boolean CheckAnswerImage(String imagePath) {
        if (imagePath.contains("right"))
            return true;
        else return false;
    }

    public void speechToText(Context context, TextView textView, String answerWord, boolean isLastQuestion, View view, Activity activity, int quesNo, int pMark, int nMark) {

        SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, QuestionsActivity.strSpeakCode);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, QuestionsActivity.strSpeakCode);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, QuestionsActivity.strSpeakCode);

        QuestionRightWrongPopup qrwp = new QuestionRightWrongPopup();
        speechRecognizer.startListening(speechRecognizerIntent);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                textView.setText("");
                textView.setHint("Listening...");

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                textView.setHint("Checking...");
            }

            @Override
            public void onError(int i) {
                textView.setHint("Touch to Speak again");
            }

            @Override
            public void onResults(Bundle bundle) {
                //micButton.setImageResource(R.drawable.ic_mic_black_off);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                String output = data.get(0).substring(0, 1).toUpperCase() + data.get(0).substring(1).toLowerCase();
                textView.setText(output);
                attemptCount++;
                if (answerWord.equals(data.get(0))) {

                    qrwp.showPopup(activity, view, true, isLastQuestion, quesNo, attemptCount, true, pMark, nMark);

                } else {
                    qrwp.showPopup(activity, view, false, isLastQuestion, quesNo, attemptCount, true, pMark, nMark);
                }
                Log.d("attemptCount", QuestionsActivity.countMap.toString());

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

    }

    public void onClickHomeButton(View view, final Activity activity, int quesNo) {
        //mediaClose();
        showExitPopup(view, activity);
       /* //Uncomment the below code to Set the message and title from the strings.xml file
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        //Setting message manually and performing action on button click
        builder.setMessage(activity.getString(R.string.ad_home_button_pressed_msg))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        *//*QuestionsActivity.timerHandler.removeCallbacks(QuestionsActivity.timerRunnable);
                        QuestionsActivity.tvTimer.setVisibility(View.INVISIBLE);
                        QuestionsActivity.clearData();*//*
                        activity.finish();
                        *//*if (quesNo == 1) {
                            activity.finish();
                        }
                        QuestionsActivity.tvTimer.setVisibility(View.INVISIBLE);
                        Log.d("ssssss", String.valueOf(QuestionsActivity.isNewChapter));
                        if (QuestionsActivity.isNewChapter) {
                            if (attemptCount != 0) {
                                QuestionsActivity.countMap.put(String.valueOf(quesNo), String.valueOf(attemptCount));
                                QuestionsActivity.score.setAttemptcount(QuestionsActivity.countMap);
                                QuestionsActivity.score.setCompletedques(String.valueOf(quesNo));
                            } else {
                                QuestionsActivity.score.setAttemptcount(QuestionsActivity.countMap);
                                QuestionsActivity.score.setCompletedques(String.valueOf(quesNo - 1));
                            }

                            if (quesNo == 1) {
                                QuestionsActivity.CalculateMarks(0, 0, 0);
                            } else if (quesNo == 2 && QuestionsActivity.OutOfTotal == 0) {
                                QuestionsActivity.CalculateMarks(0, 0, 0);
                            }
                            Log.d("attemptCount", QuestionsActivity.countMap.toString());

                            QuestionsActivity.score.setSpentTime(timeSpent);
                            postLesson(view, activity, quesNo, QuestionsActivity.tvTimer.getText().toString());
                            System.out.println(new

                                    GsonBuilder().

                                    setPrettyPrinting().

                                    create().

                                    toJson(QuestionsActivity.score));
                        } else {
                            activity.finish();
                        }*//*

                    }
                })
                .

                        setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //QuestionsActivity.tvTimer.setVisibility(View.INVISIBLE);
                                dialog.cancel();
                                dialog.dismiss();
                            }
                        });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Alert");
        alert.show();*/
    }

    public void showExitPopup(View view, Activity activity) {
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_home_exit, null);

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

        TextView tvMessage = popupView.findViewById(R.id.tvMessage);
        Button btnYes = popupView.findViewById(R.id.btnYes);
        Button btnNo = popupView.findViewById(R.id.btnNo);
        ImageButton imgBtnClose = popupView.findViewById(R.id.imgBtnClose);

        tvMessage.setText(activity.getString(R.string.ad_home_button_pressed_msg));

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                mediaClose();
                activity.startActivityForResult(new Intent(activity, HomeActivity.class), 2);
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        imgBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });


        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void postLesson(View view, Activity activity, int quesNo, String strTimeSpent) {

        if (NetworkUtil.getConnectivityStatus(activity) != 0) {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<PostLessonResponse> call = apiInterface.scoreUpdate(QuestionsActivity.score);
            call.enqueue(new Callback<PostLessonResponse>() {
                @Override
                public void onResponse(Call<PostLessonResponse> call, Response<PostLessonResponse> response) {

                    if (response.body() != null) {
                        Log.d("response postLesson", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));

                        if (response.body().getStatus().getCode() == 1011 || response.body().getStatus().getCode() == 1012) {
                            QuestionsActivity.timerHandler.removeCallbacks(QuestionsActivity.timerRunnable);

                            QuestionsActivity.tvTimer.setVisibility(View.INVISIBLE);
                            if(response.body().getContinuenext().getLessonno() == 1) {
                                ChapterCompletedPopup chapterCompletedPopup = new ChapterCompletedPopup();
                                chapterCompletedPopup.showPopupWindow(view,activity, response.body(), quesNo, strTimeSpent);
                            }else {
                                LessonCompletedPopup lessonCompletedPopup = new LessonCompletedPopup();
                                lessonCompletedPopup.showPopupWindow(view, activity, response.body(), quesNo, strTimeSpent);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<PostLessonResponse> call, Throwable t) {
                    Log.e("response postLesson", t.getMessage());
                }
            });

        } else {
            netWorkErrorAlert(activity);
            activity.startActivityForResult(new Intent(activity, HomeActivity.class), 2);
        }
    }

    public static void netWorkErrorAlert(Activity activity) {
        Snackbar.make(activity.getCurrentFocus().getRootView(), activity.getString(R.string.msg_network_failed), Snackbar.LENGTH_SHORT).show();
    }

    public static void noDataSnackBar(Activity activity) {
        Snackbar.make(activity.getCurrentFocus().getRootView(), activity.getString(R.string.msg_network_failed), Snackbar.LENGTH_SHORT).show();
    }

    public void wentWorngToast(Context context) {
        Toast.makeText(context, "Something went wrong, Try again later", Toast.LENGTH_LONG);
    }

    public static void mediaClose() {
        if (mediaPlayer != null)
            mediaPlayer.release();
        if (Audio.mediaPlayer != null) {
            Audio.mediaPlayer.release();
            Audio.mediaPlayer = null;
        }
    }
}
