package com.bazinga.lantoon.registration;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.Tags;
import com.bazinga.lantoon.ValidationFunction;
import com.bazinga.lantoon.login.ui.login.LoginActivity;
import com.facebook.AccessToken;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hbb20.CountryCodePicker;

public class SignupActivity extends AppCompatActivity {

    CommonFunction cf;
    private FirebaseAuth mAuth;
    int RC_GOOGLE_SIGN_IN = 1;
    int RC_FACEBOOK_SIGN_IN = 2;
    CallbackManager callbackManager;
    EditText etFullName, etEmail, etPhone, etPassword, etCnfPassword;
    String strFullName, strEmail, strPass, strCnfPass, strCountryCode, strPhoneNumber;
    int knownLangId, learnLangId, registrationTypeId = 1;
    Button btnSignup, btnGoogleLogin;
    FrameLayout flFacebookLogin;
    CountryCodePicker countryCodePicker;
    TextView tvBackToLogin;
    ValidationFunction vf;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        cf = new CommonFunction();
        cf.fullScreen(getWindow());
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        init();
        faceBookLogin();
        googleLogin();
        if (user != null) {
            mAuth.signOut();
            mGoogleSignInClient.signOut();
        }

    }


    public void init() {

        Intent intent = getIntent();
        knownLangId = intent.getIntExtra(Tags.TAG_KNOWN_LANGUAGE, 0);
        learnLangId = intent.getIntExtra(Tags.TAG_LEARN_LANGUAGE, 0);

        etFullName = (EditText) findViewById(R.id.etFullName);
        etEmail = (EditText) findViewById(R.id.etEmailAddress);
        etPhone = (EditText) findViewById(R.id.etPhoneNumber);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etCnfPassword = (EditText) findViewById(R.id.etCnfPassword);
        countryCodePicker = (CountryCodePicker) findViewById(R.id.countryCodePicker);
        btnSignup = (Button) findViewById(R.id.btnSignup);
        flFacebookLogin = (FrameLayout) findViewById(R.id.flFacebookLogin);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strFullName = etFullName.getText().toString();
                strEmail = etEmail.getText().toString();
                strPass = etPassword.getText().toString().trim();
                strCnfPass = etCnfPassword.getText().toString().trim();
                strCountryCode = countryCodePicker.getSelectedCountryCode().toString();
                strPhoneNumber = etPhone.getText().toString();
                if (registrationTypeId == 1) {
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
                    } else if (!strPass.equals(strCnfPass)) {
                        etPassword.setError("Password and Confirm password Not matching");
                    } else if (strPass.length() < 8 && !vf.isValidPassword(strPass)) {
                        etPassword.setError("Password not valid");
                    } else {
                        //registrationTypeId = 1;
                        nextLevelSelection(strFullName, strEmail, strPass, strCountryCode, strPhoneNumber, registrationTypeId);
                    }
                } else if (registrationTypeId == 2) {
                    if (vf.isEmpty(etFullName)) {
                        etFullName.setError("Full Name Required");
                        //Toast.makeText(SignupActivity.this, "Please Enter Full Name", Toast.LENGTH_SHORT).show();
                    } else if (vf.isEmpty(etEmail)) {
                        etEmail.setError("Email id is Required");
                    } else if (vf.isEmail(etEmail) == false) {
                        etEmail.setError("Please Enter Valid email");
                        //Toast.makeText(SignupActivity.this, "Please Enter Valid email", Toast.LENGTH_SHORT).show();
                    } else {
                        nextLevelSelection(strFullName, strEmail, "", strCountryCode, strPhoneNumber, registrationTypeId);
                    }
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

    private void nextLevelSelection(String strFullName, String strEmail, String strPass, String strCountryCode, String strPhoneNumber, int registrationTypeId) {
        Toast.makeText(SignupActivity.this, "Success", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignupActivity.this, LevelSlectionActivity.class);
        intent.putExtra(Tags.TAG_USERNAME, strFullName);
        intent.putExtra(Tags.TAG_EMAILID, strEmail);
        intent.putExtra(Tags.TAG_PASSWORD, strPass);
        intent.putExtra(Tags.TAG_COUNTRY_CODE, strCountryCode);
        intent.putExtra(Tags.TAG_PHONE_NUMBER, strPhoneNumber);
        intent.putExtra(Tags.TAG_KNOWN_LANGUAGE, knownLangId);
        intent.putExtra(Tags.TAG_LEARN_LANGUAGE, learnLangId);
        intent.putExtra(Tags.TAG_DEVICE_ID, getIntent().getStringExtra(Tags.TAG_DEVICE_ID));
        intent.putExtra(Tags.TAG_CURRENT_LOCATION, getIntent().getStringExtra(Tags.TAG_CURRENT_LOCATION));
        intent.putExtra(Tags.TAG_REGION_CODE, getIntent().getStringExtra(Tags.TAG_REGION_CODE));
        intent.putExtra(Tags.TAG_REGISTRATION_TYPE, registrationTypeId);
        intent.putExtra(Tags.TAG_NOTIFICATION_TOKEN, getIntent().getStringExtra(Tags.TAG_NOTIFICATION_TOKEN));
        startActivity(intent);
    }


    public void faceBookLogin() {
        String EMAIL = "email";

        //LoginManager.getInstance().logInWithReadPermissions(SignupActivity.this,Arrays.asList("public_profile"));
        callbackManager = CallbackManager.Factory.create();
        LoginButton fbLoginBtn = (LoginButton) findViewById(R.id.btnFb);
        fbLoginBtn.setReadPermissions("email", "public_profile");
        Button fbLogin = (Button) findViewById(R.id.btnFbLogin);
        fbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbLoginBtn.performClick();
            }
        });

        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        //LoginManager.getInstance().logInWithReadPermissions(this,Arrays.asList("email","public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);

                handleFacebookAccessToken(loginResult.getAccessToken());

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

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            registrationTypeId = 3;
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });
    }

    public void googleLogin() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //SignInButton googleSignIn = findViewById(R.id.btnGoogle);
        btnGoogleLogin = (Button) findViewById(R.id.btnGoogleLogin);
        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            registrationTypeId = 2;
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Log.d("signInWithCredential:email", user.getEmail());
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user.getEmail() != null) {
            etEmail.setText(user.getEmail());
            etEmail.setEnabled(false);
        }
        etFullName.setText(user.getDisplayName());
        etPhone.setText(user.getPhoneNumber());
        btnGoogleLogin.setVisibility(View.GONE);
        flFacebookLogin.setVisibility(View.GONE);
        etPassword.setVisibility(View.GONE);
        etCnfPassword.setVisibility(View.GONE);

    }

}