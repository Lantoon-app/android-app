package com.bazinga.lantoon.home.changereferlanguage;

import android.util.Log;
import android.widget.Filter;

import com.bazinga.lantoon.registration.langselection.model.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChangeLanguageFilter extends Filter {
    List<Language> languageList;
    ChangeReferenceLanguageAdapter changeReferenceLanguageAdapter;

    public ChangeLanguageFilter(List<Language> languageList, ChangeReferenceLanguageAdapter changeReferenceLanguageAdapter) {
        this.languageList = languageList;
        this.changeReferenceLanguageAdapter = changeReferenceLanguageAdapter;
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

            List<Language> filterList = new ArrayList<>();

            //LOOP THRU FILTER LIST
            for (int i = 0; i < languageList.size(); i++) {
                //FILTER
                System.out.println("languageList "+languageList.get(i).getLanguageName().toUpperCase(Locale.ROOT)+" constraint "+constraint);
                if (languageList.get(i).getLanguageName().toUpperCase(Locale.ROOT).contains(constraint)) {
                    filterList.add(languageList.get(i));
                }
            }

            results.count = filterList.size();
            results.values = filterList;
        } else {
            results.count = languageList.size();
            results.values = languageList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        changeReferenceLanguageAdapter.languageList = (List<Language>) results.values;
        changeReferenceLanguageAdapter.notifyDataSetChanged();
    }
}
