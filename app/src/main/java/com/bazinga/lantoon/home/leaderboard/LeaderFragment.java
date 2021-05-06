package com.bazinga.lantoon.home.leaderboard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
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

import java.util.ArrayList;


public class LeaderFragment extends Fragment {

    SessionManager sessionManager;
    private LeaderViewModel leaderViewModel;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    LeaderAdapter leaderAdapter;
    LinearLayoutManager mLayoutManager;
    public static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    boolean isLoading = false;
    private boolean isLastPage = false;
    boolean fragmentDestroyed = false;

    RelativeLayout rlFull;
    //footer
    ImageView ivLeaderItemFooter;
    TextView tvUserNameLeaderItemFooter, tvRankLeaderItemFooter, tvGemCountLeaderItemFooter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sessionManager = new SessionManager(getContext());
        leaderViewModel = new ViewModelProvider(getActivity(),
                new LeaderViewModelProvider(sessionManager.getUid(), sessionManager.getLearLang())).get(LeaderViewModel.class);

        View root = inflater.inflate(R.layout.fragment_leader, container, false);
        progressBar = root.findViewById(R.id.pbLeader);
        rlFull = root.findViewById(R.id.rlFull);
        rlFull.setVisibility(View.GONE);
        recyclerView = root.findViewById(R.id.rvLeader);
        initFooter(root);
        leaderAdapter = new LeaderAdapter(new ArrayList<Leader>(), getContext());
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(leaderAdapter);
        isLoading = true;
        preparedListItem();
        //initScrollListener();

        return root;
    }

    private void initFooter(View root) {

        ivLeaderItemFooter = root.findViewById(R.id.ivLeaderItemFooter);
        tvUserNameLeaderItemFooter = root.findViewById(R.id.tvUserNameLeaderItemFooter);
        tvRankLeaderItemFooter = root.findViewById(R.id.tvRankLeaderItemFooter);
        tvGemCountLeaderItemFooter = root.findViewById(R.id.tvGemCountLeaderItemFooter);
    }


    private void preparedListItem() {
        progressBar.setVisibility(View.VISIBLE);

        leaderViewModel.getLeaders().observe(getActivity(), new Observer<LeaderResponse>() {

            @Override
            public void onChanged(LeaderResponse leaderResponse) {

                if (leaderResponse != null) {
                    if (!fragmentDestroyed) {
                        isLoading = false;
                        leaderAdapter.addAll(leaderResponse.getData());
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
                }
            }
        });
    }

    private void setFooter(@NonNull MyLeaderData myLeaderData) {
        if (myLeaderData.getPicture() != null) {
           /* byte[] decodedString = Base64.decode(myLeaderData.getPicture(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            RoundedBitmapDrawable dr =
                    RoundedBitmapDrawableFactory.create(getResources(), decodedByte);
            dr.setGravity(Gravity.CENTER);
            dr.setCircular(true);
            ivLeaderItemFooter.setBackground(null);
            ivLeaderItemFooter.setImageDrawable(dr);*/
            Glide.with(this).load(myLeaderData.getPicture()).circleCrop().addListener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    ivLeaderItemFooter.setBackground(getActivity().getDrawable(R.drawable.icon_avatar));
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(ivLeaderItemFooter);
        }
        tvUserNameLeaderItemFooter.setText(myLeaderData.getUname());
        tvRankLeaderItemFooter.setText(String.valueOf(myLeaderData.getRank()));
        tvGemCountLeaderItemFooter.setText(String.valueOf(myLeaderData.getGemcount()));
    }


    private void initScrollListener() {
        recyclerView.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                leaderViewModel.getData(currentPage, sessionManager.getUid(), sessionManager.getLearLang());
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