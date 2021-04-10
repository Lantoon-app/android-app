package com.bazinga.lantoon.home.chapter.lesson;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.Utils;
import com.bazinga.lantoon.home.chapter.lesson.model.Score;
import com.bazinga.lantoon.login.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class QuestionsActivity extends AppCompatActivity {
    private static final int MY_PERMISSION_REQUEST_CODE = 1001;
    public static Context context;
    CommonFunction cf;
    public static QuestionsViewModel questionViewModel;
    public static SessionManager sessionManager;
    public static ViewPager2 mPager;
    public static ProgressDialog progress;
    public static long startTime;
    public static int totalQues = 0;
    public static String strFilePath = "";
    public static Score score;
    public static int Pmark = 0,Nmark = 0,OutOfTotal = 0;
    public static Map<String, String> countMap = new HashMap<>();
    public static String strUserId,strTotalQues, strCompletedQues;


    //public static String strUserId,strLanguageId,strChapterNo,strLessonNo,strStartTime,strEndTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        strFilePath = getCacheDir().getPath();
        context = this;
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, MY_PERMISSION_REQUEST_CODE);
        sessionManager = new SessionManager(this);
        strUserId = sessionManager.getUserDetails().getUid();
        cf= new CommonFunction();
        cf.fullScreen(getWindow());
        setContentView(R.layout.activity_questions);
        mPager = findViewById(R.id.view_pager);
        progress = new ProgressDialog(this);
    }

    private void init(int langid, int chaperno, int lessonno) {

        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        /*strUserId = sessionManager.getUserDetails().getUid();
        strLanguageId = String.valueOf(getIntent().getIntExtra(Utils.TAG_LANGUAGE_ID,0));
        strChapterNo = String.valueOf(getIntent().getIntExtra(Utils.TAG_CHAPTER_NO,0));
        strLessonNo = String.valueOf(getIntent().getIntExtra(Utils.TAG_LESSON_NO,0));*/

        questionViewModel = new ViewModelProvider(this,
                new QuestionsViewModelFactory(langid,chaperno,lessonno)).get(QuestionsViewModel.class);
        questionViewModel.getProgressTask().observe(this, task -> {

            Log.d("TAG", "onChanged: status " + task.getStatus() + " value: " + task.getValue());
            switch (task.getStatus()) {
                case TaskState.CANCELLED:
                    Toast.makeText(context, "Canceled by user", Toast.LENGTH_SHORT).show();
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

                    questionViewModel.getQuestionsMutableLiveData().observe(QuestionsActivity.this, fragments -> {

                        MyFragmentPageAdapter mPageAdapter = new MyFragmentPageAdapter(QuestionsActivity.this, fragments);
                        //mPager.setUserInputEnabled(false);
                        mPager.setAdapter(mPageAdapter);
                        //mPager.setUserInputEnabled(false);
                        mPager.clearFocus();
                        progress.dismiss();
                        totalQues = mPageAdapter.getItemCount();
                        startTime = System.currentTimeMillis();
                        score = new Score();
                        score.setUid(sessionManager.getUserDetails().getUid());
                        score.setLangid(String.valueOf(langid));
                        score.setChaptno(String.valueOf(chaperno));
                        score.setLessonno(String.valueOf(lessonno));
                        score.setTotalques(String.valueOf(totalQues));
                        score.setGcode("LANENG");
                        score.setSpentTime("10:30:22");
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
                init(getIntent().getIntExtra(Utils.TAG_LANGUAGE_ID, 0),
                        getIntent().getIntExtra(Utils.TAG_CHAPTER_NO, 0),
                        getIntent().getIntExtra(Utils.TAG_LESSON_NO, 0));
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

    public static void CalculateMarks(int pmark,int nmark,int defaultPMark){
        Pmark = Pmark+pmark;
        Nmark = Nmark+nmark;
        OutOfTotal = OutOfTotal+defaultPMark;
        score.setPmark(String.valueOf(Pmark));
        score.setNmark(String.valueOf(Nmark));
        score.setOutOfTotal(String.valueOf(OutOfTotal));

    }

}