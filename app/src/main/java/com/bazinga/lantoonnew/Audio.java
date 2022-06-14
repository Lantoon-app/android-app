package com.bazinga.lantoonnew;

import android.app.Activity;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

import java.io.IOException;

import static android.content.Context.AUDIO_SERVICE;

public class Audio {
    public SoundPool soundPool;
    public static MediaPlayer mediaPlayer;
    PlayPauseView pV;

    public void playAudioFile(String path) {

        if (mediaPlayer != null)
            mediaPlayer.release();
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void playAudioFileAnim(Activity activity,String path, PlayPauseView playPauseView) {

        if (mediaPlayer != null) {
            mediaPlayer.release();
            if(pV!=null) {
                pV.setState(PlayPauseView.STATE_PAUSE);
                pV.setImageDrawable(activity.getDrawable(R.drawable.anim_vector_play));
            }
        }
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            playPauseView.setState(PlayPauseView.STATE_PLAY);
            pV = playPauseView;
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    playPauseView.setState(PlayPauseView.STATE_PAUSE);
                    pV.setImageDrawable(activity.getDrawable(R.drawable.anim_vector_play));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void playAudioSlow(Activity activity, String path) {
        if (soundPool != null)
            soundPool.release();
        AudioManager audioManager = (AudioManager) activity.getSystemService(AUDIO_SERVICE);
        float actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(100)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(100, AudioManager.STREAM_MUSIC, 0);
        }
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(sampleId, volume, volume, 1, 0, 0.8f);
            }
        });

        soundPool.load(path, 1);
    }

    public void closeSound() {
        soundPool.release();
        soundPool = null;
    }
}
