package com.bazinga.lantoon;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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
    public void playAudio(String fileLocation) {
        try {
            Log.d("audio Location",fileLocation);
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(fileLocation);//Write your location here
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void playAudioSeq(String fileLocation){
        try {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(fileLocation);
        mediaPlayer.prepare();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer player) {
                player.stop();

                // play next audio file

            }

        });
        mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void playAudioSlow(Context context,String fileLocation){
        SoundPool soundPool;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
       // SoundPool soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        Integer sound = soundPool.load(fileLocation,1);
        //Integer sound2 = soundPool.load(this, R.raw.file2, 1);
        //playSound(sound1);


            AudioManager mgr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            float volume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC)
                    / mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            soundPool.play(sound, volume, volume, 1, -1, 1.0f);


    }
    public int percent(int a, int b)
    {
        float result = 0;
        result = ((a/b) * 100);

        return (int)result;
    }
    public void shakeAnimation(View view,int duration){
        ObjectAnimator
                .ofFloat(view, "translationX", 0, 25, -25, 25, -25,15, -15, 6, -6, 0)
                .setDuration(duration)
                .start();
    }

}
