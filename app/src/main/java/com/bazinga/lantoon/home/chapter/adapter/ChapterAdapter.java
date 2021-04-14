package com.bazinga.lantoon.home.chapter.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
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
import com.bazinga.lantoon.Utils;
import com.bazinga.lantoon.home.chapter.BaseViewHolder;
import com.bazinga.lantoon.home.chapter.lesson.QuestionsActivity;
import com.bazinga.lantoon.home.chapter.lesson.model.ContinueNext;
import com.bazinga.lantoon.home.chapter.model.Chapter;

import java.util.List;


public class ChapterAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    Activity activity;
    int learnlang;
    private ContinueNext continueNext;
    private List<Chapter> mChapterList;
    private Callback mCallback;

    public ChapterAdapter(List<Chapter> chapter, Activity activity, Integer learnlang) {
        this.mChapterList = chapter;
        this.activity = activity;
        this.learnlang = learnlang;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter, parent, false));
            case VIEW_TYPE_LOADING:
                return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter_loading, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }


    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == mChapterList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return mChapterList == null ? 0 : mChapterList.size();
    }

    public void add(Chapter response) {
        mChapterList.add(response);
        notifyItemInserted(mChapterList.size() - 1);
    }

    public void addAll(List<Chapter> postItems, ContinueNext continueNext) {
        this.continueNext = continueNext;
        for (Chapter response : postItems) {
            add(response);
        }
    }


    private void remove(Chapter postItems) {
        int position = mChapterList.indexOf(postItems);
        if (position > -1) {
            mChapterList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addLoading() {
        isLoaderVisible = true;
        add(new Chapter());
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = mChapterList.size() - 1;
        Chapter item = getItem(position);
        if (item != null) {
            mChapterList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    Chapter getItem(int position) {
        return mChapterList.get(position);
    }


    public interface Callback {
        void onRepoEmptyViewRetryClick();
    }

    public class ViewHolder extends BaseViewHolder {

        TextView tvChapter;
        ImageView ivDisabled, ivLock;
        ProgressBar pbChapter;
        RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            tvChapter = itemView.findViewById(R.id.tvChapterNumber);
            pbChapter = itemView.findViewById(R.id.pbChapter);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            ivDisabled = itemView.findViewById(R.id.ivDisabled);
            ivLock = itemView.findViewById(R.id.ivLock);
            pbChapter.setProgress(0);

        }

        protected void clear() {

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void onBind(int position) {
            super.onBind(position);
            Chapter mChapter = mChapterList.get(position);
            tvChapter.setText("CHAPTER " + mChapter.getChapterNo());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(activity,"Test",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity, QuestionsActivity.class);
                    if(Integer.valueOf(mChapter.getChapterNo()) == continueNext.getChapterno()) {
                        intent.putExtra(Utils.TAG_LANGUAGE_ID, continueNext.getLangid());
                        intent.putExtra(Utils.TAG_CHAPTER_NO, continueNext.getChapterno());
                        intent.putExtra(Utils.TAG_LESSON_NO, continueNext.getLessonno());
                        intent.putExtra(Utils.TAG_START_QUESTION_NO, continueNext.getStartingquesno());
                    }else {
                        intent.putExtra(Utils.TAG_LANGUAGE_ID, continueNext.getLangid());
                        intent.putExtra(Utils.TAG_CHAPTER_NO, Integer.valueOf(mChapter.getChapterNo()));
                        intent.putExtra(Utils.TAG_LESSON_NO, 1);
                        intent.putExtra(Utils.TAG_START_QUESTION_NO, 1);
                    /*intent.putExtra(Utils.TAG_CHAPTER_NO, Integer.valueOf(mChapter.getChapterNo()));
                    if (continueNext.getLessonno() != 4)
                        intent.putExtra(Utils.TAG_LESSON_NO, continueNext.getLessonno() + 1);
                    else intent.putExtra(Utils.TAG_LESSON_NO, mChapter.getCompletedLessons());
                    intent.putExtra(Utils.TAG_START_QUESTION_NO,continueNext.getStartingquesno());*/

                    }
                    activity.startActivity(intent);
                }
            });

            Log.d("chapters ", "Completed "+mChapter.getCompletedLessons()+ " Active "+continueNext.getStartingquesno() + " Cno "+mChapterList.get(position).getChapterNo());
           /* if (mChapter.getCompletedLessons() == 0) {

            }*/
            pbChapter.setProgress(mChapter.getCompletedLessons());

            if (Integer.valueOf(mChapterList.get(position).getChapterNo()) <= continueNext.getChapterno()) {


                tvChapter.setVisibility(View.VISIBLE);
                ivLock.setVisibility(View.INVISIBLE);
                ivDisabled.setVisibility(View.INVISIBLE);
                ratingBar.setRating(mChapter.getCompletedLessons());


            }else {

                tvChapter.setVisibility(View.INVISIBLE);
                ivLock.setVisibility(View.VISIBLE);
                ivDisabled.setVisibility(View.VISIBLE);
                ratingBar.setNumStars(0);
            }


        }
    }

    public class EmptyViewHolder extends BaseViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        protected void clear() {

        }

    }
}