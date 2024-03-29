package com.bazinga.lantoon.home.leaderboard;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.home.chapter.utils.PaginationScrollListener;
import com.bazinga.lantoon.home.leaderboard.model.Leader;
import com.bazinga.lantoon.home.leaderboard.model.LeaderResponse;
import com.bazinga.lantoon.home.leaderboard.model.MyLeaderData;
import com.bazinga.lantoon.login.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


public class LeaderFragment extends Fragment {

    SessionManager sessionManager;
    private LeaderViewModel leaderViewModel;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    LeaderAdapter leaderAdapter;
    LinearLayoutManager mLayoutManager;
    TabLayout tablayout_leaderbord;
    View footerView;
    public static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    boolean isLoading = false;
    private boolean isLastPage = false;
    boolean fragmentDestroyed = false;

    RelativeLayout rlFull;
    //footer
    ImageView ivLeaderItemFooter,iv_leader_footer_flag;
    TextView tvUserNameLeaderItemFooter, tvRankLeaderItemFooter, tvGemCountLeaderItemFooter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sessionManager = new SessionManager(getContext());
        leaderViewModel = new ViewModelProvider(this).get(LeaderViewModel.class);

        View root = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        progressBar = root.findViewById(R.id.pbLeader);
        tablayout_leaderbord = root.findViewById(R.id.tablayout_leaderbord);
        rlFull = root.findViewById(R.id.rlFull);
        //rlFull.setVisibility(View.GONE);
        recyclerView = root.findViewById(R.id.rvLeader);
        initFooter(root);
        leaderAdapter = new LeaderAdapter(new ArrayList<Leader>(), getContext());
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(leaderAdapter);
        isLoading = true;
        preparedListItem(0);
        //initScrollListener();
        tablayout_leaderbord.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0)
                    preparedListItem(0);
                if (tab.getPosition() == 1)
                    preparedListItem(1);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return root;
    }

    private void initFooter(View root) {
        footerView = root.findViewById(R.id.footer);
        ivLeaderItemFooter = root.findViewById(R.id.ivLeaderItemFooter);
        iv_leader_footer_flag = root.findViewById(R.id.iv_leader_footer_flag);
        tvUserNameLeaderItemFooter = root.findViewById(R.id.tvUserNameLeaderItemFooter);
        tvRankLeaderItemFooter = root.findViewById(R.id.tvRankLeaderItemFooter);
        tvGemCountLeaderItemFooter = root.findViewById(R.id.tvGemCountLeaderItemFooter);
        footerView.setVisibility(View.GONE);
    }


    private void preparedListItem(int type) {
        if (leaderAdapter.getItemCount() > 0) {
            leaderAdapter.clear();
        }
        leaderViewModel.getLeaderData(type, 1, sessionManager.getUid(), sessionManager.getLearnLangId());
        progressBar.setVisibility(View.VISIBLE);
        leaderViewModel.getLeaders().observe(getViewLifecycleOwner(), new Observer<LeaderResponse>() {

            @Override
            public void onChanged(LeaderResponse leaderResponse) {

                if (leaderResponse != null) {
                    if (!fragmentDestroyed) {
                        isLoading = false;
                        leaderAdapter.addAll(leaderResponse.getData(),type);
                        if (leaderResponse.getMyLeaderData() != null)
                            setFooter(leaderResponse.getMyLeaderData());
                    /*else {
                        MyLeaderData myLeaderData = new MyLeaderData();
                        myLeaderData.setGemcount(0);
                        myLeaderData.setPicture(null);
                        myLeaderData.setRank(0);
                        myLeaderData.setUname(sessionManager.getUserDetails().getUname());
                        setFooter(myLeaderData);
                    }*/
                        progressBar.setVisibility(View.INVISIBLE);
                        rlFull.setVisibility(View.VISIBLE);
                    }
                } else {
                    //CommonFunction.netWorkErrorAlert(getActivity());
                }
            }
        });

    }


    private void setFooter(@NonNull MyLeaderData myLeaderData) {
        if (myLeaderData.getPicture() != null) {
            footerView.setVisibility(View.VISIBLE);
            Glide.with(this).load(myLeaderData.getPicture()).circleCrop().addListener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    ivLeaderItemFooter.setBackground(getActivity().getDrawable(R.drawable.icon_avatar));
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    ivLeaderItemFooter.setPadding(20, 20, 20, 20);
                    return false;
                }
            }).into(ivLeaderItemFooter);
        }
        /*if (myLeaderData.getUname().length() > 5)
            tvUserNameLeaderItemFooter.setText(myLeaderData.getUname().substring(0, 5) + "..");
        else
            tvUserNameLeaderItemFooter.setText(myLeaderData.getUname());*/
        if(myLeaderData.region_code.equals("896"))
            iv_leader_footer_flag.setImageDrawable(getContext().getDrawable(R.drawable.flag_turkey));
        else
            iv_leader_footer_flag.setImageDrawable(getContext().getDrawable(R.drawable.flag_india));

        tvUserNameLeaderItemFooter.setText("You");
        tvRankLeaderItemFooter.setText(String.valueOf(myLeaderData.getRank()));
        tvGemCountLeaderItemFooter.setText(String.valueOf(myLeaderData.getGemcount()));
    }


    private void initScrollListener(int type) {
        recyclerView.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                leaderViewModel.getLeaderData(type, currentPage, sessionManager.getUid(), sessionManager.getLearnLangId());
                preparedListItem(type);

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

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentDestroyed = true;
    }

    private void loadMore() {

        /*leaderArrayList.add(null);
        leaderAdapter.notifyItemInserted(leaderArrayList.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                leaderArrayList.remove(leaderArrayList.size() - 1);
                int scrollPosition = leaderArrayList.size();
                leaderAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                while (currentSize - 1 < nextLimit) {
                    leaderArrayList.add(currentSize);
                    currentSize++;
                }

                leaderAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);*/


    }
}