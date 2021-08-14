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

import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.NetworkUtil;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.Tags;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        if (mChapterList.size() > 0) {
            Chapter item = getItem(position);
            if (item != null) {
                mChapterList.remove(position);
                notifyItemRemoved(position);
            }
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

        @RequiresApi(api = Build.VERSION_CODES.O)
        public ViewHolder(View itemView) {
            super(itemView);
            //pbChapter = new ProgressBar(activity).findViewById(R.id.pbChapter);
            //pbChapter.setProgress(20);
            pbChapter = itemView.findViewById(R.id.pbChapter);
            tvChapter = itemView.findViewById(R.id.tvChapterNumber);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            ivDisabled = itemView.findViewById(R.id.ivDisabled);
            ivLock = itemView.findViewById(R.id.ivLock);
            //pbChapter.setMax(100);
            //pbChapter.setMin(0);

        }

        @Override
        public int getCurrentPosition() {
            return super.getCurrentPosition();
        }

        public ProgressBar getPbChapter() {
            return pbChapter;
        }

        @Override
        protected void clear() {

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void onBind(int position) {
            super.onBind(position);

            if (Integer.valueOf(mChapterList.get(position).getChapterNo()) <= continueNext.getChapterno() && Integer.valueOf(mChapterList.get(position).getChapterNo()) <= Integer.valueOf(continueNext.getunlockedChapters())) {


                switch (mChapterList.get(position).getCompletedLessons()) {
                    case 0:
                        getPbChapter().setProgress(1);
                        break;
                    case 25:
                        getPbChapter().setProgress(25);
                        break;
                    case 50:
                        getPbChapter().setProgress(50);
                        break;
                    case 75:
                        getPbChapter().setProgress(75);
                        break;
                    case 100:
                        getPbChapter().setProgress(100);
                        break;
                }

                ratingBar.setRating(mChapterList.get(position).getGemcount());
                tvChapter.setText("CHAPTER " + mChapterList.get(position).getChapterNo());
                tvChapter.setVisibility(View.VISIBLE);
                ivLock.setVisibility(View.INVISIBLE);
                ivDisabled.setVisibility(View.INVISIBLE);


            } else {

                tvChapter.setVisibility(View.INVISIBLE);
                ivLock.setVisibility(View.VISIBLE);
                ivDisabled.setVisibility(View.VISIBLE);
                ratingBar.setRating(0);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Integer.valueOf(mChapterList.get(position).getChapterNo()) <= continueNext.getChapterno() && Integer.valueOf(mChapterList.get(position).getChapterNo()) <= Integer.valueOf(continueNext.getunlockedChapters())) {
                        //Toast.makeText(activity,"Test",Toast.LENGTH_SHORT).show();
                        if (NetworkUtil.getConnectivityStatus(activity) != 0) {
                            Intent intent = new Intent(activity, QuestionsActivity.class);
                            if (Integer.valueOf(mChapterList.get(position).getChapterNo()) == continueNext.getChapterno()) {
                                intent.putExtra(Tags.TAG_IS_NEW_CHAPTER, true);
                                intent.putExtra(Tags.TAG_IS_RANDOM_QUESTIONS, false);
                                intent.putExtra(Tags.TAG_LANGUAGE_ID, continueNext.getLangid());
                                intent.putExtra(Tags.TAG_CHAPTER_NO, continueNext.getChapterno());
                                intent.putExtra(Tags.TAG_LESSON_NO, continueNext.getLessonno());
                                intent.putExtra(Tags.TAG_SPENT_TIME, continueNext.getSpentTime());
                                intent.putExtra(Tags.TAG_START_QUESTION_NO, continueNext.getStartingquesno());
                            } else if (mChapterList.get(position).getActiveLesson() != null && mChapterList.get(position).getActiveLesson().getLessonno() > 0) {
                                intent.putExtra(Tags.TAG_IS_NEW_CHAPTER, false);
                                intent.putExtra(Tags.TAG_IS_RANDOM_QUESTIONS, false);
                                intent.putExtra(Tags.TAG_LANGUAGE_ID, Integer.valueOf(mChapterList.get(position).getLanguageName()));
                                intent.putExtra(Tags.TAG_CHAPTER_NO, Integer.valueOf(mChapterList.get(position).getChapterNo()));
                                intent.putExtra(Tags.TAG_LESSON_NO, mChapterList.get(position).getActiveLesson().getLessonno());
                                intent.putExtra(Tags.TAG_SPENT_TIME, "0");
                                intent.putExtra(Tags.TAG_START_QUESTION_NO, mChapterList.get(position).getActiveLesson().getStartingquesno());
                            } else if (mChapterList.get(position).getActiveLesson() != null && mChapterList.get(position).getActiveLesson().getLessonno() == 0) {
                                Log.d("isRandomQuestion adap", "true");
                                Log.d("isRandomQuestion gem", mChapterList.get(position).getGemcount().toString());
                                intent.putExtra(Tags.TAG_IS_NEW_CHAPTER, false);
                                intent.putExtra(Tags.TAG_IS_RANDOM_QUESTIONS, true);
                                intent.putExtra(Tags.TAG_LANGUAGE_ID, Integer.valueOf(mChapterList.get(position).getLanguageName()));
                                intent.putExtra(Tags.TAG_CHAPTER_NO, Integer.valueOf(mChapterList.get(position).getChapterNo()));
                                intent.putExtra(Tags.TAG_LESSON_NO, 0);
                                intent.putExtra(Tags.TAG_SPENT_TIME, "0");
                                intent.putExtra(Tags.TAG_START_QUESTION_NO, 0);
                            }
                            activity.startActivity(intent);
                        } else {
                            CommonFunction.netWorkErrorAlert(activity);
                        }

                    }

                }
            });


            Log.d("chapters ", "Completed " + mChapterList.get(position).getCompletedLessons() + " Active " + continueNext.getStartingquesno() + " Cno " + mChapterList.get(position).getChapterNo());
           /* if (mChapterList.get(position).getCompletedLessons() == 0) {

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