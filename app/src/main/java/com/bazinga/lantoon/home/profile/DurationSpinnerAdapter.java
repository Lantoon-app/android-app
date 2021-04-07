package com.bazinga.lantoon.home.profile;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.registration.model.DurationData;

import java.util.List;

public class DurationSpinnerAdapter extends BaseAdapter {
    Context context;
    List<DurationData> durationDataList;

    public DurationSpinnerAdapter(Context context, List<DurationData> durationDataList){
        this.context = context;
        this.durationDataList = durationDataList;
    }
    @Override
    public int getCount() {
        return durationDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       View view = convertView.inflate(context,R.layout.item_duration_spinner, null);

        TextView durations = view.findViewById(R.id.textView);
        durations.setText(durationDataList.get(position).getCaption()+" ( "+durationDataList.get(position).getDurationMin()+" MIN / DAY )");

        return view;
    }
}
