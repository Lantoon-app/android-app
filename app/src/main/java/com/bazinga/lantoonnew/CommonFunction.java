package com.bazinga.lantoonnew;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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

import com.bazinga.lantoonnew.home.HomeActivity;
import com.bazinga.lantoonnew.home.chapter.lesson.ChapterCompletedPopup;
import com.bazinga.lantoonnew.home.chapter.lesson.LessonCompletedPopup;
import com.bazinga.lantoonnew.home.chapter.lesson.QuestionRightWrongPopup;
import com.bazinga.lantoonnew.home.chapter.lesson.QuestionsActivity;
import com.bazinga.lantoonnew.home.chapter.lesson.model.PostLessonResponse;
import com.bazinga.lantoonnew.home.chapter.lesson.model.Question;
import com.bazinga.lantoonnew.home.payment.model.PurchaseResponse;
import com.bazinga.lantoonnew.login.SessionManager;
import com.bazinga.lantoonnew.login.ui.login.LoggedInUserView;
import com.bazinga.lantoonnew.retrofit.ApiClient;
import com.bazinga.lantoonnew.retrofit.ApiInterface;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;

import com.google.gson.GsonBuilder;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import me.ibrahimsn.lib.CirclesLoadingView;
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
        //requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
        Glide.with(activity).load(filePath).apply(requestOptions).into(imageView);

    }

    public void setImageBorder(View imageView, int padding, Drawable drawable) {
       // if (drawable != null)
        imageView.setBackground(drawable);
        imageView.setPadding(padding, padding, padding, padding);
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
                    requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(40));
                    iv.setTag(imagePaths[next]);
                    //Glide.with(activity).load(imagePaths[next]).apply(requestOptions).into(iv);
                    Glide.with(activity).load(imagePaths[next]).into(iv);
                    break;
                }
            }
        }

    }

    public void checkQuestion(ImageView ansImageView, int quesNo, int totalQues, View view, Activity activity, int[] imageViewIds, String[] imagePaths, Question question, Audio audio, PlayPauseView btnAudio) {
        if (isCheckImageQuestion) {
            String tag = ansImageView.getTag().toString();
            isCheckImageQuestion = false;
            attemptCount++;
            QuestionRightWrongPopup qrwp = new QuestionRightWrongPopup();
            if (CheckAnswerImage(tag)) {
                if (quesNo == totalQues) {

                    qrwp.showPopup(activity, view, CheckAnswerImage(tag), true, quesNo, attemptCount, false, question, audio, btnAudio, ansImageView, imageViewIds, imagePaths, view);

                } else {
                    qrwp.showPopup(activity, view, CheckAnswerImage(tag), false, quesNo, attemptCount, false, question, audio, btnAudio, ansImageView, imageViewIds, imagePaths, view);
                }

            } else {
                qrwp.showPopup(activity, view, CheckAnswerImage(tag), false, quesNo, attemptCount, false, question, audio, btnAudio, ansImageView, imageViewIds, imagePaths, view);
                //setShuffleImages(activity, imageViewIds, imagePaths, view);

            }
            System.out.println("attemptCount " + attemptCount);
        }

    }

    public boolean CheckAnswerImage(String imagePath) {
        if (imagePath.contains("right"))
            return true;
        else return false;
    }

    public void speechToText(Context context, TextView textView, CirclesLoadingView circlesLoadingView, String answerWord, boolean isLastQuestion, View view, Activity activity, int quesNo, Question question, Audio audio, PlayPauseView btnAudio) {

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
                textView.setHint("");
                circlesLoadingView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                circlesLoadingView.setVisibility(View.GONE);
                textView.setHint("Matching...");
            }

            @Override
            public void onError(int i) {
                circlesLoadingView.setVisibility(View.GONE);
                textView.setHint("Tap & Speak again");
            }

            @Override
            public void onResults(Bundle bundle) {
                //micButton.setImageResource(R.drawable.ic_mic_black_off);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                String output = data.get(0).substring(0, 1).toUpperCase() + data.get(0).substring(1).toLowerCase();
                textView.setText(output);
                attemptCount++;
                System.out.println("p3-answer " + answerWord);
                System.out.println("p3-data " + output.toLowerCase());
                byte[] asciiData = output.toLowerCase().getBytes(StandardCharsets.US_ASCII);
                String asciiDataString = Arrays.toString(asciiData);
                System.out.println(asciiDataString);

                byte[] asciiAnswerData = answerWord.toLowerCase().getBytes(StandardCharsets.US_ASCII);
                String asciiAnswerDataString = Arrays.toString(asciiAnswerData);
                System.out.println(asciiAnswerDataString);

                if (asciiAnswerDataString.equals(asciiDataString)) {

                    qrwp.showPopup(activity, view, true, isLastQuestion, quesNo, attemptCount, true, question, audio, btnAudio, null, null, null, view);

                } else {
                    qrwp.showPopup(activity, view, false, isLastQuestion, quesNo, attemptCount, true, question, audio, btnAudio, null, null, null, view);
                }

                /*if (answerWord.equals(data.get(0))) {

                    qrwp.showPopup(activity, view, true, isLastQuestion, quesNo, attemptCount, true, pMark, nMark);

                } else {
                    qrwp.showPopup(activity, view, false, isLastQuestion, quesNo, attemptCount, true, pMark, nMark);
                }*/
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
        showExitPopup(view, activity);
    }

    public void showExitPopup(View view, Activity activity) {
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_home_exit, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = false;

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
            CommonProgressPopup commonProgressPopup = new CommonProgressPopup();
            commonProgressPopup.showPopupWindow(view, activity);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<PostLessonResponse> call = apiInterface.scoreUpdate(QuestionsActivity.score);
            call.enqueue(new Callback<PostLessonResponse>() {
                @Override
                public void onResponse(Call<PostLessonResponse> call, Response<PostLessonResponse> response) {
                    commonProgressPopup.dismiss();
                    if (response.body() != null) {
                        Log.d("response postLesson", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));

                        if (response.body().getStatus().getCode() == 1011 || response.body().getStatus().getCode() == 1012) {
                            QuestionsActivity.timerHandler.removeCallbacks(QuestionsActivity.timerRunnable);

                            QuestionsActivity.tvTimer.setVisibility(View.INVISIBLE);
                            if (response.body().getContinuenext().getLessonno() == 1) {
                                ChapterCompletedPopup chapterCompletedPopup = new ChapterCompletedPopup();
                                chapterCompletedPopup.showPopupWindow(view, activity, response.body(), quesNo, strTimeSpent);
                            } else {
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

    public static void permissionNeededAlert(Context context, Activity activity, String message) {
        //Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
        //If User was asked permission before and denied
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle("Permission needed");
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("Open Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(),
                        null);
                intent.setData(uri);
                activity.startActivity(intent);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("onClick: Cancelling", "onClick: Cancelling");
            }
        });

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
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

    public static void printServerResponse(String tag, Object obj) {
        Log.d(tag, new GsonBuilder().setPrettyPrinting().create().toJson(obj));
    }

    public static void postPaymentPurchaseDetails(Context context, Activity activity, String title, String message, String status, String transaction_id, String package_id, String user_id, String language, String total_amount, String paid_amount, String payment_type, String chapters_unlocked, String duration_in_days) {
        if (NetworkUtil.getConnectivityStatus(context) != 0) {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<PurchaseResponse> call = apiInterface.postPaymentPurchaseDetails(transaction_id, package_id, user_id, language, total_amount, paid_amount, payment_type, chapters_unlocked, duration_in_days);
            call.enqueue(new Callback<PurchaseResponse>() {
                @Override
                public void onResponse(Call<PurchaseResponse> call, Response<PurchaseResponse> response) {
                    printServerResponse("Purchase response", response.body());
                    if (response.body().getStatus().getCode() == 1059)
                        paymentAlert(context, activity, title, message + "\n Transation Id - " + transaction_id);
                }

                @Override
                public void onFailure(Call<PurchaseResponse> call, Throwable t) {
                    paymentAlert(context, activity, "Alert", t.getMessage());
                }
            });
        } else
            netWorkErrorAlert(activity);
    }

    public static void paymentAlert(Context context, Activity activity, String titile, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //Setting message manually and performing action on button click
        builder.setTitle(titile);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //PayUActivity.startHomeActivity();
                        activity.finish();
                        //activity.startActivityForResult(new Intent(context,HomeActivity.class),2);
                        activity.startActivity(new Intent(context, HomeActivity.class));
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void storeUserData(SessionManager sessionManager, LoggedInUserView model) {
        //loadingProgressBar.setVisibility(View.GONE);
        String user = new GsonBuilder().create().toJson(model.getloginData());
        String picture = model.getloginData().getPhoto();

        sessionManager.createLoginSession(user);
        sessionManager.setProfilePic(picture);
        sessionManager.setUid(model.getloginData().getUid());
        sessionManager.setUserName(model.getloginData().getUname());
        sessionManager.setEmailId(model.getloginData().getEmail());
        sessionManager.setRegionCode(model.getloginData().getRegionCode());
        sessionManager.setRegistrationType(model.getloginData().getRegistrationtype());

        sessionManager.setLearnLangId(model.getloginData().getLearnlangId());
        sessionManager.setLearnLangName(model.getloginData().getLearnlangObj().getLanguageName());
        sessionManager.setLearnLangNativeName(model.getloginData().getLearnlangObj().getNativeName());

        sessionManager.setKnownLangId(model.getloginData().getKnownlangId());
        sessionManager.setKnownLangName(model.getloginData().getKnownlangObj().getLanguageName());
        sessionManager.setKnownLangNativeName(model.getloginData().getKnownlangObj().getNativeName());

        sessionManager.setSpeakCode(model.getloginData().getSpeakCode());
    }
}
