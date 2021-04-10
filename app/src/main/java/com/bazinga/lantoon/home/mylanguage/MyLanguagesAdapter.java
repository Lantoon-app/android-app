package com.bazinga.lantoon.home.mylanguage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.lifecycle.Observer;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.registration.langselection.model.Language;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class MyLanguagesAdapter extends ArrayAdapter<Language> {
    public MyLanguagesAdapter(Context context, List<Language> languageArrayList) {
        super(context, 0, languageArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Language language = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_change_language, parent, false);
        }
        ImageView imageView = convertView.findViewById(R.id.imgView);

        // Lookup view for data population
        TextView tvName = convertView.findViewById(R.id.textView);
        // Populate the data into the template view using the data object
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
        Glide.with(getContext()).load("https://www.lantoon.net/Lantoon%20Admin%20Panel/" + language.getImagePath()).apply(requestOptions).into(imageView);
        tvName.setText(language.getLanguageName());
        // Return the completed view to render on screen
        return convertView;
    }

}