package com.bazinga.lantoon.login.ui.login;

import android.app.Activity;

import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.NetworkUtil;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.Tags;
import com.bazinga.lantoon.ValidationFunction;
import com.bazinga.lantoon.home.HomeActivity;
import com.bazinga.lantoon.login.SessionManager;
import com.bazinga.lantoon.login.forget.ForgetPasswordActivity;
import com.google.gson.GsonBuilder;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    SessionManager sessionManager;
     EditText usernameEditText, passwordEditText;
     Button loginButton;
     ProgressBar loadingProgressBar;
     TextView tvForgetPassword;
     LinearLayout lllogin;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(this);
        lllogin= findViewById(R.id.lllogin);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.pbLoading);
        tvForgetPassword = findViewById(R.id.tvForgetPassword);
        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            }
        });

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
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

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(), getIntent().getStringExtra(Tags.TAG_DEVICE_ID), "");
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //lllogin.setVisibility(View.INVISIBLE);
                if (NetworkUtil.getConnectivityStatus(LoginActivity.this) != 0) {
                    if (!ValidationFunction.isEmpty(usernameEditText) || ValidationFunction.isEmpty(passwordEditText)) {
                        loadingProgressBar.setVisibility(View.VISIBLE);
                        loginViewModel.login(usernameEditText.getText().toString(),
                                passwordEditText.getText().toString(), getIntent().getStringExtra(Tags.TAG_DEVICE_ID),getIntent().getStringExtra(Tags.TAG_NOTIFICATION_TOKEN));
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.empty_username_password, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    CommonFunction.netWorkErrorAlert(LoginActivity.this);
                }
            }
        });
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBack();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    private void onBack() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.ad_home_button_pressed_msg))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle("Alert");
        alert.show();
    }

    private void updateUiWithUser(LoggedInUserView model) {
        loadingProgressBar.setVisibility(View.GONE);
        String user = new GsonBuilder().create().toJson(model.getloginData());
        String picture = model.getloginData().getPhoto();

        sessionManager.createLoginSession(user);
        sessionManager.setProfilePic(picture);
        sessionManager.setUid(model.getloginData().getUid());
        sessionManager.setUserName(model.getloginData().getUname());
        sessionManager.setLearnLang(model.getloginData().getLearnlang());
        sessionManager.setKnownLang(model.getloginData().getKnownlang());
        sessionManager.setSpeakCode(model.getloginData().getSpeakCode());
        /*String welcome = getString(R.string.welcome) + model.getloginData().getUname();
        // TODO : initiate successful logged in experience
        Toast.makeText(this, welcome, Toast.LENGTH_LONG).show();*/
        System.out.println("Login username "+sessionManager.getUserName());
        startActivity(new Intent(this, HomeActivity.class));
    }

    private void showLoginFailed(String errorString) {
        loadingProgressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}