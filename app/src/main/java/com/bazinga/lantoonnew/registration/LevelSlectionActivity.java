package com.bazinga.lantoonnew.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bazinga.lantoonnew.CommonFunction;
import com.bazinga.lantoonnew.R;
import com.bazinga.lantoonnew.Tags;

public class LevelSlectionActivity extends AppCompatActivity {
    LinearLayout llNew, llTest;
    CommonFunction cf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selction);
        cf = new CommonFunction();
        cf.fullScreen(getWindow());

        llNew = (LinearLayout) findViewById(R.id.llNew);
        llTest = (LinearLayout) findViewById(R.id.llTest);

        llNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextDurationSelection(1);
            }
        });
        llTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextDurationSelection(2);
            }
        });


    }

    private void nextDurationSelection (int rollId){


        Intent intentDuration = new Intent(LevelSlectionActivity.this, DurationSelectionActivity.class);
        intentDuration.putExtra(Tags.TAG_EMAILID, getIntent().getStringExtra(Tags.TAG_EMAILID));
        intentDuration.putExtra(Tags.TAG_USERNAME, getIntent().getStringExtra(Tags.TAG_USERNAME));
        intentDuration.putExtra(Tags.TAG_PASSWORD, getIntent().getStringExtra(Tags.TAG_PASSWORD));
        intentDuration.putExtra(Tags.TAG_COUNTRY_CODE, getIntent().getStringExtra(Tags.TAG_COUNTRY_CODE));
        intentDuration.putExtra(Tags.TAG_PHONE_NUMBER, getIntent().getStringExtra(Tags.TAG_PHONE_NUMBER));
        intentDuration.putExtra(Tags.TAG_LEARN_LANGUAGE, getIntent().getIntExtra(Tags.TAG_LEARN_LANGUAGE,0));
        intentDuration.putExtra(Tags.TAG_KNOWN_LANGUAGE, getIntent().getIntExtra(Tags.TAG_KNOWN_LANGUAGE,0));
        intentDuration.putExtra(Tags.TAG_DEVICE_ID, getIntent().getStringExtra(Tags.TAG_DEVICE_ID));
        intentDuration.putExtra(Tags.TAG_USER_ROLE, rollId);
        intentDuration.putExtra(Tags.TAG_CURRENT_LOCATION, getIntent().getStringExtra(Tags.TAG_CURRENT_LOCATION));
        intentDuration.putExtra(Tags.TAG_REGION_CODE, getIntent().getStringExtra(Tags.TAG_REGION_CODE));
        intentDuration.putExtra(Tags.TAG_REGISTRATION_TYPE,getIntent().getIntExtra(Tags.TAG_REGISTRATION_TYPE,0));
        intentDuration.putExtra(Tags.TAG_NOTIFICATION_TOKEN, getIntent().getStringExtra(Tags.TAG_NOTIFICATION_TOKEN));
        startActivity(intentDuration);

    }


}
