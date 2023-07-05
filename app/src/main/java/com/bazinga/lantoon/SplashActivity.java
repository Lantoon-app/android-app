package com.bazinga.lantoon;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bazinga.lantoon.home.chapter.lesson.ChapterCompletedPopup;
import com.bazinga.lantoon.login.SessionManager;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashActivity extends AppCompatActivity {
    CommonFunction cf;
    SessionManager sessionManager;
    AppUpdateManager mAppUpdateManager;
    int RC_APP_UPDATE = 11111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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
                //checkLogin();
            }
        });

        checkAppUpdate();
        if (ApiClient.isTest)
            checkLogin();

    }

    private void checkAppUpdate() {
        mAppUpdateManager = AppUpdateManagerFactory.create(this);
        // mAppUpdateManager.registerListener(installStateUpdatedListener);
        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(result, AppUpdateType.IMMEDIATE, SplashActivity.this
                                , RC_APP_UPDATE);

                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                } else {
                    checkLogin();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("code result", String.valueOf(resultCode));
        Log.d("code req", String.valueOf(requestCode));
        /* we can check without requestCode == RC_APP_UPDATE because
    we known exactly there is only requestCode from  startUpdateFlowForResult() */
        if (requestCode == RC_APP_UPDATE && resultCode != RESULT_OK) {
            //Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            finishAffinity();
        }
        if (requestCode == RC_APP_UPDATE && resultCode == RESULT_OK) {
            //Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            checkLogin();
        }
    }

    private void checkLogin() {
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
    }
    /* private InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
         @Override
         public void onStateUpdate(InstallState state) {
             if (state.installStatus() == InstallStatus.DOWNLOADED) {
                 showCompletedUpdate();
             }
             if (state.installStatus() == InstallStatus.CANCELED) {

             }
         }
     };

     private void showCompletedUpdate() {
         Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "New app is ready!",
                 Snackbar.LENGTH_INDEFINITE);
         snackbar.setAction("Install", new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 mAppUpdateManager.completeUpdate();
             }
         });
         snackbar.show();

     }

      @Override
      protected void onStop() {
          if (mAppUpdateManager != null)
              mAppUpdateManager.unregisterListener(installStateUpdatedListener);
          super.onStop();
      }*/

}