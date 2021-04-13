package com.bazinga.lantoon.home.leader;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.home.chapter.ChapterFragment;
import com.bazinga.lantoon.home.chapter.ChapterViewModel;
import com.bazinga.lantoon.home.chapter.ChapterViewModelFactory;
import com.bazinga.lantoon.home.chapter.adapter.ChapterAdapter;
import com.bazinga.lantoon.home.chapter.model.Chapter;
import com.bazinga.lantoon.home.chapter.model.ChapterResponse;
import com.bazinga.lantoon.home.chapter.utils.EqualSpacingItemDecoration;
import com.bazinga.lantoon.home.chapter.utils.PaginationScrollListener;
import com.bazinga.lantoon.home.leader.model.Leader;
import com.bazinga.lantoon.home.leader.model.LeaderResponse;
import com.bazinga.lantoon.login.SessionManager;

import java.util.ArrayList;


public class LeaderFragment extends Fragment {

    SessionManager sessionManager;
    private LeaderViewModel leaderViewModel;
    RecyclerView mRecyclerView;
    LeaderAdapter mLeaderAdapter;
    public static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 3;
    private boolean isLoading = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sessionManager = new SessionManager(getContext());
        leaderViewModel = new ViewModelProvider(getActivity(),
                new LeaderViewModelProvider(sessionManager.getUserDetails().getUid())).get(LeaderViewModel.class);

        View root = inflater.inflate(R.layout.fragment_leader, container, false);
        mRecyclerView = root.findViewById(R.id.rvLeader);

        mLeaderAdapter = new LeaderAdapter(new ArrayList<Leader>(), getActivity(), sessionManager.getUserDetails().getUid());
        mRecyclerView.setHasFixedSize(true);
        /*
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new EqualSpacingItemDecoration(0, EqualSpacingItemDecoration.GRID));
        mRecyclerView.setAdapter(mLeaderAdapter);
        preparedListItem();

        mRecyclerView.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                leaderViewModel.getData(currentPage, sessionManager.getUserDetails().getUid());
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
        System.out.println("Chapter Current page no = " + String.valueOf(currentPage));*/

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
        System.out.println("Leader preparedListItem page no = " + String.valueOf(currentPage));

        leaderViewModel.getLeaders().observe(getActivity(), new Observer<LeaderResponse>() {

            @Override
            public void onChanged(LeaderResponse leaderResponse) {
                if (leaderResponse == null) {
                    //mLeaderAdapter.removeLoading();
                    isLastPage = true;
                    isLoading = false;
                } else {
                    if (LeaderFragment.this.currentPage != PAGE_START)
                        mLeaderAdapter.removeLoading();
                    mLeaderAdapter.addAll(leaderResponse.getData());
                    if (LeaderFragment.this.currentPage < totalPage) mLeaderAdapter.addLoading();
                    else isLastPage = true;
                    isLoading = false;
                }

            }
        });
    }
}