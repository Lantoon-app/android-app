package com.bazinga.lantoon.home.target;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.SlantView;
import com.bazinga.lantoon.home.mylanguage.model.MyLanguageData;
import com.bazinga.lantoon.home.target.model.Target;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TargetListAdapter extends ArrayAdapter<Target> {
    public TargetListAdapter(Context context, List<Target> targetList) {
        super(context, 0, targetList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Target target = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_target, parent, false);
        }

        SlantView slantView = convertView.findViewById(R.id.slantView);
        TextView tvLesson = convertView.findViewById(R.id.tvLesson);
        TextView tvDates = convertView.findViewById(R.id.tvDates);

        tvLesson.setText(target.getDisplayText());
        String dtFrom = target.getFromDate();
        String dtTo = target.getToDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date fromDate = format.parse(dtFrom);
            Date toDate = format.parse(dtTo);

            //System.out.println(date);
            tvDates.setText(new SimpleDateFormat("MMM").format(fromDate)+" "+fromDate.getDate()+" TO "+ new SimpleDateFormat("MMM").format(toDate)+" "+toDate.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
