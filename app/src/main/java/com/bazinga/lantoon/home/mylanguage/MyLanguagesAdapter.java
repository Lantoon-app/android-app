package com.bazinga.lantoon.home.mylanguage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.home.HomeActivity;
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

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MyLanguageData myLanguageData = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_change_my_language, parent, false);
        }

        //ImageView imageView = convertView.findViewById(R.id.imgView);

        // Lookup view for data population
        LinearLayout linearLayout11 = convertView.findViewById(R.id.linearLayout11);
        TextView tvName = convertView.findViewById(R.id.textView);
        ImageView ivLang = convertView.findViewById(R.id.ivLang);
        // Populate the data into the template view using the data object
       /* RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
        Glide.with(getContext()).load("https://www.lantoon.net/Lantoon%20Admin%20Panel/" + myLanguageData.getLearnLanguage().getImagePath()).apply(requestOptions).into(imageView);*/

        tvName.setText(myLanguageData.getLearnLanguage().getLanguageName() + " / " + myLanguageData.getLearnLanguage().getNativeName());
        if (HomeActivity.sessionManager.getLearLang() == Integer.valueOf(myLanguageData.getLearnLanguage().getLanguageID())) {
            linearLayout11.setBackground(getContext().getDrawable(R.drawable.my_language_item_selected_bg));
            tvName.setTextColor(getContext().getColor(R.color.white));
            ivLang.setImageResource(R.drawable.my_language_item_empty);
        } else ivLang.setImageResource(R.drawable.my_language_item_filled);
        // Return the completed view to render on screen
        return convertView;
    }

}