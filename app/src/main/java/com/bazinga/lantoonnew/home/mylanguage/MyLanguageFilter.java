package com.bazinga.lantoonnew.home.mylanguage;

import android.util.Log;
import android.widget.Filter;

import com.bazinga.lantoonnew.home.mylanguage.model.MyLanguageData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyLanguageFilter extends Filter {

    List<MyLanguageData> myLanguageDataList;
    MyLanguagesAdapter myLanguagesAdapter;

    public MyLanguageFilter(List<MyLanguageData> myLanguageDataList, MyLanguagesAdapter myLanguagesAdapter) {
        this.myLanguageDataList = myLanguageDataList;
        this.myLanguagesAdapter = myLanguagesAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        //RESULTS
        FilterResults results = new FilterResults();
        Log.d("constraint",constraint.toString());
        //VALIDATION
        if (constraint != null && constraint.length() > 0) {

            //CHANGE TO UPPER FOR CONSISTENCY
            constraint = constraint.toString().toUpperCase();

            List<MyLanguageData> filterList = new ArrayList<>();

            //LOOP THRU FILTER LIST
            for (int i = 0; i < myLanguageDataList.size(); i++) {
                //FILTER
                System.out.println("myLanguageDataList "+myLanguageDataList.get(i).getLearnLanguage().getLanguageName().toUpperCase(Locale.ROOT)+" constraint "+constraint);
                if (myLanguageDataList.get(i).getLearnLanguage().getLanguageName().toUpperCase(Locale.ROOT).contains(constraint)) {
                    filterList.add(myLanguageDataList.get(i));
                }
            }

            results.count = filterList.size();
            results.values = filterList;
        } else {
            results.count = myLanguageDataList.size();
            results.values = myLanguageDataList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        myLanguagesAdapter.myLanguageDataList = (List<MyLanguageData>) results.values;
        myLanguagesAdapter.notifyDataSetChanged();
    }
}
