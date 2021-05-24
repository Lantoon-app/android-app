package com.bazinga.lantoon.home.chapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bazinga.lantoon.BuildConfig;
import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.GetStartActivity;
import com.bazinga.lantoon.NetworkUtil;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.home.HomeActivity;
import com.bazinga.lantoon.home.chapter.adapter.ChapterAdapter;
import com.bazinga.lantoon.home.chapter.model.Chapter;
import com.bazinga.lantoon.home.chapter.model.ChapterResponse;
import com.bazinga.lantoon.home.chapter.utils.EqualSpacingItemDecoration;
import com.bazinga.lantoon.home.chapter.utils.PaginationScrollListener;
import com.bazinga.lantoon.login.SessionManager;
import com.bazinga.lantoon.login.forget.ForgetPasswordActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class ChapterFragment extends Fragment {

    private ChapterViewModel chapterViewModel;
    ChapterAdapter mChapterAdapter;
    GridLayoutManager mLayoutManager;
    SessionManager sessionManager;
    RecyclerView mRecyclerView;
    public static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 100;
    private boolean isLoading = false;
    int itemCount = 0;
    boolean fragmentDestroyed = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sessionManager = new SessionManager(getContext());
        chapterViewModel = new ViewModelProvider(this,
                new ChapterViewModelFactory(sessionManager.getLearLang(), sessionManager.getUid())).get(ChapterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_chapter, container, false);
        mRecyclerView = root.findViewById(R.id.rvChapter);

        mChapterAdapter = new ChapterAdapter(new ArrayList<Chapter>(), getActivity(), sessionManager.getLearLang());
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        //mLayoutManager.checkLayoutParams(new GridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new EqualSpacingItemDecoration(0, EqualSpacingItemDecoration.GRID));
        mRecyclerView.setAdapter(mChapterAdapter);
        preparedListItem();
        /**
         * add scroll listener while user reach in bottom load more will call
         */

        mRecyclerView.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                chapterViewModel.getData(currentPage, sessionManager.getLearLang(), sessionManager.getUid());
                preparedListItem();

            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        System.out.println("Chapter Current page no = " + String.valueOf(currentPage));
        return root;
    }

   /* @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        mChapterAdapter.clear();
        chapterViewModel.getData(currentPage,mSwipeRefreshLayout,sessionManager.getUserDetails().getLearnlang(),sessionManager.getUserDetails().getUid());
        preparedListItem();
    }*/


    private void preparedListItem() {
        System.out.println("Chapter preparedListItem page no = " + String.valueOf(currentPage));
        if (NetworkUtil.getConnectivityStatus(getContext()) != 0) {
            chapterViewModel.getChapters().observe(getActivity(), new Observer<ChapterResponse>() {

                @Override
                public void onChanged(ChapterResponse chapterResponse) {
                    if (!fragmentDestroyed) {
                        if (chapterResponse == null) {
                            mChapterAdapter.removeLoading();
                            isLastPage = true;
                            isLoading = false;
                        } else {
                            if (chapterResponse.getStatus().getCode() == 1032) {
                                if (ChapterFragment.this.currentPage != PAGE_START)
                                    mChapterAdapter.removeLoading();
                                mChapterAdapter.addAll(chapterResponse.getData(), chapterResponse.getContinuenext());
                                mChapterAdapter.notifyDataSetChanged();
                                if (ChapterFragment.this.currentPage < totalPage)
                                    mChapterAdapter.addLoading();
                                else isLastPage = true;
                                isLoading = false;
                            } else if (chapterResponse.getStatus().getCode() == 1111) {
                                appUpdateAlert(chapterResponse.getStatus().getMessage());
                            }
                        }
                    }
                }
            });
        } else {


            //CommonFunction.netWorkErrorAlert(getActivity());
            // Snackbar.make(activity.getCurrentFocus().getRootView(), activity.getString(R.string.msg_network_failed), Snackbar.LENGTH_SHORT).show();
        }

    }

    private void appUpdateAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        //Setting message manually and performing action on button click
        builder.setTitle("App New Version Alert !");
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final String appPackageName = BuildConfig.APPLICATION_ID; // getPackageName() from Context or Activity object
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        /*try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }*/
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().finishAffinity();
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Alert");
        alert.show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentDestroyed = true;
    }

}