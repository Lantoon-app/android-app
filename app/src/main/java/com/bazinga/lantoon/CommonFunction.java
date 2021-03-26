package com.bazinga.lantoon;

import android.animation.ObjectAnimator;
import android.app.Activity;
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
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
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

    public void setImagefromLocalFolder(Activity activity, String folderPath, ImageView imageView) {
        File file = new File(folderPath);
        File[] files = file.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String filename) {

                return filename.contains(".jpg");
            }
        });
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
        Glide.with(activity).load(files[0]).apply(requestOptions).into(imageView);

    }

    public void setShuffleImages(Activity activity, int[] imageViewIds, String rightImgPaths, String wrongImgFolderPath, View view) {
        File file = new File(wrongImgFolderPath);
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
        arrayList.add(rightImageFile[0]);


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
                    Glide.with(activity).load(arrayList.get(next)).apply(requestOptions).into(iv);
                    break;
                }
            }
        }

    }

}
