package com.bazinga.lantoon.registration.langselection.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.Tags;
import com.bazinga.lantoon.registration.langselection.adapter.LanguageAdapter;
import com.bazinga.lantoon.registration.langselection.model.Language;
import com.bazinga.lantoon.registration.langselection.viewmodel.LanguageViewModel;
import com.bazinga.lantoon.registration.SignupActivity;

import java.util.List;

public class LangSelectionActivity extends AppCompatActivity {

    CommonFunction cf;
    LangSelectionActivity context;
    LanguageViewModel languageViewModel;
    ViewPager2 iKnowVp, iWantLearnVp;
    int knownLangId, learnLangId;
    List<Language> languageList;
    TextView tv1, tv2;
    ProgressBar progressBar;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lang_selection);
        cf = new CommonFunction();
        cf.fullScreen(getWindow());

        btnNext = findViewById(R.id.btnNext);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        progressBar = findViewById(R.id.progressBar);
        iKnowVp = findViewById(R.id.vpIknow);
        iWantLearnVp = findViewById(R.id.vpIwantLearn);
        languageViewModel = new ViewModelProvider(this).get(LanguageViewModel.class);
        updateUI(true);
        languageViewModel.getLanguageMutableLiveData().observe(this, new Observer<List<Language>>() {
            @Override
            public void onChanged(List<Language> languages) {

                languageList = languages;
                LanguageAdapter languageAdapter = new LanguageAdapter(LangSelectionActivity.this, languageList);
                int totalCount = languageAdapter.getItemCount() - 1;
                iKnowVp.setAdapter(languageAdapter);
                iWantLearnVp.setAdapter(languageAdapter);
                iWantLearnVp.setCurrentItem(iKnowVp.getCurrentItem() + 1);


                iKnowVp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                        if (iKnowVp.getCurrentItem() == iWantLearnVp.getCurrentItem()) {
                            if (iKnowVp.getCurrentItem() != totalCount) {
                                iWantLearnVp.setCurrentItem(iKnowVp.getCurrentItem() + 1);
                            } else {
                                iWantLearnVp.setCurrentItem(iKnowVp.getCurrentItem() - 1);
                            }
                        }

                    }
                });

                iWantLearnVp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                        if (iWantLearnVp.getCurrentItem() == iKnowVp.getCurrentItem()) {
                            if (iWantLearnVp.getCurrentItem() != totalCount) {
                                iKnowVp.setCurrentItem(iWantLearnVp.getCurrentItem() + 1);
                            } else {
                                iKnowVp.setCurrentItem(iWantLearnVp.getCurrentItem() - 1);
                            }
                        }
                    }
                });
                updateUI(false);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iKnowVp.getCurrentItem() != iWantLearnVp.getCurrentItem()) {

                    knownLangId = Integer.valueOf(languageList.get(iKnowVp.getCurrentItem()).getLanguageID());
                    learnLangId = Integer.valueOf(languageList.get(iWantLearnVp.getCurrentItem()).getLanguageID());
                    Intent intent = new Intent(LangSelectionActivity.this, SignupActivity.class);
                    intent.putExtra(Tags.TAG_KNOWN_LANGUAGE, knownLangId);
                    intent.putExtra(Tags.TAG_LEARN_LANGUAGE, learnLangId);
                    intent.putExtra(Tags.TAG_DEVICE_ID, getIntent().getStringExtra(Tags.TAG_DEVICE_ID));
                    intent.putExtra(Tags.TAG_CURRENT_LOCATION, getIntent().getStringExtra(Tags.TAG_CURRENT_LOCATION));
                    intent.putExtra(Tags.TAG_REGION_CODE, getIntent().getStringExtra(Tags.TAG_REGION_CODE));
                    intent.putExtra(Tags.TAG_NOTIFICATION_TOKEN, getIntent().getStringExtra(Tags.TAG_NOTIFICATION_TOKEN));
                    startActivity(intent);
                } else {
                    Toast.makeText(LangSelectionActivity.this, "Both Languages are equal", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updateUI(boolean isLoading) {
        if (isLoading) {
            tv1.setVisibility(View.INVISIBLE);
            tv2.setVisibility(View.INVISIBLE);
            btnNext.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            tv1.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }
}