package com.bazinga.lantoon.home.target;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.github.florent37.diagonallayout.DiagonalLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TargetListAdapter extends BaseAdapter implements Filterable {

    Context c;
    List<Target> targetList;
    LayoutInflater inflater;

    List<Target> filterList;
    TargetFilter filter;

    public TargetListAdapter(Context context, List<Target> targetList) {

        this.c = context;
        this.targetList = targetList;
        this.filterList = targetList;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return targetList.size();
    }

    @Override
    public Object getItem(int position) {
        return targetList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //View root;
        if (convertView == null) {
            //inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_target, null);
        }

        DiagonalLayout diagonalLayout = convertView.findViewById(R.id.slantView);
        ImageView imgView = convertView.findViewById(R.id.ivSlant);
        TextView tvLesson = convertView.findViewById(R.id.tvLesson);
        TextView tvDates = convertView.findViewById(R.id.tvDates);
        if (targetList.get(position).getTargetstatus() == 1) {
            convertView.setBackground(c.getDrawable(R.drawable.target_completed));
            imgView.setBackgroundColor(c.getResources().getColor(R.color.target_completed));
            tvLesson.setTextColor(Color.BLACK);
            tvDates.setTextColor(Color.BLACK);
        } else if (targetList.get(position).getTargetstatus() == 2) {
            convertView.setBackground(c.getDrawable(R.drawable.target_ongoing));
            imgView.setBackgroundColor(c.getResources().getColor(R.color.target_ongoing));
            tvLesson.setTextColor(Color.WHITE);
            tvDates.setTextColor(Color.WHITE);
        } else if (targetList.get(position).getTargetstatus() == 3) {
            convertView.setBackground(c.getDrawable(R.drawable.target_upcoming));
            imgView.setBackgroundColor(c.getResources().getColor(R.color.target_upcoming));
            tvLesson.setTextColor(Color.WHITE);
            tvDates.setTextColor(Color.WHITE);
        }
        tvLesson.setText(targetList.get(position).getDisplayText());
        String dtFrom = targetList.get(position).getFromDate();
        String dtTo = targetList.get(position).getToDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date fromDate = format.parse(dtFrom);
            Date toDate = format.parse(dtTo);

            //System.out.println(date);
            tvDates.setText(new SimpleDateFormat("MMM").format(fromDate) + " " + fromDate.getDate() + " TO " + new SimpleDateFormat("MMM").format(toDate) + " " + toDate.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new TargetFilter(filterList, this);
        }

        return filter;
    }

   /* @Override
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
    }*/
}
