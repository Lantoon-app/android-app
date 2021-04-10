package com.bazinga.lantoon.home.mylanguage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.registration.langselection.adapter.LanguageAdapter;
import com.bazinga.lantoon.registration.langselection.model.Language;
import com.bazinga.lantoon.registration.langselection.view.LangSelectionActivity;
import com.google.gson.Gson;

import java.util.List;

public class MyLanguageFragment extends Fragment {

    private MyLanguageViewModel myLanguageViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myLanguageViewModel = new ViewModelProvider(this).get(MyLanguageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_language, container, false);
        ListView listView = root.findViewById(R.id.llView);

        myLanguageViewModel.getLanguageMutableLiveData().observe(getActivity(), new Observer<List<Language>>() {
            @Override
            public void onChanged(List<Language> languages) {
                Log.e("response body= ", new Gson().toJson(languages));

                MyLanguagesAdapter adapter = new MyLanguagesAdapter(getContext(), languages);


                listView.setAdapter(adapter);
            }
        });


        return root;
    }
}