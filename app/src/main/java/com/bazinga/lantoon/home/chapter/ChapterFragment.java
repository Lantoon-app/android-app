package com.bazinga.lantoon.home.chapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.home.chapter.adapter.ChapterAdapter;
import com.bazinga.lantoon.home.chapter.lesson.QuestionsViewModel;
import com.bazinga.lantoon.home.chapter.lesson.QuestionsViewModelFactory;
import com.bazinga.lantoon.home.chapter.model.Chapter;
import com.bazinga.lantoon.home.chapter.model.ChapterResponse;
import com.bazinga.lantoon.home.chapter.utils.EqualSpacingItemDecoration;
import com.bazinga.lantoon.home.chapter.utils.PaginationScrollListener;
import com.bazinga.lantoon.home.chapter.widget.HorizontalSwipeRefreshLayout;
import com.bazinga.lantoon.login.SessionManager;


import java.util.ArrayList;
import java.util.List;


public class ChapterFragment extends Fragment {

    private ChapterViewModel chapterViewModel;
    ChapterAdapter mChapterAdapter;
    GridLayoutManager mLayoutManager;
    SessionManager sessionManager;
    RecyclerView mRecyclerView;
    public static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 3;
    private boolean isLoading = false;
    int itemCount = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sessionManager = new SessionManager(getContext());
        chapterViewModel = new ViewModelProvider(this,
                new ChapterViewModelFactory(sessionManager.getUserDetails().getLearnlang(), sessionManager.getUserDetails().getUid())).get(ChapterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_chapter, container, false);
        mRecyclerView = root.findViewById(R.id.rvChapter);

        mChapterAdapter = new ChapterAdapter(new ArrayList<Chapter>(), getActivity(), sessionManager.getUserDetails().getLearnlang());
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
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
                chapterViewModel.getData(currentPage, sessionManager.getUserDetails().getLearnlang(), sessionManager.getUserDetails().getUid());
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

        chapterViewModel.getChapters().observe(getActivity(), new Observer<ChapterResponse>() {

            @Override
            public void onChanged(ChapterResponse chapterResponse) {
                if (chapterResponse == null) {
                    mChapterAdapter.removeLoading();
                    isLastPage = true;
                    isLoading = false;
                } else {
                    if (ChapterFragment.this.currentPage != PAGE_START)
                        mChapterAdapter.removeLoading();
                    mChapterAdapter.addAll(chapterResponse.getData(), chapterResponse.getContinuenext());
                    mChapterAdapter.notifyDataSetChanged();
                    if (ChapterFragment.this.currentPage < totalPage) mChapterAdapter.addLoading();
                    else isLastPage = true;
                    isLoading = false;
                }

            }
        });
    }


}