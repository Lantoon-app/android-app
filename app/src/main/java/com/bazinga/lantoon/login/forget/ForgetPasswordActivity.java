package com.bazinga.lantoon.login.forget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.NetworkUtil;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.ValidationFunction;
import com.bazinga.lantoon.home.mylanguage.MyLanguageViewModel;
import com.bazinga.lantoon.login.ui.login.LoginActivity;
import com.bazinga.lantoon.retrofit.Status;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    ForgetPasswordViewModel forgetPasswordViewModel;
    ProgressBar pbForgetPassword;
    TextView tvForgetPasswordMsg;
    EditText etForgetPasswordEmail, etForgetPasswordOtp, etForgetPasswordPassword, etForgetPasswordCnfPassword;
    LinearLayout llChangePassword;
    Button btnSendOtp, btnValidate, btnSubmit;
    String userID;
    int otp = 0, userOtp = 1;
    ValidationFunction vf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        forgetPasswordViewModel = new ViewModelProvider(this).get(ForgetPasswordViewModel.class);
        pbForgetPassword = findViewById(R.id.pbForgetPassword);
        tvForgetPasswordMsg = findViewById(R.id.tvForgetPasswordMsg);
        etForgetPasswordEmail = findViewById(R.id.etForgetPasswordEmail);
        etForgetPasswordOtp = findViewById(R.id.etForgetPasswordOtp);
        etForgetPasswordPassword = findViewById(R.id.etForgetPasswordPassword);
        etForgetPasswordCnfPassword = findViewById(R.id.etForgetPasswordCnfPassword);
        llChangePassword = findViewById(R.id.llChangePassword);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        btnValidate = findViewById(R.id.btnValidate);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSendOtp.setOnClickListener(this::onClick);
        btnValidate.setOnClickListener(this::onClick);
        btnSubmit.setOnClickListener(this::onClick);
        emailInput();
    }

    private void emailInput() {
        etForgetPasswordOtp.setVisibility(View.GONE);
        llChangePassword.setVisibility(View.GONE);
        btnSendOtp.setEnabled(false);
        btnValidate.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.GONE);
        etForgetPasswordEmail.addTextChangedListener(afterTextChangedListener);

    }

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
            if (!isUserNameValid(s.toString())) {
                btnSendOtp.setEnabled(false);
                etForgetPasswordEmail.setError(getResources().getText(R.string.invalid_username));
            } else btnSendOtp.setEnabled(true);
        }
    };

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username != null) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSendOtp:
                if (NetworkUtil.getConnectivityStatus(ForgetPasswordActivity.this) != 0) {
                    forgetPasswordViewModel.getOtpLiveData(etForgetPasswordEmail.getText().toString()).observe(this, new Observer<OtpResponse>() {
                        @Override
                        public void onChanged(OtpResponse otpResponse) {
                            if (otpResponse != null) {
                                Toast.makeText(ForgetPasswordActivity.this, otpResponse.getStatus().getMessage(), Toast.LENGTH_SHORT).show();
                                if (otpResponse.getStatus().getCode() == 1045) {
                                    userID = otpResponse.getOTPdata().getUid();
                                    otp = otpResponse.getOTPdata().getOtp();
                                    etForgetPasswordEmail.setVisibility(View.GONE);
                                    tvForgetPasswordMsg.setText("Enter OTP");
                                    etForgetPasswordOtp.setVisibility(View.VISIBLE);
                                    btnSendOtp.setVisibility(View.GONE);
                                    btnValidate.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });
                } else {
                    CommonFunction.netWorkErrorAlert(ForgetPasswordActivity.this);
                }

                break;
            case R.id.btnValidate:
                if (NetworkUtil.getConnectivityStatus(ForgetPasswordActivity.this) != 0) {
                    if (otp != 0) {
                        try {
                            userOtp = Integer.valueOf(etForgetPasswordOtp.getText().toString());
                        } catch (NumberFormatException e) {
                            Toast.makeText(ForgetPasswordActivity.this, "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        forgetPasswordViewModel.validateOTP(otp, userOtp).observe(this, new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                if (aBoolean) {
                                    tvForgetPasswordMsg.setText("Change Password");
                                    etForgetPasswordOtp.setVisibility(View.GONE);
                                    btnValidate.setVisibility(View.GONE);
                                    llChangePassword.setVisibility(View.VISIBLE);
                                    btnSubmit.setVisibility(View.VISIBLE);
                                    Toast.makeText(ForgetPasswordActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ForgetPasswordActivity.this, "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    CommonFunction.netWorkErrorAlert(ForgetPasswordActivity.this);
                }

                break;
            case R.id.btnSubmit:
                if (NetworkUtil.getConnectivityStatus(ForgetPasswordActivity.this) != 0) {
                    String strPass = "";
                    String strCnfPass = "";

                    strPass = etForgetPasswordPassword.getText().toString().trim();
                    strCnfPass = etForgetPasswordCnfPassword.getText().toString().trim();

                    if (vf.isEmpty(etForgetPasswordPassword) || strPass.length() < 8) {
                        etForgetPasswordPassword.setError("Enter Valid New Password");
                    } else if (vf.isEmpty(etForgetPasswordCnfPassword) || strCnfPass.length() < 8) {
                        etForgetPasswordCnfPassword.setError("Enter Valid Confirm Password");
                    } else if (!strPass.equals(strCnfPass)) {
                        etForgetPasswordPassword.setError("Password and Confirm password Not matching");
                    }/*else if (!vf.isValidPassword(strPass)){
                     etForgetPasswordPassword.setError("Password not valid");
            }*/ else {
                        forgetPasswordViewModel.changeForgetPassword(userID, strPass).observe(this, new Observer<Status>() {
                            @Override
                            public void onChanged(Status status) {
                                if (status != null)
                                    if (status.getCode() == 1037) {
                                        Toast.makeText(ForgetPasswordActivity.this, "Password reset Successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                                    } else
                                        Toast.makeText(ForgetPasswordActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                } else {
                    CommonFunction.netWorkErrorAlert(ForgetPasswordActivity.this);
                }

                break;
        }


    }
}