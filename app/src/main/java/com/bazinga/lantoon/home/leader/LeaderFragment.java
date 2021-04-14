package com.bazinga.lantoon.home.leader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.home.chapter.utils.PaginationScrollListener;
import com.bazinga.lantoon.home.leader.model.Leader;
import com.bazinga.lantoon.home.leader.model.LeaderResponse;
import com.bazinga.lantoon.home.leader.model.MyLeaderData;
import com.bazinga.lantoon.login.SessionManager;

import java.util.ArrayList;


public class LeaderFragment extends Fragment {

    SessionManager sessionManager;
    private LeaderViewModel leaderViewModel;
    RecyclerView recyclerView;
    LeaderAdapter leaderAdapter;
    LinearLayoutManager mLayoutManager;
    public static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    boolean isLoading = false;
    private boolean isLastPage = false;

    RelativeLayout rlFull;
    //footer
    ImageView ivLeaderItemFooter;
    TextView tvUserNameLeaderItemFooter, tvRankLeaderItemFooter, tvGemCountLeaderItemFooter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sessionManager = new SessionManager(getContext());
        leaderViewModel = new ViewModelProvider(getActivity(),
                new LeaderViewModelProvider(sessionManager.getUserDetails().getUid(), sessionManager.getUserDetails().getLearnlang())).get(LeaderViewModel.class);

        View root = inflater.inflate(R.layout.fragment_leader, container, false);
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


        leaderViewModel.getLeaders().observe(getActivity(), new Observer<LeaderResponse>() {

            @Override
            public void onChanged(LeaderResponse leaderResponse) {
                if (leaderResponse != null) {
                    isLoading = false;
                    leaderAdapter.addAll(leaderResponse.getData());
                    setFooter(leaderResponse.getMyLeaderData());
                    rlFull.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void setFooter(MyLeaderData myLeaderData) {
        if (myLeaderData.getPicture() != null) {
            byte[] decodedString = Base64.decode(myLeaderData.getPicture(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            RoundedBitmapDrawable dr =
                    RoundedBitmapDrawableFactory.create(getResources(), decodedByte);
            dr.setGravity(Gravity.CENTER);
            dr.setCircular(true);
            ivLeaderItemFooter.setBackground(null);
            ivLeaderItemFooter.setImageDrawable(dr);
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
                leaderViewModel.getData(currentPage, sessionManager.getUserDetails().getUid(), sessionManager.getUserDetails().getLearnlang());
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