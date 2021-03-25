package com.bazinga.lantoon.home.chapter.lesson;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
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

public class QuestionsActivity extends AppCompatActivity {
    private static final int MY_PERMISSION_REQUEST_CODE = 1001;
    CommonFunction cf;
    ViewPager2 mPager;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, MY_PERMISSION_REQUEST_CODE);
        setContentView(R.layout.questions_activity);
        cf = new CommonFunction();
        cf.fullScreen(getWindow());
        mPager = findViewById(R.id.view_pager);
        progress = new ProgressDialog(this);
    }

    private void init() {
        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        QuestionsViewModel questionViewModel = new ViewModelProvider(this).get(QuestionsViewModel.class);
        questionViewModel.getProgressTask().observe(this, task -> {

            Log.d("TAG", "onChanged: status " + task.getStatus() + " value: " + task.getValue());
            switch (task.getStatus()) {
                case TaskState.CANCELLED:
                    Toast.makeText(this, "Canceled by user", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                    break;
                case TaskState.RUNNING:
                    progress.show();
                    break;
                case TaskState.STOP:
                    progress.dismiss();
                    break;
                case TaskState.COMPLETED:
                    Log.d("TAG", "Finished");

                    questionViewModel.getQuestionsMutableLiveData().observe(this, fragments -> {

                        MyFragmentPageAdapter mPageAdapter = new MyFragmentPageAdapter(QuestionsActivity.this, fragments);
                        mPager.setAdapter(mPageAdapter);
                        progress.dismiss();
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
}