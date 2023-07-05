package com.bazinga.lantoon.home.changereferlanguage;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.registration.langselection.model.Language;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ChangeReferenceLanguageAdapter extends BaseAdapter implements Filterable {
    int referLang;
    List<Language> languageList, filterList;
    Context context;
    ChangeLanguageFilter filter;

    public ChangeReferenceLanguageAdapter(Context context, List<Language> languageArrayList, int referLang) {
        this.context = context;
        this.languageList = languageArrayList;
        this.filterList = languageArrayList;
        this.referLang = referLang;
    }

    @Override
    public int getCount() {
        return languageList.size();
    }

    @Override
    public Object getItem(int i) {
        return languageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        //Language myLanguageData = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_change_language, parent, false);

        }

        ImageView imageView = convertView.findViewById(R.id.imgView);
        //LinearLayout llItem = convertView.findViewById(R.id.llItem);
        TextView tvName = convertView.findViewById(R.id.textView);

        // Populate the data into the template view using the data object
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
        if (ApiClient.isTest)
            Glide.with(context).load(ApiClient.BASE_TEST_URL + "Lantoon/" + languageList.get(position).getImagePath()).apply(requestOptions).into(imageView);
        else
            Glide.with(context).load(ApiClient.BASE_PROD_URL + "Lantoon/" + languageList.get(position).getImagePath()).apply(requestOptions).into(imageView);
        //tvName.setText(languageList.get(position).getLanguageName() + " / " + languageList.get(position).getNativeName());
        tvName.setText(languageList.get(position).getNativeName());
        if (Integer.valueOf(languageList.get(position).getLanguageID()) == referLang) {
            tvName.setBackground(context.getDrawable(R.drawable.bg_tv_laguage_selected));
            tvName.setTextColor(context.getColor(R.color.black));
        } else {
            tvName.setBackground(context.getDrawable(R.drawable.bg_tv_laguage));
            tvName.setTextColor(context.getColor(R.color.white));
        }


        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new ChangeLanguageFilter(filterList, this);
        }
        return filter;
    }
}