package com.bazinga.lantoon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bazinga.lantoon.home.chapter.lesson.ChapterCompletedPopup;
import com.bazinga.lantoon.login.SessionManager;

public class SplashActivity extends AppCompatActivity {
    CommonFunction cf;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cf = new CommonFunction();
        cf.fullScreen(getWindow());
        setContentView(R.layout.activity_splash);
        sessionManager = new SessionManager(SplashActivity.this);
        ImageView imageView = findViewById(R.id.imageView);
        ImageView ivGif = findViewById(R.id.ivGif);
        ChapterCompletedPopup chapterCompletedPopup = new ChapterCompletedPopup();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //chapterCompletedPopup.showPopupWindow(v,SplashActivity.this,null,0,"0");
            }
        });

        sessionManager.checkLogin();

        /*Glide.with(this).load(R.drawable.gif_baloons).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (resource instanceof GifDrawable) {
                    ((GifDrawable)resource).setLoopCount(1);
                    ((GifDrawable)resource).registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                        @Override
                        public void onAnimationEnd(Drawable drawable) {
                            super.onAnimationEnd(drawable);
                            sessionManager.checkLogin();
                        }
                    });
                }
                return false;
            }


        }).into(ivGif);*/


    }

}