package com.bazinga.lantoonnew.home.changepassword;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bazinga.lantoonnew.R;
import com.bazinga.lantoonnew.ValidationFunction;
import com.bazinga.lantoonnew.home.HomeActivity;
import com.bazinga.lantoonnew.retrofit.Status;
import com.google.gson.GsonBuilder;


public class ChangePasswordFragment extends Fragment {

    private ChangePasswordViewModel changePasswordViewModel;
    EditText etChangePasswordOldPassword, etChangePasswordNewPassword, etChangePasswordCnfPassword;
    Button btnChangePassword;
    ValidationFunction vf;
    boolean fragmentDestroyed = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_change_password, container, false);


        etChangePasswordOldPassword = root.findViewById(R.id.etChangePasswordOldPassword);
        etChangePasswordNewPassword = root.findViewById(R.id.etChangePasswordNewPassword);
        etChangePasswordCnfPassword = root.findViewById(R.id.etChangePasswordCnfPassword);
        changePasswordViewModel = new ViewModelProvider(getActivity(),
                new ChangePasswordViewModelProvider(HomeActivity.sessionManager.getUid(),
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

                    changePasswordViewModel.update(HomeActivity.sessionManager.getUid(),
                            etChangePasswordNewPassword.getText().toString().trim()
                            , etChangePasswordOldPassword.getText().toString().trim());
                    changePasswordViewModel.getResult().observe(getActivity(), new Observer<Status>() {
                        @Override
                        public void onChanged(@Nullable Status s) {
                            if(!fragmentDestroyed) {
                                Toast.makeText(getContext(), s.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("Status ", new GsonBuilder().setPrettyPrinting().create().toJson(s));
                            }
                        }
                    });
                }


            }
        });

        return root;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentDestroyed = true;
    }
}