package com.bazinga.lantoonnew.home.mylanguage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bazinga.lantoonnew.R;
import com.bazinga.lantoonnew.home.HomeActivity;
import com.bazinga.lantoonnew.home.mylanguage.model.MyLanguageData;
import com.bazinga.lantoonnew.retrofit.ApiClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class MyLanguagesAdapter extends BaseAdapter implements Filterable {
    int knwnLang;
    Context context;
    List<MyLanguageData>  myLanguageDataList,filterList;
    MyLanguageFilter filter;
    public MyLanguagesAdapter(Context context, List<MyLanguageData> languageArrayList, int knwnLang) {
        this.context = context;
        this.myLanguageDataList = languageArrayList;
        this.filterList = languageArrayList;
        this.knwnLang = knwnLang;
    }

    @Override
    public int getCount() {
        return myLanguageDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return myLanguageDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        //MyLanguageData myLanguageData = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_change_my_language, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imgView);
        //LinearLayout llItem = convertView.findViewById(R.id.llItem);
        TextView tvName = convertView.findViewById(R.id.textView);

        // Populate the data into the template view using the data object
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
        if (ApiClient.isTest)
            Glide.with(context).load(ApiClient.BASE_TEST_URL + "Lantoon/" + myLanguageDataList.get(position).getLearnLanguage().getImagePath()).apply(requestOptions).into(imageView);
        else
            Glide.with(context).load(ApiClient.BASE_PROD_URL + "Lantoon/" + myLanguageDataList.get(position).getLearnLanguage().getImagePath()).apply(requestOptions).into(imageView);

        tvName.setText(myLanguageDataList.get(position).getLearnLanguage().getNativeName());
        if (Integer.valueOf(myLanguageDataList.get(position).getLearnLanguage().getLanguageID()) == knwnLang) {
            tvName.setBackground(context.getDrawable(R.drawable.bg_tv_laguage_selected));
            tvName.setTextColor(context.getColor(R.color.black));
        } else {
            tvName.setBackground(context.getDrawable(R.drawable.bg_tv_laguage));
            tvName.setTextColor(context.getColor(R.color.white));
        }

        /*tvName.setText(myLanguageData.getLearnLanguage().getLanguageName() + " / " + myLanguageData.getLearnLanguage().getNativeName());
        if (HomeActivity.sessionManager.getLearnLangId() == Integer.valueOf(myLanguageData.getLearnLanguage().getLanguageID())) {
            linearLayout11.setBackground(getContext().getDrawable(R.drawable.my_language_item_selected_bg));
            tvName.setTextColor(getContext().getColor(R.color.white));
            ivLang.setImageResource(R.drawable.my_language_item_empty);
        } else ivLang.setImageResource(R.drawable.my_language_item_filled);
       */ // Return the completed view to render on screen
        return convertView;
    }
    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new MyLanguageFilter(filterList, this);
        }

        return filter;
    }
}