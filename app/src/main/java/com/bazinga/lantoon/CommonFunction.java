package com.bazinga.lantoon;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class CommonFunction {
    Context context;

  /*  public void CommonFunction(Context context) {
        this.context = context;
    }*/

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

    public int percent(int a, int b) {
        float result = 0;
        result = ((a / b) * 100);

        return (int) result;
    }

    public void shakeAnimation(View view, int duration) {
        ObjectAnimator
                .ofFloat(view, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                .setDuration(duration)
                .start();
    }

    public void setImage(Activity activity, String filePath, ImageView imageView) {
        /*File file = new File(folderPath);
        File[] files = file.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String filename) {

                return filename.contains(".jpg");
            }
        });*/
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
        Glide.with(activity).load(filePath).apply(requestOptions).into(imageView);

    }

    public void setShuffleImages(Activity activity, int[] imageViewIds, String[] imagePaths, View view) {
        /*File file = new File(wrongImgFolderPath);
        File[] wrongImageFile = file.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String filename) {

                return filename.contains(".jpg");
            }
        });
        ArrayList<File> arrayList = new ArrayList<File>();
        for (int i = 0; i < wrongImageFile.length; i++) {
            arrayList.add(wrongImageFile[i]);
        }
        file = new File(rightImgPaths);
        File[] rightImageFile = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.contains(".jpg");
            }
        });
        arrayList.add(rightImageFile[0]);*/


        Random rng = new Random();
        List<Integer> generated = new ArrayList<Integer>();
        for (int i = 0; i < imageViewIds.length; i++) {
            while (true) {
                Integer next = rng.nextInt(imageViewIds.length);
                if (!generated.contains(next)) {
                    generated.add(next);
                    ImageButton iv = (ImageButton) view.findViewById(imageViewIds[i]);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
                    iv.setTag(imagePaths[next]);
                    Glide.with(activity).load(imagePaths[next]).apply(requestOptions).into(iv);
                    break;
                }
            }
        }

    }

    public boolean CheckAnswerImage(String imagePath) {
        if (imagePath.contains("right"))
            return true;
        else return false;
    }

    public void speechToText(Context context, TextView textView, String answerWord){

        SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);

        //String languagePref = Locale.ENGLISH;
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Locale.ENGLISH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, Locale.ENGLISH);

        /*final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.);
*/
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

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //micButton.setImageResource(R.drawable.ic_mic_black_off);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                textView.setText(data.get(0));
                Log.d("check text", data.get(0));
                Log.d("check text", answerWord);
                /*if(data.get(0).equals(answerWord)) {
                    Log.d("check ok", data.get(0) + " " + answerWord);

                }*/
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        /*micBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    //micBtn.setImageResource(R.drawable.ic_mic_black_24dp);
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });*/

    }

}
