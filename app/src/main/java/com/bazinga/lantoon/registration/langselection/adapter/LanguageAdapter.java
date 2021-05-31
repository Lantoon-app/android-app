package com.bazinga.lantoon.registration.langselection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.registration.langselection.model.Language;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {

    Context context;
    List<Language> languageList;

    public LanguageAdapter(Context context, List<Language> languageList) {
        this.context = context;
        this.languageList = languageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_language_selection, parent, false);

        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //if(languageList.get(position).getLanguageName().equals(languageList.get(position).getNativeName()))
        holder.languageName.setText(languageList.get(position).getLanguageName() + " / " + languageList.get(position).getNativeName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
        Glide.with(context).load(ApiClient.BASE_URL +"Lantoon%20Admin%20Panel/" + languageList.get(position).getImagePath()).apply(requestOptions).into(holder.languageImage);
    }

    @Override
    public int getItemCount() {
        return languageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView languageName;
        ImageView languageImage;

        ViewHolder(View itemView) {
            super(itemView);
            languageName = itemView.findViewById(R.id.txtViewLanguageName);

            languageImage = itemView.findViewById(R.id.imgViewLanguage);
        }
    }
}
