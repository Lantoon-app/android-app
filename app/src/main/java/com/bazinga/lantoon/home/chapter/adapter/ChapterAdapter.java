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
        final ProgressBar pbChapter;
        RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            //pbChapter = new ProgressBar(activity).findViewById(R.id.pbChapter);
            //pbChapter.setProgress(20);
            pbChapter = itemView.findViewById(R.id.pbChapter);
            tvChapter = itemView.findViewById(R.id.tvChapterNumber);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            ivDisabled = itemView.findViewById(R.id.ivDisabled);
            ivLock = itemView.findViewById(R.id.ivLock);
            pbChapter.setProgress(0);

        }

        @Override
        protected void clear() {

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void onBind(int position) {
            super.onBind(position);
            Chapter mChapter = mChapterList.get(position);
            pbChapter.setProgress(mChapter.getCompletedLessons());
            ratingBar.setRating(mChapter.getGemcount());
            tvChapter.setText("CHAPTER " + mChapter.getChapterNo());
            if (Integer.valueOf(mChapterList.get(position).getChapterNo()) <= continueNext.getChapterno()) {


                tvChapter.setVisibility(View.VISIBLE);
                ivLock.setVisibility(View.INVISIBLE);
                ivDisabled.setVisibility(View.INVISIBLE);



            } else {

                tvChapter.setVisibility(View.INVISIBLE);
                ivLock.setVisibility(View.VISIBLE);
                ivDisabled.setVisibility(View.VISIBLE);
                ratingBar.setNumStars(0);
            }
            if (Integer.valueOf(mChapter.getChapterNo()) <= continueNext.getChapterno()) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(activity,"Test",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, QuestionsActivity.class);
                        if (Integer.valueOf(mChapter.getChapterNo()) == continueNext.getChapterno()) {
                            intent.putExtra(Utils.TAG_IS_NEW_CHAPTER, true);
                            intent.putExtra(Utils.TAG_IS_RANDOM_QUESTIONS,false);
                            intent.putExtra(Utils.TAG_LANGUAGE_ID, continueNext.getLangid());
                            intent.putExtra(Utils.TAG_CHAPTER_NO, continueNext.getChapterno());
                            intent.putExtra(Utils.TAG_LESSON_NO, continueNext.getLessonno());
                            intent.putExtra(Utils.TAG_START_QUESTION_NO, continueNext.getStartingquesno());
                        } else if (mChapter.getActiveLesson() != null && mChapter.getActiveLesson().getLessonno() >0) {
                            intent.putExtra(Utils.TAG_IS_NEW_CHAPTER, false);
                            intent.putExtra(Utils.TAG_IS_RANDOM_QUESTIONS,false);
                            intent.putExtra(Utils.TAG_LANGUAGE_ID, Integer.valueOf(mChapter.getLanguageName()));
                            intent.putExtra(Utils.TAG_CHAPTER_NO, Integer.valueOf(mChapter.getChapterNo()));
                            intent.putExtra(Utils.TAG_LESSON_NO, mChapter.getActiveLesson().getLessonno());
                            intent.putExtra(Utils.TAG_START_QUESTION_NO, mChapter.getActiveLesson().getStartingquesno());
                        }else if (mChapter.getActiveLesson() != null && mChapter.getActiveLesson().getLessonno() == 0){
                            Log.d("isRandomQuestion adap","true" );
                            Log.d("isRandomQuestion gem",mChapter.getGemcount().toString() );
                            intent.putExtra(Utils.TAG_IS_NEW_CHAPTER, false);
                            intent.putExtra(Utils.TAG_IS_RANDOM_QUESTIONS,true);
                            intent.putExtra(Utils.TAG_LANGUAGE_ID, Integer.valueOf(mChapter.getLanguageName()));
                            intent.putExtra(Utils.TAG_CHAPTER_NO, Integer.valueOf(mChapter.getChapterNo()));
                            intent.putExtra(Utils.TAG_LESSON_NO, 0);
                            intent.putExtra(Utils.TAG_START_QUESTION_NO,0);
                        }

                        activity.startActivity(intent);
                    }
                });
            }

            Log.d("chapters ", "Completed " + mChapter.getCompletedLessons() + " Active " + continueNext.getStartingquesno() + " Cno " + mChapterList.get(position).getChapterNo());
           /* if (mChapter.getCompletedLessons() == 0) {

            }*/


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