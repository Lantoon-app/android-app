package com.bazinga.lantoonnew.home.chapter.lesson;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bazinga.lantoonnew.R;
import com.bazinga.lantoonnew.Tags;
import com.bazinga.lantoonnew.home.HomeActivity;
import com.bazinga.lantoonnew.home.chapter.lesson.model.PostLessonResponse;

public class LessonCompletedPopup {
    public LessonCompletedPopup() {

    }

    public void showPopupWindow(final View view, Activity activity, PostLessonResponse postLessonResponse, int quesNo, String strTimeSpent) {

        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_lesson_completed, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = false;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        TextView tvTitleMsg = popupView.findViewById(R.id.tvTitleMsg);
        TextView tvTotalTimeSpendPopupLessonCompleted = popupView.findViewById(R.id.tvTotalTimeSpendPopupLessonCompleted);

        Button btnExitPopupLessonCompleted = popupView.findViewById(R.id.btnExitPopupLessonCompleted);
        Button btnContinuePopupLessonCompleted = popupView.findViewById(R.id.btnContinuePopupLessonCompleted);
        if (postLessonResponse != null) {
            if (postLessonResponse.getContinuenext().getTargetType().equals("evaluation")) {
                if (postLessonResponse.getPostLessonData().getContinueStatus() == 0) {
                    tvTitleMsg.setText(postLessonResponse.getPostLessonData().getFailedChapters());
                    btnContinuePopupLessonCompleted.setText("RETAKE");
                }
                tvTotalTimeSpendPopupLessonCompleted.setText("Time Spend (" + postLessonResponse.getPostLessonData().getEvaluationScore().getSpentTime() + ")");
            } else {
                tvTitleMsg.setText("You have successfully completed!!");
                tvTotalTimeSpendPopupLessonCompleted.setText("Time Spend (" + strTimeSpent + ")");
            }

           /* if (postLessonResponse.getContinuenext().getLessonno() == 1)
                tvTitleMsg.setText(activity.getResources().getString(R.string.successfully_completed_the_chapter));
            else
                tvTitleMsg.setText(activity.getResources().getString(R.string.successfully_completed_the_lesson));*/
        }

        if (QuestionsActivity.isRandomQuestion || quesNo != QuestionsActivity.totalQues)
            btnContinuePopupLessonCompleted.setVisibility(View.GONE);
        if (postLessonResponse != null) {
            if (postLessonResponse.getContinuenext().getChapterno() > Integer.valueOf(postLessonResponse.getContinuenext().getUnlockedChapters())) {
                btnContinuePopupLessonCompleted.setEnabled(false);
            }
        }
        btnExitPopupLessonCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

                activity.startActivityForResult(new Intent(activity, HomeActivity.class), 2);
            }
        });


        btnContinuePopupLessonCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                //activity.startActivityForResult(new Intent(activity,QuestionsActivity.class),22);
                activity.finish();
                Intent intent = new Intent(activity, QuestionsActivity.class);
                intent.putExtra(Tags.TAG_IS_NEW_CHAPTER, true);
                intent.putExtra(Tags.TAG_SPENT_TIME, "0");
                intent.putExtra(Tags.TAG_START_QUESTION_NO, 1);
                intent.putExtra(Tags.TAG_LANGUAGE_ID, Integer.parseInt(postLessonResponse.getContinuenext().getLangid()));
                intent.putExtra(Tags.TAG_LESSON_NO, postLessonResponse.getContinuenext().getLessonno());
                if (postLessonResponse.getContinuenext().getTargetType().equals("chapter")) {
                    intent.putExtra(Tags.TAG_IS_EVALUATION_QUESTIONS, false);
                    intent.putExtra(Tags.TAG_CHAPTER_NO, postLessonResponse.getContinuenext().getChapterno());
                    intent.putExtra(Tags.TAG_CHAPTER_TYPE, postLessonResponse.getContinuenext().getChapterType());
                } else if (postLessonResponse.getContinuenext().getTargetType().equals("evaluation")) {
                    intent.putExtra(Tags.TAG_IS_EVALUATION_QUESTIONS, true);
                    intent.putExtra(Tags.TAG_CHAPTER_NO, postLessonResponse.getContinuenext().getEvaluation_id());
                    intent.putExtra(Tags.TAG_CHAPTER_TYPE, 2);
                }

                activity.startActivity(intent);
                //overridePendingTransition(0, 0);
            }
        });
        QuestionsActivity.clearData();

        //Handler for clicking on the inactive zone of the window

       /* popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                //popupWindow.dismiss();
                return true;
            }
        });*/
    }
}
