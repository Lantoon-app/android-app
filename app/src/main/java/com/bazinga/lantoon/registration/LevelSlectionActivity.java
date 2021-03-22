package com.bazinga.lantoon.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.Utils;

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
                nextDurationSelection("new");
            }
        });
        llTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextDurationSelection("level");
            }
        });


    }

    private void nextDurationSelection (String strRoll){


        Intent intentDuration = new Intent(LevelSlectionActivity.this, DurationSelectionActivity.class);
        intentDuration.putExtra(Utils.TAG_EMAILID, getIntent().getStringExtra(Utils.TAG_EMAILID));
        intentDuration.putExtra(Utils.TAG_USERNAME, getIntent().getStringExtra(Utils.TAG_USERNAME));
        intentDuration.putExtra(Utils.TAG_PASSWORD, getIntent().getStringExtra(Utils.TAG_PASSWORD));
        intentDuration.putExtra(Utils.TAG_COUNTRY_CODE, getIntent().getStringExtra(Utils.TAG_COUNTRY_CODE));
        intentDuration.putExtra(Utils.TAG_PHONE_NUMBER, getIntent().getStringExtra(Utils.TAG_PHONE_NUMBER));
        intentDuration.putExtra(Utils.TAG_LEARN_LANGUAGE, getIntent().getStringExtra(Utils.TAG_LEARN_LANGUAGE));
        intentDuration.putExtra(Utils.TAG_KNOWN_LANGUAGE, getIntent().getStringExtra(Utils.TAG_KNOWN_LANGUAGE));
        intentDuration.putExtra(Utils.TAG_DEVICE_ID, getIntent().getStringExtra(Utils.TAG_DEVICE_ID));
        intentDuration.putExtra(Utils.TAG_USER_ROLE, strRoll);
        intentDuration.putExtra(Utils.TAG_CURRENT_LOCATION, getIntent().getStringExtra(Utils.TAG_CURRENT_LOCATION));
        intentDuration.putExtra(Utils.TAG_REGISTRATION_TYPE,getIntent().getStringExtra(Utils.TAG_REGISTRATION_TYPE));
        startActivity(intentDuration);

    }


}
