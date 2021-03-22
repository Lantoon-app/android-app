package com.bazinga.lantoon.home.chapter.lesson;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.home.chapter.lesson.ui.l1.L1Fragment;
import com.bazinga.lantoon.home.chapter.lesson.ui.p1.P1Fragment;
import com.bazinga.lantoon.home.chapter.lesson.ui.p2.P2Fragment;
import com.bazinga.lantoon.home.chapter.lesson.ui.p3.P3Fragment;
import com.bazinga.lantoon.home.chapter.lesson.ui.q.QFragment;
import com.bazinga.lantoon.home.chapter.lesson.ui.qp1.QP1Fragment;
import com.bazinga.lantoon.home.chapter.lesson.ui.qp2.QP2Fragment;
import com.bazinga.lantoon.home.chapter.lesson.ui.qp3.QP3Fragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    @StringRes
    private static final int[] TAB_TITLES = new int[] { R.string.tab_text_1, R.string.tab_text_2 };
    private final Context mContext;
    public ViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.

        switch (position)
        {
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
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }
    @Override
    public int getCount() {
        // Show 2 total pages.
        return 8;
    }
}
