package com.bazinga.lantoon.home.chapter.lesson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
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
import com.bazinga.lantoon.home.chapter.lesson.ui.p1.P1Fragment;
import com.bazinga.lantoon.home.chapter.lesson.ui.p2.P2Fragment;
import com.bazinga.lantoon.home.chapter.lesson.ui.p3.P3Fragment;
import com.bazinga.lantoon.home.chapter.lesson.ui.q.QFragment;
import com.bazinga.lantoon.home.chapter.lesson.ui.qp1.QP1Fragment;
import com.bazinga.lantoon.home.chapter.lesson.ui.qp2.QP2Fragment;
import com.bazinga.lantoon.home.chapter.lesson.ui.qp3.QP3Fragment;
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

        QuestionsViewModel questionViewModel = new ViewModelProvider(this).get(QuestionsViewModel.class);
        questionViewModel.getQuestionsMutableLiveData().observe(this, jsonObject -> {


            List<Fragment> fragments = buildFragments(jsonObject);
            ViewPager mPager = findViewById(R.id.view_pager);
            MyFragmentPageAdapter mPageAdapter = new MyFragmentPageAdapter(QuestionsActivity.this, getSupportFragmentManager(), fragments);
            mPager.setAdapter(mPageAdapter);

            //Add a new Fragment to the list with bundle
           /* Bundle b = new Bundle();
            //b.putInt("position", i);
            String title = "asd";

            mPageAdapter.add(L1Fragment.class,b);
            mPageAdapter.notifyDataSetChanged();*/
        });

    }

    private List<Fragment> buildFragments(@NonNull JsonObject jsonObject) {
        List<Fragment> fragments = new ArrayList<Fragment>();


        Log.d("jaonobj aize", String.valueOf(jsonObject.size()));
        int f = 0;
        int totalQuestions = jsonObject.size();
        for (int i = 1; i < totalQuestions + 1; i++) {

            JsonObject j = jsonObject.getAsJsonArray(String.valueOf(i)).get(0).getAsJsonObject();
            Gson gson = new Gson();
            Question question = gson.fromJson(j, Question.class);
            String qtype = j.get("q_type").toString();
            String ss = "\"p1\"";

            if (jsonObject.get(String.valueOf(i)).getAsJsonArray().size() > 0 && qtype.contains("\"l1\"")) {

                fragments.add(f, L1Fragment.newInstance(i, totalQuestions, jsonObject.getAsJsonArray(String.valueOf(i)).toString()));
            }
            if (qtype.contains("\"p1\"")) {

                fragments.add(f, P1Fragment.newInstance(i, totalQuestions, j.toString()));
            }
            if (qtype.contains("\"p2\"")) {

                fragments.add(f, P2Fragment.newInstance(i, totalQuestions, j.toString()));
            }
            if (qtype.contains("\"p3\"")) {

                fragments.add(f, P3Fragment.newInstance(i, totalQuestions, j.toString()));
            }
            if (qtype.contains("\"q\"")) {

                fragments.add(f, QFragment.newInstance(i, totalQuestions, j.toString()));
            }
            if (qtype.contains("\"qp1\"")) {

                fragments.add(f, QP1Fragment.newInstance(i, totalQuestions, j.toString()));
            }
            if (qtype.contains("\"qp2\"")) {

                fragments.add(f, QP2Fragment.newInstance(i, totalQuestions, j.toString()));
            }
            if (qtype.contains("\"qp3\"")) {

                fragments.add(f, QP3Fragment.newInstance(i, totalQuestions, j.toString()));
            }
            f++;
        }


        return fragments;
    }
}