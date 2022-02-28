package com.bazinga.lantoon.registration;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.Tags;
import com.bazinga.lantoon.home.HomeActivity;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.bazinga.lantoon.registration.model.User;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DurationSelectionActivity extends AppCompatActivity implements View.OnClickListener {
    CommonFunction cf;
    View progressView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duration_selection);
        cf = new CommonFunction();
        cf.fullScreen(getWindow());
        progressView = findViewById(R.id.llProgressBar);
        LinearLayout llCasual = (LinearLayout) findViewById(R.id.llCasual);
        LinearLayout llRegular = (LinearLayout) findViewById(R.id.llRegular);
        LinearLayout llIntense = (LinearLayout) findViewById(R.id.llIntense);
        llCasual.setOnClickListener(this);
        llRegular.setOnClickListener(this);
        llIntense.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llCasual:
                signup(5);
                break;
            case R.id.llRegular:
                signup(10);
                break;
            case R.id.llIntense:
                signup(15);
                break;
        }

    }

    private void signup(@NonNull int intMinsPerDay) {
        //Toast.makeText(DurationSelectionActivity.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
        /**
         Create new user
         **/
        /*User user = new User(
                "nizam@test3.com",
                "nizam",
                "12345678",
                "31",
                "111113232323",
                "",
                "",
                "",
                2,
                3,
                "asdfasdfsdfsdfsdfsdf",
                2,
                "",
                5,
                "google");*/
        progressBar(true);
        User user = new User(getIntent().getStringExtra(Tags.TAG_EMAILID),
                getIntent().getStringExtra(Tags.TAG_USERNAME),
                getIntent().getStringExtra(Tags.TAG_PASSWORD),
                getIntent().getStringExtra(Tags.TAG_COUNTRY_CODE),
                getIntent().getStringExtra(Tags.TAG_PHONE_NUMBER),
                Tags.TAG_REGION,
                Tags.TAG_INSTITUTE,
                Tags.TAG_GROUP_CODE,
                getIntent().getIntExtra(Tags.TAG_LEARN_LANGUAGE,0),
                getIntent().getIntExtra(Tags.TAG_KNOWN_LANGUAGE,0),
                getIntent().getStringExtra(Tags.TAG_DEVICE_ID),
                getIntent().getIntExtra(Tags.TAG_USER_ROLE,0),
                getIntent().getStringExtra(Tags.TAG_CURRENT_LOCATION),
                intMinsPerDay,
                getIntent().getIntExtra(Tags.TAG_REGISTRATION_TYPE,0),
                getIntent().getStringExtra(Tags.TAG_NOTIFICATION_TOKEN),
                "Android", Build.MODEL);
        System.out.println("user data " + new Gson().toJson(user));
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<User> call = apiInterface.createUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(DurationSelectionActivity.this,"Signup Success",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DurationSelectionActivity.this, HomeActivity.class);
                    startActivity(intent);


                    Log.e("response body= ", new Gson().toJson(response.body()));
                } else {
                    Log.e("response message= ", response.message() + response.code());
                }
                progressBar(false);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                call.cancel();
                t.printStackTrace();
                progressBar(false);
                Log.e("response ERROR= ", "" + t.getMessage() + " " + t.getLocalizedMessage());
                Toast.makeText(getApplicationContext(), "" + t.toString(), Toast.LENGTH_LONG).show();
            }
        });


    }

    private void progressBar(boolean visible) {
        if(visible)
            progressView.setVisibility(View.VISIBLE);
        else
            progressView.setVisibility(View.INVISIBLE);
    }
}
