package com.bazinga.lantoonnew;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bazinga.lantoonnew.home.chapter.lesson.ChapterCompletedPopup;
import com.bazinga.lantoonnew.login.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

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
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        //Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();
                        sessionManager.checkLogin(token);
                    }
                });
       // sessionManager.checkLogin();

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