package com.bazinga.lantoon.home.payment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.home.payment.model.PaymentPackage;
import com.bazinga.lantoon.registration.langselection.model.Language;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class PaymentPackageAdapter extends ArrayAdapter<PaymentPackage> {

    public PaymentPackageAdapter(Context context, List<PaymentPackage> paymentPackageList) {
        super(context, 0, paymentPackageList);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        //Language myLanguageData = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_payment_package, parent, false);

        }

        TextView tvPackageName = convertView.findViewById(R.id.tvPackageName);
        TextView tvChapterUnlocked = convertView.findViewById(R.id.tvChapterUnlocked);
        TextView tvValidityInDays = convertView.findViewById(R.id.tvValidityInDays);
        TextView tvPrice = convertView.findViewById(R.id.tvPrice);

        tvPackageName.setText(getItem(position).getPackageName());
        tvChapterUnlocked.setText(getItem(position).getChaptersUnlocked());
        tvValidityInDays.setText(getItem(position).getDurationInDays()+"\n"+"Days");
        tvPrice.setText(getItem(position).getPrice()+" "+ getItem(position).getCurrencySymbol());


        // Return the completed view to render on screen
        return convertView;
    }
}