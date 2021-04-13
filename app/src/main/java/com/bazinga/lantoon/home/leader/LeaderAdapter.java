package com.bazinga.lantoon.home.leader;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.home.leader.model.Leader;

import java.util.List;

public class LeaderAdapter extends RecyclerView.Adapter<LeaderBaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    Activity activity;
    private List<Leader> mLeaderList;
    private Callback mCallback;
    String strUserId;

    public LeaderAdapter(List<Leader> leaderList, Activity activity, String strUserId) {
        this.mLeaderList = leaderList;
        this.activity = activity;
        this.strUserId = strUserId;

    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @NonNull
    @Override
    public LeaderBaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leader, parent, false));
            case VIEW_TYPE_LOADING:
                return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderBaseViewHolder holder, int position) {
        holder.onBind(position);
    }


    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == mLeaderList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return mLeaderList == null ? 0 : mLeaderList.size();
    }

    public void add(Leader response) {
        mLeaderList.add(response);
        notifyItemInserted(mLeaderList.size() - 1);
    }

    public void addAll(List<Leader> postItems) {

        for (Leader response : postItems) {
            add(response);
        }
    }


    private void remove(Leader postItems) {
        int position = mLeaderList.indexOf(postItems);
        if (position > -1) {
            mLeaderList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addLoading() {
        isLoaderVisible = true;
        add(new Leader());
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = mLeaderList.size() - 1;
        Leader item = getItem(position);
        if (item != null) {
            mLeaderList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    Leader getItem(int position) {
        return mLeaderList.get(position);
    }


    public interface Callback {
        void onRepoEmptyViewRetryClick();
    }

    public class ViewHolder extends LeaderBaseViewHolder {

       /* TextView tvChapter;
        ImageView ivDisabled, ivLock;
        ProgressBar pbChapter;
        RatingBar ratingBar;*/

        public ViewHolder(View itemView) {
            super(itemView);
           /* tvChapter = itemView.findViewById(R.id.tvChapterNumber);
            pbChapter = itemView.findViewById(R.id.pbChapter);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            ivDisabled = itemView.findViewById(R.id.ivDisabled);
            ivLock = itemView.findViewById(R.id.ivLock);
            pbChapter.setProgress(0);*/

        }

        protected void clear() {

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void onBind(int position) {
            super.onBind(position);
            Leader mLeader = mLeaderList.get(position);
            //tvChapter.setText("CHAPTER " + mChapter.getChapterNo());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        }
    }

    public class EmptyViewHolder extends LeaderBaseViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        protected void clear() {

        }

    }
}