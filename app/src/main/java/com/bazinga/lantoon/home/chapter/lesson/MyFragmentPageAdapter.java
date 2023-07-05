package com.bazinga.lantoon.home.chapter.lesson;

import java.util.List;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

public class MyFragmentPageAdapter extends FragmentStateAdapter {

    public static int pos = 0;

    private List<Fragment> myFragments;
    //private ArrayList<String> categories;
    private Context context;

    public MyFragmentPageAdapter(FragmentActivity fragmentActivity, List<Fragment> myFrags) {
        super(fragmentActivity);
        myFragments = myFrags;
        //this.categories = cats;
        //this.context = c;
    }

   /* @Override
    public Fragment getItem(int position) {

        return myFragments.get(position);

    }

   @Override
    public int getCount() {

        return myFragments.size();
    }*/

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return myFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return myFragments.size();
    }

    /* @Override
    public CharSequence getPageTitle(int position) {

        setPos(position);
        return categories.get(position);
    }

    public static int getPos() {
        return pos;
    }

    public void add(Class<Fragment> c, Bundle b) {
        myFragments.add(Fragment.instantiate(context,c.getName(),b));
        //categories.add(title);
    }

    public static void setPos(int pos) {
        MyFragmentPageAdapter.pos = pos;
    }*/
}