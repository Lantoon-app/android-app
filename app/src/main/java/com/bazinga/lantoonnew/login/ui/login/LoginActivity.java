package com.bazinga.lantoonnew.login.ui.login;

import static android.content.ContentValues.TAG;
import static com.bazinga.lantoonnew.CommonFunction.storeUserData;

import android.app.Activity;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bazinga.lantoonnew.CommonFunction;
import com.bazinga.lantoonnew.NetworkUtil;
import com.bazinga.lantoonnew.R;
import com.bazinga.lantoonnew.Tags;
import com.bazinga.lantoonnew.ValidationFunction;
import com.bazinga.lantoonnew.home.HomeActivity;
import com.bazinga.lantoonnew.login.SessionManager;
import com.bazinga.lantoonnew.login.forget.ForgetPasswordActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
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

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private FirebaseAuth mAuth;
    int RC_GOOGLE_SIGN_IN = 1;
    SessionManager sessionManager;
    EditText usernameEditText, passwordEditText;
    Button loginButton;
    ProgressBar loadingProgressBar;
    TextView tvForgetPassword;
    LinearLayout lllogin;
    String deiveId, notificationToken, deviceModel;
    int registrationTypeId = 1;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(this);
        deiveId = getIntent().getStringExtra(Tags.TAG_DEVICE_ID);
        notificationToken = getIntent().getStringExtra(Tags.TAG_NOTIFICATION_TOKEN);
        deviceModel = Build.MODEL;
        mAuth = FirebaseAuth.getInstance();
        faceBookLogin();
        googleLogin();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mAuth.signOut();
            mGoogleSignInClient.signOut();
        }
        lllogin = findViewById(R.id.lllogin);
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
                            passwordEditText.getText().toString(), deiveId, notificationToken, deviceModel, registrationTypeId);
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
                                passwordEditText.getText().toString(), deiveId, notificationToken, deviceModel, 1);
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.empty_username_password, Toast.LENGTH_SHORT).show();
                    }
                } else {
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
        storeUserData(sessionManager, model);
        finish();
        startActivity(new Intent(this, HomeActivity.class));
    }

    private void showLoginFailed(String errorString) {
        loadingProgressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    public void faceBookLogin() {
        String EMAIL = "email";

        //LoginManager.getInstance().logInWithReadPermissions(SignupActivity.this,Arrays.asList("public_profile"));
        CallbackManager callbackManager = CallbackManager.Factory.create();
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
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<com.facebook.login.LoginResult>() {
            @Override
            public void onSuccess(com.facebook.login.LoginResult loginResult) {
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
                            socialLogin(user.getEmail(),registrationTypeId);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
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
        Button btnGoogleLogin = (Button) findViewById(R.id.btnGoogleLogin);
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
                            socialLogin(user.getEmail(),registrationTypeId);
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

    private void socialLogin(String emailId,int registrationTypeId){
        loginViewModel.login(emailId,
                "", deiveId, notificationToken, deviceModel, registrationTypeId);
    }

}