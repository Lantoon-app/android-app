package com.bazinga.lantoon.home.chapter.lesson;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.Utils;

public class QuestionsActivity extends AppCompatActivity {
    private static final int MY_PERMISSION_REQUEST_CODE = 1001;
    //CommonFunction cf;
    public static ViewPager2 mPager;
    ProgressDialog progress;
    public static long startTime;
    public static int totalQues = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, MY_PERMISSION_REQUEST_CODE);
        setContentView(R.layout.questions_activity);
        mPager = findViewById(R.id.view_pager);
        progress = new ProgressDialog(this);
    }

    private void init() {

        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        QuestionsViewModel questionViewModel = new ViewModelProvider(this,
                new QuestionsViewModelFactory(getIntent().getIntExtra(Utils.TAG_LANGUAGE_ID, 0),
                        getIntent().getIntExtra(Utils.TAG_CHAPTER_NO, 0),
                        getIntent().getIntExtra(Utils.TAG_LESSON_NO, 0))).get(QuestionsViewModel.class);
        questionViewModel.getProgressTask().observe(this, task -> {

            Log.d("TAG", "onChanged: status " + task.getStatus() + " value: " + task.getValue());
            switch (task.getStatus()) {
                case TaskState.CANCELLED:
                    Toast.makeText(this, "Canceled by user", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                    break;
                case TaskState.RUNNING:
                    progress.show();
                    startTime = System.currentTimeMillis();
                    break;
                case TaskState.STOP:
                    progress.dismiss();
                    break;
                case TaskState.COMPLETED:
                    Log.d("TAG", "Finished");

                    questionViewModel.getQuestionsMutableLiveData().observe(this, fragments -> {

                        MyFragmentPageAdapter mPageAdapter = new MyFragmentPageAdapter(QuestionsActivity.this, fragments);
                        mPager.setAdapter(mPageAdapter);
                        mPager.clearFocus();
                        progress.dismiss();
                        totalQues = mPageAdapter.getItemCount();
                        startTime = System.currentTimeMillis();
                        /*  //Add a new Fragment to the list with bundle
                        Bundle b = new Bundle();
                        //b.putInt("position", i);
                        String title = "asd";

                        mPager.add(L1Fragment.class,b);
                        mPager.notifyDataSetChanged();*/
                    });
                    break;

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {

            if (requestCode == MY_PERMISSION_REQUEST_CODE)
                init();
            //Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }


    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}