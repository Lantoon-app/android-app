package com.bazinga.lantoon.home.chapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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
import com.bazinga.lantoon.Tags;
import com.bazinga.lantoon.home.HomeActivity;
import com.bazinga.lantoon.home.chapter.adapter.ChapterAdapter;
import com.bazinga.lantoon.home.chapter.model.Chapter;
import com.bazinga.lantoon.home.chapter.model.ChapterResponse;
import com.bazinga.lantoon.home.chapter.utils.EqualSpacingItemDecoration;
import com.bazinga.lantoon.home.chapter.utils.PaginationScrollListener;
import com.bazinga.lantoon.login.SessionManager;
import com.bazinga.lantoon.login.forget.ForgetPasswordActivity;
import com.google.android.material.appbar.AppBarLayout;
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
    ProgressBar progressBar;
    ImageView ivMaintenance;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sessionManager = new SessionManager(getContext());

        @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        chapterViewModel = new ViewModelProvider(this,
                new ChapterViewModelFactory(sessionManager.getLearLang(), sessionManager.getUid(), deviceId)).get(ChapterViewModel.class);
        System.out.println("deviceid " + deviceId);
        View root = inflater.inflate(R.layout.fragment_chapter, container, false);

        ivMaintenance = root.findViewById(R.id.ivMaintenance);
        //ivMaintenance.setVisibility(View.VISIBLE);
        mRecyclerView = root.findViewById(R.id.rvChapter);
        progressBar = root.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
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
                chapterViewModel.getData(currentPage, sessionManager.getLearLang(), sessionManager.getUid(), getActivity().getIntent().getStringExtra(Tags.TAG_DEVICE_ID));
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
            if (isLoading == false)
                progressBar.setVisibility(View.VISIBLE);
            chapterViewModel.getChapters().observe(getActivity(), new Observer<ChapterResponse>() {

                @Override
                public void onChanged(ChapterResponse chapterResponse) {
                    progressBar.setVisibility(View.GONE);
                    if (!fragmentDestroyed) {
                        if (chapterResponse == null) {
                            mChapterAdapter.removeLoading();
                            isLastPage = true;
                            isLoading = false;
                        } else {
                            if (chapterResponse.getStatus().getCode() == 1032) {
                                //ivMaintenance.setVisibility(View.GONE);
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
                            } else if (chapterResponse.getStatus().getCode() == 2043) {
                                Toast.makeText(getContext(), chapterResponse.getStatus().getMessage(), Toast.LENGTH_LONG).show();
                                sessionManager.logoutUser();
                            } else if (chapterResponse.getStatus().getCode() == 20008) {
                                ivMaintenance.setVisibility(View.VISIBLE);
                                //appUpdateAlert(chapterResponse.getStatus().getMessage());
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
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_app_update, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = false;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler

        TextView tvMessage = popupView.findViewById(R.id.tvMessage);
        Button btnExit = popupView.findViewById(R.id.btnExit);
        Button btnUpdate = popupView.findViewById(R.id.btnUpdate);


        tvMessage.setText(msg);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                getActivity().finishAffinity();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // popupWindow.dismiss();
                final String appPackageName = BuildConfig.APPLICATION_ID; // getPackageName() from Context or Activity object
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        });


        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //popupWindow.dismiss();
                return true;
            }
        });

        /*AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        //Setting message manually and performing action on button click
        builder.setTitle("App New Version Alert !");
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final String appPackageName = BuildConfig.APPLICATION_ID; // getPackageName() from Context or Activity object
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        *//*try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }*//*
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
        alert.show();*/

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentDestroyed = true;
    }

}