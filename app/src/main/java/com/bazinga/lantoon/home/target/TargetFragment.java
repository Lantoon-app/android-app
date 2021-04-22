package com.bazinga.lantoon.home.target;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.calendar.CalendarView;
import com.bazinga.lantoon.home.HomeActivity;
import com.bazinga.lantoon.home.target.model.Target;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


public class TargetFragment extends Fragment implements View.OnClickListener {

    private TargetViewModel targetViewModel;
    Button btnCompleted, btnOnGoing, btnUpcoming;
    TargetListAdapter targetListAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        targetViewModel = new ViewModelProvider(this).get(TargetViewModel.class);
        View root = inflater.inflate(R.layout.fragment_target, container, false);
        btnCompleted = root.findViewById(R.id.btnCompleted);
        btnOnGoing = root.findViewById(R.id.btnOnGoing);
        btnUpcoming = root.findViewById(R.id.btnUpcoming);
        btnCompleted.setOnClickListener(this::onClick);
        btnOnGoing.setOnClickListener(this::onClick);
        btnUpcoming.setOnClickListener(this::onClick);
        btnCompleted.setTextColor(Color.BLUE);


        ListView lvTargets = root.findViewById(R.id.lvTargets);

        targetViewModel.getTargets(HomeActivity.sessionManager.getUserDetails().getUid()).observe(getActivity(), new Observer<List<Target>>() {
            @Override
            public void onChanged(List<Target> targets) {
                if (targets != null) {
                    //calendarView.updateCalendarTarget(targets);
                    targetListAdapter = new TargetListAdapter(getContext(), targets);
                    targetListAdapter.getFilter().filter("1");
                    lvTargets.setAdapter(targetListAdapter);
                }
            }
        });
        return root;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCompleted:
                targetListAdapter.getFilter().filter("1");
                btnCompleted.setTextColor(Color.BLUE);
                btnOnGoing.setTextColor(Color.WHITE);
                btnUpcoming.setTextColor(Color.WHITE);
                break;
            case R.id.btnOnGoing:
                targetListAdapter.getFilter().filter("2");
                btnCompleted.setTextColor(Color.WHITE);
                btnOnGoing.setTextColor(Color.BLUE);
                btnUpcoming.setTextColor(Color.WHITE);
                break;
            case R.id.btnUpcoming:
                targetListAdapter.getFilter().filter("3");
                btnCompleted.setTextColor(Color.WHITE);
                btnOnGoing.setTextColor(Color.WHITE);
                btnUpcoming.setTextColor(Color.BLUE);
                break;
        }
    }


}