package com.bazinga.lantoonnew.home.payment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bazinga.lantoonnew.R;
import com.bazinga.lantoonnew.home.payment.model.PaymentPackage;

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