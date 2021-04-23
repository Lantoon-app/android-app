package com.bazinga.lantoon.home.changereferlanguage;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.home.HomeActivity;
import com.bazinga.lantoon.home.mylanguage.model.MyLanguageData;
import com.bazinga.lantoon.registration.langselection.model.Language;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ChangeReferenceLanguageAdapter extends ArrayAdapter<Language> {
    public ChangeReferenceLanguageAdapter(Context context, List<Language> languageArrayList) {
        super(context, 0, languageArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Language myLanguageData = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_change_my_language, parent, false);
        }
        if(Integer.valueOf(myLanguageData.getLanguageID())  == HomeActivity.sessionManager.getKnownLang())
            convertView.setBackground(getContext().getDrawable(R.drawable.button_bg));
        ImageView imageView = convertView.findViewById(R.id.imgView);
        TextView tvName = convertView.findViewById(R.id.textView);

        // Populate the data into the template view using the data object
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
        Glide.with(getContext()).load("https://www.lantoon.net/Lantoon%20Admin%20Panel/" + myLanguageData.getImagePath()).apply(requestOptions).into(imageView);
        tvName.setText(myLanguageData.getLanguageName());

        // Return the completed view to render on screen
        return convertView;
    }
}