package com.bazinga.lantoon.home.leader;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public abstract class LeaderBaseViewHolder extends RecyclerView.ViewHolder {

    private int mCurrentPosition;

    public LeaderBaseViewHolder(View itemView) {
        super(itemView);
    }

    protected abstract void clear();

    public void onBind(int position) {
        mCurrentPosition = position;
        clear();
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

}


