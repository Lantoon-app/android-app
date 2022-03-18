package com.bazinga.lantoon.registration;

import static com.bazinga.lantoon.CommonFunction.storeUserData;

import android.app.Activity;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.Tags;
import com.bazinga.lantoon.home.HomeActivity;
import com.bazinga.lantoon.login.SessionManager;
import com.bazinga.lantoon.login.data.model.LoggedInUserResponse;
import com.bazinga.lantoon.login.ui.login.LoggedInUserView;
import com.bazinga.lantoon.login.ui.login.LoginResult;
import com.bazinga.lantoon.login.ui.login.LoginViewModel;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.bazinga.lantoon.registration.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DurationSelectionActivity extends AppCompatActivity implements View.OnClickListener {
    CommonFunction cf;
    private LoginViewModel loginViewModel;
    SessionManager sessionManager;
    View progressView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duration_selection);
        cf = new CommonFunction();
        cf.fullScreen(getWindow());
        sessionManager = new SessionManager(this);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        progressView = findViewById(R.id.llProgressBar);
        LinearLayout llCasual = (LinearLayout) findViewById(R.id.llCasual);
        LinearLayout llRegular = (LinearLayout) findViewById(R.id.llRegular);
        LinearLayout llIntense = (LinearLayout) findViewById(R.id.llIntense);
        llCasual.setOnClickListener(this);
        llRegular.setOnClickListener(this);
        llIntense.setOnClickListener(this);
        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                progressBar(false);
                if (loginResult == null) {
                    return;
                }
                //loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                //finish();
            }
        });
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
        progressBar(true);
        User user = new User(getIntent().getStringExtra(Tags.TAG_EMAILID),
                getIntent().getStringExtra(Tags.TAG_USERNAME),
                getIntent().getStringExtra(Tags.TAG_PASSWORD),
                getIntent().getStringExtra(Tags.TAG_COUNTRY_CODE),
                getIntent().getStringExtra(Tags.TAG_PHONE_NUMBER),
                getIntent().getStringExtra(Tags.TAG_REGION_CODE),
                Tags.TAG_INSTITUTE,
                Tags.TAG_GROUP_CODE,
                getIntent().getIntExtra(Tags.TAG_LEARN_LANGUAGE, 0),
                getIntent().getIntExtra(Tags.TAG_KNOWN_LANGUAGE, 0),
                getIntent().getStringExtra(Tags.TAG_DEVICE_ID),
                getIntent().getIntExtra(Tags.TAG_USER_ROLE, 0),
                getIntent().getStringExtra(Tags.TAG_CURRENT_LOCATION),
                intMinsPerDay,
                getIntent().getIntExtra(Tags.TAG_REGISTRATION_TYPE, 0),
                getIntent().getStringExtra(Tags.TAG_NOTIFICATION_TOKEN),
                "Android", Build.MODEL);
        System.out.println("user data " + new Gson().toJson(user));
        loginViewModel.register(user);


    }

    private void progressBar(boolean visible) {
        if (visible)
            progressView.setVisibility(View.VISIBLE);
        else
            progressView.setVisibility(View.INVISIBLE);
    }

    private void updateUiWithUser(LoggedInUserView model) {
        storeUserData(sessionManager, model);
        startActivity(new Intent(this, HomeActivity.class));
    }

    private void showLoginFailed(String errorString) {

        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
