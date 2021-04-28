package com.bazinga.lantoon;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bazinga.lantoon.login.SessionManager;

public class SplashActivity extends AppCompatActivity {
    CommonFunction cf;
    SessionManager sessionManager;
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cf = new CommonFunction();
        cf.fullScreen(getWindow());
        setContentView(R.layout.activity_splash);
        sessionManager = new SessionManager(SplashActivity.this);
        //sessionManager.checkLogin();
         videoView =findViewById(R.id.videoView);

        /*//Creating MediaController
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);

        //specify the location of media file
       // Uri uri=Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/media/1.mp4");

        //Setting MediaController and URI, then starting the videoView
        videoView.setMediaController(mediaController);*/
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" +
                R.raw.video));
        videoView.requestFocus();
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                sessionManager.checkLogin();
            }
        });
    }
}