package com.bazinga.lantoon.home.changepassword;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.ValidationFunction;
import com.bazinga.lantoon.home.HomeActivity;
import com.bazinga.lantoon.home.leader.LeaderViewModelProvider;
import com.bazinga.lantoon.login.SessionManager;
import com.bazinga.lantoon.retrofit.Status;
import com.google.gson.GsonBuilder;

import java.util.Locale;


public class ChangePasswordFragment extends Fragment {

    private ChangePasswordViewModel changePasswordViewModel;
    EditText etChangePasswordOldPassword, etChangePasswordNewPassword, etChangePasswordCnfPassword;
    Button btnChangePassword;
    ValidationFunction vf;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_change_password, container, false);


        etChangePasswordOldPassword = root.findViewById(R.id.etChangePasswordOldPassword);
        etChangePasswordNewPassword = root.findViewById(R.id.etChangePasswordNewPassword);
        etChangePasswordCnfPassword = root.findViewById(R.id.etChangePasswordCnfPassword);
        changePasswordViewModel = new ViewModelProvider(getActivity(),
                new ChangePasswordViewModelProvider(HomeActivity.sessionManager.getUserDetails().getUid(),
                        etChangePasswordNewPassword.getText().toString().trim()
                        , etChangePasswordOldPassword.getText().toString().trim()
                )).get(ChangePasswordViewModel.class);
        btnChangePassword = root.findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vf.isEmpty(etChangePasswordOldPassword) || etChangePasswordOldPassword.getText().toString().length() < 8) {
                    etChangePasswordOldPassword.setError("Enter Valid Password");
                } else if (vf.isEmpty(etChangePasswordNewPassword) || etChangePasswordNewPassword.getText().toString().length() < 8) {
                    etChangePasswordNewPassword.setError("Enter Valid New Password");
                }else if (vf.isEmpty(etChangePasswordCnfPassword) || etChangePasswordCnfPassword.getText().toString().length() < 8) {
                    etChangePasswordCnfPassword.setError("Enter Valid Confirm Password");
                } else if (!etChangePasswordNewPassword.getText().toString().equals(etChangePasswordCnfPassword.getText().toString())){
                    etChangePasswordNewPassword.setError("Password and Confirm password Not matching");
                }else if (etChangePasswordNewPassword.getText().toString().length()<8 && !vf.isValidPassword(etChangePasswordNewPassword.getText().toString())){
                    etChangePasswordNewPassword.setError("Password not valid");
                } else {

                    changePasswordViewModel.update(HomeActivity.sessionManager.getUserDetails().getUid(),
                            etChangePasswordNewPassword.getText().toString().trim()
                            , etChangePasswordOldPassword.getText().toString().trim());
                    changePasswordViewModel.getResult().observe(getActivity(), new Observer<Status>() {
                        @Override
                        public void onChanged(@Nullable Status s) {
                            Toast.makeText(getContext(),s.getMessage(),Toast.LENGTH_SHORT).show();
                            Log.d("Status ",new GsonBuilder().setPrettyPrinting().create().toJson(s));
                        }
                    });
                }


            }
        });

        return root;
    }
}