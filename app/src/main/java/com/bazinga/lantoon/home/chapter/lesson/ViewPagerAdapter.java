package com.bazinga.lantoon.home.chapter.lesson;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    private JsonObject jsonObject;

    public ViewPagerAdapter(Context context, FragmentManager fm, JsonObject jsonObject) {
        super(fm);
        this.mContext = context;
        this.jsonObject = jsonObject;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        //ArrayList<Question> arrayList = new ArrayList<Question>();
        if (jsonObject != null) {
            for (int i = 1; i < jsonObject.size() + 1; i++) {
                if (jsonObject.get(String.valueOf(i)).getAsJsonArray().size() > 1) {
                    Log.d("jsonObject L1", jsonObject.getAsJsonArray(String.valueOf(i)).toString());
                    //return L1Fragment.newInstance();
                    position = 0;
                } else {
                    //return QP1Fragment.newInstance();
                    position = 1;
                }
            }

        }

        switch (position) {
            case 0:
                return L1Fragment.newInstance();
            case 1:
                return P1Fragment.newInstance();
            case 2:
                return P2Fragment.newInstance();
            case 3:
                return P3Fragment.newInstance();
            case 4:
                return QFragment.newInstance();
            case 5:
                return QP1Fragment.newInstance();
            case 6:
                return QP2Fragment.newInstance();
            case 7:
                return QP3Fragment.newInstance();
            default:
                return null;
        }
        /*if (position == 0) {
            return L1Fragment.newInstance();
        } else {
            return P1Fragment.newInstance();
        }*/
        // return L1Fragment.newInstance();
    }


    @Override
    public int getCount() {
        // Show 2 total pages.
        return jsonObject.size() + 1;
    }
}
