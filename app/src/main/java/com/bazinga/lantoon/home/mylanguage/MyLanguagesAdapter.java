package com.bazinga.lantoon.home.mylanguage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.home.mylanguage.model.MyLanguageData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class MyLanguagesAdapter extends ArrayAdapter<MyLanguageData> {
    public MyLanguagesAdapter(Context context, List<MyLanguageData> languageArrayList) {
        super(context, 0, languageArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MyLanguageData myLanguageData = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_change_my_language, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imgView);

        // Lookup view for data population
        TextView tvName = convertView.findViewById(R.id.textView);
        // Populate the data into the template view using the data object
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
        Glide.with(getContext()).load("https://www.lantoon.net/Lantoon%20Admin%20Panel/" + myLanguageData.getLearnLanguage().getImagePath()).apply(requestOptions).into(imageView);
        tvName.setText(myLanguageData.getLearnLanguage().getLanguageName());
        // Return the completed view to render on screen
        return convertView;
    }

}