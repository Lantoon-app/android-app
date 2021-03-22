package com.bazinga.lantoon.home.leader;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bazinga.lantoon.R;


public class LeaderFragment extends Fragment {

    private LeaderViewModel leaderViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        leaderViewModel = new ViewModelProvider(this).get(LeaderViewModel.class);
        View root = inflater.inflate(R.layout.fragment_leader, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        leaderViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}