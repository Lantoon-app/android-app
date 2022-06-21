package com.bazinga.lantoonnew.home.leaderboard;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bazinga.lantoonnew.R;
import com.bazinga.lantoonnew.home.leaderboard.model.Leader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class LeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public List<Leader> mItemList;
    Context context;


    public LeaderAdapter(List<Leader> itemList, Context context) {

        this.mItemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leader, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leader_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ItemViewHolder) {

            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void add(Leader response) {
        mItemList.add(response);
        notifyItemInserted(mItemList.size() - 1);
    }

    public void addAll(List<Leader> postItems) {

        for (Leader response : postItems) {
            add(response);
        }
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        View view;
        ImageView ivLeaderItem;
        TextView tvUserNameLeaderItem, tvRankLeaderItem, tvGemCountLeaderItem;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ivLeaderItem = itemView.findViewById(R.id.ivLeaderItem);
            tvUserNameLeaderItem = itemView.findViewById(R.id.tvUserNameLeaderItem);
            tvRankLeaderItem = itemView.findViewById(R.id.tvRankLeaderItem);
            tvGemCountLeaderItem = itemView.findViewById(R.id.tvGemCountLeaderItem);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        Leader leader = mItemList.get(position);
        if (leader.getRank() == 1) {
            //viewHolder.view.setBackgroundResource(R.drawable.bg_item_leader_first_rank);
            viewHolder.tvUserNameLeaderItem.setTextColor(Color.WHITE);
            //viewHolder.tvRankLeaderItem.setTextColor(Color.WHITE);
            viewHolder.tvGemCountLeaderItem.setTextColor(Color.WHITE);
        } else {
            //viewHolder.view.setBackgroundResource(R.drawable.bg_item_leader_all_rank);
            //viewHolder.tvUserNameLeaderItem.setTextColor(Color.GRAY);
            //viewHolder.tvRankLeaderItem.setTextColor(Color.GRAY);
            //viewHolder.tvGemCountLeaderItem.setTextColor(Color.GRAY);
            if (leader.getPicture() != null) {
           /* byte[] decodedString = Base64.decode(leader.getPicture(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            RoundedBitmapDrawable dr =
                    RoundedBitmapDrawableFactory.create(viewHolder.itemView.getResources(), decodedByte);
            dr.setGravity(Gravity.CENTER);
            dr.setCircular(true);
            viewHolder.ivLeaderItem.setBackground(null);
            viewHolder.ivLeaderItem.setImageDrawable(dr);*/
                Glide.with(context).load(leader.getPicture()).circleCrop().addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        viewHolder.ivLeaderItem.setBackground(context.getDrawable(R.drawable.icon_avatar));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        return false;
                    }
                }).into(viewHolder.ivLeaderItem);
            }

        }
        if (leader.getUname().length() > 5)
            viewHolder.tvUserNameLeaderItem.setText(leader.getUname().substring(0, 5) + "..");
        else
            viewHolder.tvUserNameLeaderItem.setText(leader.getUname());
        viewHolder.tvRankLeaderItem.setText(String.valueOf(leader.getRank()));
        viewHolder.tvGemCountLeaderItem.setText(String.valueOf(leader.getGemcount()));
    }


}