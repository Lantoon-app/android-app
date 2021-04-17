package com.bazinga.lantoon.home.chapter.lesson;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.Utils;
import com.bazinga.lantoon.home.HomeActivity;
import com.bazinga.lantoon.home.chapter.lesson.model.PostLessonResponse;

import static com.bazinga.lantoon.home.chapter.lesson.QuestionsActivity.mPager;
import static com.bazinga.lantoon.home.chapter.lesson.QuestionsActivity.startTime;

public class LessonCompletedPopup {
    public LessonCompletedPopup() {

    }

    public void showPopupWindow(final View view, Activity activity, PostLessonResponse postLessonResponse, int quesNo) {

        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_lesson_completed, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        long endTime = System.currentTimeMillis();
         /*int seconds = ((endTime - startTime) / 1000) % 60);
       int minutes = ((endTime - startTime) / (1000*60)) % 60);
        int hours   = ((endTime - startTime) / (1000*60*60)) % 24);*/
        long totalSecs = ((endTime - startTime) / 1000) % 60;
        long totalMins = ((endTime - startTime) / (1000 * 60)) % 60;

        long responseSecs = totalSecs / QuestionsActivity.totalQues;
        long responseMins = totalMins / QuestionsActivity.totalQues;
        //Initialize the elements of our window, install the handler

        TextView tvTotalTimeSpendPopupLessonCompleted = popupView.findViewById(R.id.tvTotalTimeSpendPopupLessonCompleted);
        TextView tvResponseTimeSpendPopupLessonCompleted = popupView.findViewById(R.id.tvResponseTimeSpendPopupLessonCompleted);
        tvTotalTimeSpendPopupLessonCompleted.setText("Time Spend (" + totalMins + "." + totalSecs + ")");
        tvResponseTimeSpendPopupLessonCompleted.setText("Response time (" + responseMins + "." + responseSecs + ")");

        Button btnExitPopupLessonCompleted = popupView.findViewById(R.id.btnExitPopupLessonCompleted);
        btnExitPopupLessonCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

                /*Intent intent = new Intent(activity, QuestionsActivity.class);
                intent.putExtra(Utils.TAG_LANGUAGE_ID, body.getContinuenext().getLangid());
                intent.putExtra(Utils.TAG_CHAPTER_NO, body.getContinuenext().getChapterno());
                intent.putExtra(Utils.TAG_LESSON_NO, body.getContinuenext().getLessonno());
                activity.startActivity(intent);*/
                activity.startActivityForResult(new Intent(activity, HomeActivity.class), 2);
            }
        });
        Button btnContinuePopupLessonCompleted = popupView.findViewById(R.id.btnContinuePopupLessonCompleted);
        if (QuestionsActivity.isRandomQuestion || !QuestionsActivity.isNewChapter || quesNo != QuestionsActivity.totalQues)
            btnContinuePopupLessonCompleted.setVisibility(View.GONE);

        /*if (quesNo != QuestionsActivity.totalQues || !QuestionsActivity.isNewChapter)
            btnContinuePopupLessonCompleted.setVisibility(View.GONE);*/
        btnContinuePopupLessonCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                //activity.startActivityForResult(new Intent(activity,QuestionsActivity.class),22);
                activity.finish();
                Intent intent = new Intent(activity, QuestionsActivity.class);
                intent.putExtra(Utils.TAG_IS_NEW_CHAPTER, true);
                intent.putExtra(Utils.TAG_LANGUAGE_ID, postLessonResponse.getContinuenext().getLangid());
                intent.putExtra(Utils.TAG_CHAPTER_NO, postLessonResponse.getContinuenext().getChapterno());
                intent.putExtra(Utils.TAG_LESSON_NO, postLessonResponse.getContinuenext().getLessonno());
                intent.putExtra(Utils.TAG_START_QUESTION_NO, 1);
                activity.startActivity(intent);
                //overridePendingTransition(0, 0);
            }
        });
        QuestionsActivity.clearData();

        //Handler for clicking on the inactive zone of the window

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                //popupWindow.dismiss();
                return true;
            }
        });
    }
}
