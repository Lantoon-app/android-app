package com.bazinga.lantoon;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class Audio {
    public SoundPool soundPool;

    public void playAudioFile(String path){

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(sampleId, 1, 1, 1, 0, 1.0f);
            }
        });
        *//*File file = new File(path);
        File[] files = file.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String filename) {

                return filename.contains(".mp3");
            }
        });*//*
        soundPool.load(path, 1);*/

        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
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

    public void playAudioSlow(String path) {
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
                soundPool.play(sampleId, 1, 1, 1, 0, 0.8f);
            }
        });
        /*File file = new File(path);
        File[] files = file.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String filename) {

                return filename.contains(".mp3");
            }
        });*/
        soundPool.load(path, 1);
    }

    public void closeSound() {
        soundPool.release();
        soundPool = null;
    }
}
