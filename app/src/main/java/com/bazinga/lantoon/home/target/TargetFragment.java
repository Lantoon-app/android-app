package com.bazinga.lantoon.home.target;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.home.HomeActivity;
import com.bazinga.lantoon.home.target.model.Target;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class TargetFragment extends Fragment {

    private TargetViewModel targetViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        targetViewModel = new ViewModelProvider(this).get(TargetViewModel.class);
        View root = inflater.inflate(R.layout.fragment_target, container, false);
        CalendarView calendarView = root.findViewById(R.id.calendarView);
        List<Calendar> calendars = new ArrayList<>();


        List<Date> dates = getDates("2021-04-11", "2021-04-18");
        for (Date date : dates)
            calendars.add(getCalenderDate(date));
        //System.out.println(getCalenderDate(date).getTime());

        ListView lvTargets = root.findViewById(R.id.lvTargets);

        targetViewModel.getTargets(HomeActivity.sessionManager.getUserDetails().getUid()).observe(getActivity(), new Observer<List<Target>>() {
            @Override
            public void onChanged(List<Target> targets) {
                if (targets != null)
                    lvTargets.setAdapter(new TargetListAdapter(getContext(), targets));
            }
        });
        return root;
    }

    private Calendar getCalenderDate(Date date) {


        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;


    }

    private Calendar getCalenderDateFromString(String strDate) {
        try {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(strDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<Date> getDates(String dateString1, String dateString2) {
        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1.parse(dateString1);
            date2 = df1.parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while (!cal1.after(cal2)) {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }
}