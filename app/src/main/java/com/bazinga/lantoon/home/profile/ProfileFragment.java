package com.bazinga.lantoon.home.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.login.data.model.LoggedInUser;


public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    EditText etFullName, etDOB, etPhoneNumber;
    Spinner spinnerDuration;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        etFullName = root.findViewById(R.id.etFullName);
        etDOB = root.findViewById(R.id.etDOB);
        etPhoneNumber = root.findViewById(R.id.etPhoneNumber);
        spinnerDuration = root.findViewById(R.id.spinnerDuration);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.duration_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDuration.setAdapter(adapter);
        profileViewModel.getUser().observe(getActivity(), new Observer<LoggedInUser>() {
            @Override
            public void onChanged(@Nullable LoggedInUser loggedInUser) {
                //textView.setText(s);
                etFullName.setText(loggedInUser.getData().getUname());
                etDOB.setText(loggedInUser.getData().getDob());
                etPhoneNumber.setText(loggedInUser.getData().getPhone());
            }
        });
        return root;
    }
}