package com.bazinga.lantoon.home.chapter.lesson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.home.chapter.lesson.model.Question;
import com.bazinga.lantoon.home.chapter.lesson.ui.l1.L1Fragment;
import com.bazinga.lantoon.home.chapter.lesson.ui.qp1.QP1Fragment;
import com.bazinga.lantoon.registration.langselection.model.Language;
import com.bazinga.lantoon.registration.langselection.viewmodel.LanguageViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class QuestionsActivity extends AppCompatActivity {
    CommonFunction cf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_activity);
        cf = new CommonFunction();
        cf.fullScreen(getWindow());
        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, L1Fragment.newInstance())
                    .commitNow();
        }*/

        QuestionsViewModel questionViewModel = new ViewModelProvider(this).get(QuestionsViewModel.class);
        questionViewModel.getQuestionsMutableLiveData().observe(this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {
/*
                if (jsonObject != null) {
                    for (int i = 1; i < jsonObject.size() + 1; i++) {
                        if (jsonObject.get(String.valueOf(i)).getAsJsonArray().size() > 1) {
                            Log.d("jsonObject L1", jsonObject.getAsJsonArray(String.valueOf(i)).toString());
                            //return L1Fragment.newInstance();

                        } else {
                            //return QP1Fragment.newInstance();

                        }
                    }

                }*/

               /* ViewPagerAdapter viewPagerAdapter =
                        new ViewPagerAdapter(QuestionsActivity.this, getSupportFragmentManager(), jsonObject);
                ViewPager viewPager = findViewById(R.id.view_pager);
                viewPager.setAdapter(viewPagerAdapter);*/

              /*  List<View> views = new ArrayList<>();
                views.add(0,);
                //pagerAdapter.notifyDataSetChanged();
                Pager pagerAdapter = new Pager(views, QuestionsActivity.this);
                ViewPager viewPager = findViewById(R.id.view_pager);
                viewPager.setAdapter(pagerAdapter);*/
                //tabLayout.setupWithViewPager(viewPager);


                List<Fragment> fragments = buildFragments(jsonObject);
                ArrayList<String> categories = new ArrayList<String>();
                categories.add("1");
                categories.add("2");
                categories.add("3");
                categories.add("4");
                ViewPager mPager = (ViewPager) findViewById(R.id.view_pager);
                MyFragmentPageAdapter mPageAdapter = new MyFragmentPageAdapter(QuestionsActivity.this, getSupportFragmentManager(), fragments, categories);
                mPager.setAdapter(mPageAdapter);

//Add a new Fragment to the list with bundle
               /* Bundle b = new Bundle();
                //b.putInt("position", i);
                String title = "asd";

                mPageAdapter.add(L1Fragment.class,b);
                mPageAdapter.notifyDataSetChanged();*/
            }
        });


        //TabLayout tabs = findViewById(R.id.tabs);
        //tabs.setupWithViewPager(viewPager);


    }

    private List<Fragment> buildFragments(@NonNull JsonObject jsonObject) {
        List<Fragment> fragments = new ArrayList<Fragment>();

        //Bundle b = new Bundle();
        //b.putInt("position", i);


        for (int i = 1; i < jsonObject.size() + 1; i++) {
            if (jsonObject.get(String.valueOf(i)).getAsJsonArray().size() > 1) {
                Log.d("jsonObject L1", jsonObject.getAsJsonArray(String.valueOf(i)).toString());
                fragments.add(0, L1Fragment.newInstance());

            } else {
                //if(jsonObject.getAsJsonArray(String.valueOf(i)).get(0))
                //Question question = new Question();
                JsonObject jsonArray = jsonObject.getAsJsonArray(String.valueOf(i)).get(0).getAsJsonObject();

                Log.d("test ", jsonArray.get("qid").toString());
                String ss = "qp1";
                if (ss.equals(jsonArray.get("q_type")))
                    fragments.add(i, QP1Fragment.newInstance());

            }
        }


        return fragments;
    }
}