package com.bazinga.lantoon.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.Utils;
import com.bazinga.lantoon.ValidationFunction;
import com.bazinga.lantoon.login.ui.login.LoginActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.hbb20.CountryCodePicker;

import java.util.Arrays;

public class SignupActivity extends AppCompatActivity {

    CommonFunction cf;
    int RC_GOOGLE_SIGN_IN = 1;
    int RC_FACEBOOK_SIGN_IN = 2;
    CallbackManager callbackManager;
    EditText etFullName, etEmail, etPhone, etPassword, etCnfPassword;
    String strFullName, strEmail, strPass, strCnfPass, strCountryCode, strPhoneNumber,strKnownLanguage, strLearnLanguage,strRegisterType;
    Button btnSignup;
    CountryCodePicker countryCodePicker;
    TextView tvBackToLogin;
    ValidationFunction vf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        cf = new CommonFunction();
        cf.fullScreen(getWindow());
        init();
        faceBookLogin();
        googleLogin();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else if (requestCode == RC_FACEBOOK_SIGN_IN) {
            //callbackManager.onActivityResult(requestCode,RC_FACEBOOK_SIGN_IN,data);
            //System.out.println("Facebook " + data.toString());
        }
    }

    public void init() {

        Intent intent = getIntent();
        strKnownLanguage = intent.getStringExtra(Utils.TAG_KNOWN_LANGUAGE);
        strLearnLanguage = intent.getStringExtra(Utils.TAG_LEARN_LANGUAGE);

        etFullName = (EditText) findViewById(R.id.etFullName);
        etEmail = (EditText) findViewById(R.id.etEmailAddress);
        etPhone = (EditText) findViewById(R.id.etPhoneNumber);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etCnfPassword = (EditText) findViewById(R.id.etCnfPassword);
        countryCodePicker = (CountryCodePicker) findViewById(R.id.countryCodePicker);
        btnSignup = (Button) findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strFullName = etFullName.getText().toString();
                strEmail = etEmail.getText().toString();
                strPass = etPassword.getText().toString().trim();
                strCnfPass = etCnfPassword.getText().toString().trim();
                strCountryCode = countryCodePicker.getSelectedCountryCode().toString();
                strPhoneNumber = etPhone.getText().toString();

                if (vf.isEmpty(etFullName)) {
                    etFullName.setError("Full Name Required");
                    //Toast.makeText(SignupActivity.this, "Please Enter Full Name", Toast.LENGTH_SHORT).show();
                } else if (vf.isEmpty(etEmail)) {
                    etEmail.setError("Email id is Required");
                } else if (vf.isEmail(etEmail) == false) {
                    etEmail.setError("Please Enter Valid email");
                    //Toast.makeText(SignupActivity.this, "Please Enter Valid email", Toast.LENGTH_SHORT).show();
                } else if (vf.isEmpty(etPassword)) {
                    etPassword.setError("Enter Password");
                } else if (vf.isEmpty(etCnfPassword)) {
                    etCnfPassword.setError("Enter Confirm Password");
                } else if (!strPass.equals(strCnfPass)){
                    etPassword.setError("Password and Confirm password Not matching");
                }else if (strPass.length()<8 && !vf.isValidPassword(strPass)){
                    etPassword.setError("Password not valid");
                } else {
                    strRegisterType = "email";
                    nextLevelSelection(strFullName, strEmail, strPass, strCountryCode, strPhoneNumber,strRegisterType);
                }

            }
        });

        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void nextLevelSelection(String strFullName, String strEmail, String strPass, String strCountryCode, String strPhoneNumber, String strRegisterType) {
        Toast.makeText(SignupActivity.this, "Success", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignupActivity.this, LevelSlectionActivity.class);
        intent.putExtra(Utils.TAG_USERNAME,strFullName);
        intent.putExtra(Utils.TAG_EMAILID,strEmail);
        intent.putExtra(Utils.TAG_PASSWORD,strPass);
        intent.putExtra(Utils.TAG_COUNTRY_CODE,strCountryCode);
        intent.putExtra(Utils.TAG_PHONE_NUMBER,strPhoneNumber);
        intent.putExtra(Utils.TAG_KNOWN_LANGUAGE,strKnownLanguage);
        intent.putExtra(Utils.TAG_LEARN_LANGUAGE,strLearnLanguage);
        intent.putExtra(Utils.TAG_DEVICE_ID, getIntent().getStringExtra(Utils.TAG_DEVICE_ID));
        intent.putExtra(Utils.TAG_CURRENT_LOCATION, getIntent().getStringExtra(Utils.TAG_CURRENT_LOCATION));
        intent.putExtra(Utils.TAG_REGISTRATION_TYPE,strRegisterType);
        startActivity(intent);
    }


    public void faceBookLogin() {
        String EMAIL = "email";

        //LoginManager.getInstance().logInWithReadPermissions(SignupActivity.this,Arrays.asList("public_profile"));
        callbackManager = CallbackManager.Factory.create();
        LoginButton fbLoginBtn = (LoginButton) findViewById(R.id.btnFb);
        fbLoginBtn.setReadPermissions(Arrays.asList(EMAIL));
        Button fbLogin = (Button) findViewById(R.id.btnFbLogin);
        fbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbLoginBtn.performClick();
            }
        });

        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

            }

            @Override
            public void onCancel() {
                // App code
                System.out.println("Facebook ");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                System.out.println("Facebook error" + exception.toString());
            }
        });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.w("TAG", "signInResult:success =" + account.getEmail());

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    public void googleLogin() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //SignInButton googleSignIn = findViewById(R.id.btnGoogle);
        Button googleBtn = (Button) findViewById(R.id.btnGoogleLogin);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
            }
        });
        /*googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }


}