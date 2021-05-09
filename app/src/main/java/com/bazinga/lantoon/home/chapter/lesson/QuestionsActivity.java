package com.bazinga.lantoon.home.chapter.lesson;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.bazinga.lantoon.Audio;
import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.Tags;
import com.bazinga.lantoon.home.HomeActivity;
import com.bazinga.lantoon.home.chapter.lesson.model.Score;
import com.bazinga.lantoon.login.SessionManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class QuestionsActivity extends AppCompatActivity {
    private static final int MY_PERMISSION_REQUEST_CODE = 1001;
    public static Context context;
    CommonFunction cf;
    public static boolean isNewChapter, isRandomQuestion;
    public static QuestionsViewModel questionViewModel;
    public static SessionManager sessionManager;
    public static ViewPager2 mPager;
    public static TextView tvTimer;
    //public static ProgressDialog progress;
    public static ProgressBar progressBar;
    public static long startTime = 0;
    public static long startLessonTime = 0;
    public static int totalQues;
    public static String strFilePath = "";
    public static Score score;
    public static int Pmark = 0, Nmark = 0, OutOfTotal = 0;
    public static Map<String, String> countMap = new HashMap<>();
    public static String strUserId, strTotalQues, strSpeakCode, strCompletedQues;
    public static Handler timerHandler = new Handler();
    public static Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            //long millis = System.currentTimeMillis() - startTime;
            long millis = (System.currentTimeMillis() - startTime) +startLessonTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            tvTimer.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }

    };


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
        strUserId = sessionManager.getUid();
        strSpeakCode = sessionManager.getSpeakCode();
        cf = new CommonFunction();
        cf.fullScreen(getWindow());
        setContentView(R.layout.activity_questions);
        mPager = findViewById(R.id.view_pager);
        tvTimer = findViewById(R.id.tvTimer);
        tvTimer.setVisibility(View.INVISIBLE);
        progressBar = findViewById(R.id.progressBar);
        //progress = new ProgressDialog(this);
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBack();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    private void onBack() {
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_home_exit, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(this.getCurrentFocus().getRootView(), Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler

        TextView tvMessage = popupView.findViewById(R.id.tvMessage);
        Button btnYes = popupView.findViewById(R.id.btnYes);
        Button btnNo = popupView.findViewById(R.id.btnNo);
        ImageButton imgBtnClose = popupView.findViewById(R.id.imgBtnClose);

        tvMessage.setText(getString(R.string.ad_home_button_pressed_msg));

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (cf.mediaPlayer != null)
                    cf.mediaPlayer.release();
                if (Audio.mediaPlayer != null) {
                    Audio.mediaPlayer.release();
                    Audio.mediaPlayer = null;

                }
                startActivityForResult(new Intent(QuestionsActivity.this, HomeActivity.class), 2);
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        imgBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });


        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                popupWindow.dismiss();
                return true;
            }
        });
    }

    private void init(int langid, int chaperno, int lessonno,String strSpentTime, boolean isNewChapter, boolean isRandomQuestion) {
        Log.d("isRandomQuestionsss",String.valueOf(isRandomQuestion));
        this.isNewChapter = isNewChapter;
        this.isRandomQuestion = isRandomQuestion;
        this.startLessonTime = Long.decode(strSpentTime);

        /*progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(false);*/

        /*strUserId = sessionManager.getUserDetails().getUid();
        strLanguageId = String.valueOf(getIntent().getIntExtra(Utils.TAG_LANGUAGE_ID,0));
        strChapterNo = String.valueOf(getIntent().getIntExtra(Utils.TAG_CHAPTER_NO,0));
        strLessonNo = String.valueOf(getIntent().getIntExtra(Utils.TAG_LESSON_NO,0));*/

        questionViewModel = new ViewModelProvider(this,
                new QuestionsViewModelFactory(langid, chaperno, lessonno, sessionManager.getKnownLang())).get(QuestionsViewModel.class);
        questionViewModel.getProgressTask().observe(this, task -> {

            Log.d("TAG", "onChanged: status " + task.getStatus() + " value: " + task.getValue());
            switch (task.getStatus()) {
                case TaskState.CANCELLED:
                    Toast.makeText(context, "Canceled by user", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    //progress.dismiss();
                    break;
                case TaskState.RUNNING:
                    progressBar.setVisibility(View.VISIBLE);
                    //progress.show();

                    break;
                case TaskState.STOP:

                    break;
                case TaskState.COMPLETED:
                    Log.d("TAG", "Finished");

                    questionViewModel.getQuestionsMutableLiveData().observe(QuestionsActivity.this, fragments -> {

                        MyFragmentPageAdapter mPageAdapter = new MyFragmentPageAdapter(QuestionsActivity.this, fragments);

                        mPager.setAdapter(mPageAdapter);
                        mPager.setUserInputEnabled(false);
                        mPager.setCurrentItem(getIntent().getIntExtra(Tags.TAG_START_QUESTION_NO,1)-1);
                        //mPager.setCurrentItem(5);
                        mPager.clearFocus();

                        totalQues = mPageAdapter.getItemCount();
                        //startTime = System.currentTimeMillis();
                        score = new Score();
                        score.setUid(sessionManager.getUid());
                        score.setLangid(String.valueOf(langid));
                        score.setChaptno(String.valueOf(chaperno));
                        score.setLessonno(String.valueOf(lessonno));
                        score.setTotalques(String.valueOf(totalQues));
                        score.setGcode("LANENG");
                        //score.setSpentTime("10:30:22");
                        /*  //Add a new Fragment to the list with bundle
                        Bundle b = new Bundle();
                        //b.putInt("position", i);
                        String title = "asd";

                        mPager.add(L1Fragment.class,b);
                        mPager.notifyDataSetChanged();*/
                        progressBar.setVisibility(View.GONE);
                        //progress.dismiss();
                        startTime = System.currentTimeMillis();
                        timerHandler.postDelayed(timerRunnable, 0);
                        tvTimer.setVisibility(View.VISIBLE);

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
                init(getIntent().getIntExtra(Tags.TAG_LANGUAGE_ID, 0),
                        getIntent().getIntExtra(Tags.TAG_CHAPTER_NO, 0),
                        getIntent().getIntExtra(Tags.TAG_LESSON_NO, 0),
                        getIntent().getStringExtra(Tags.TAG_SPENT_TIME),
                        getIntent().getBooleanExtra(Tags.TAG_IS_NEW_CHAPTER,false),
                        getIntent().getBooleanExtra(Tags.TAG_IS_RANDOM_QUESTIONS,false));
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

    public static void CalculateMarks(int pmark, int nmark, int defaultPMark) {
        Pmark = Pmark + pmark;
        Nmark = Nmark + nmark;
        OutOfTotal = OutOfTotal + defaultPMark;
        score.setPmark(String.valueOf(Pmark));
        score.setNmark(String.valueOf(Nmark));
        score.setOutOfTotal(String.valueOf(OutOfTotal));

    }

    public static void clearData() {
        startTime = 0;
        totalQues = 0;

        score = new Score();
        Pmark = 0;
        Nmark = 0;
        OutOfTotal = 0;
        countMap.clear();
        strTotalQues = "";
        strCompletedQues = "";

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteCache(getApplicationContext());
    }
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}