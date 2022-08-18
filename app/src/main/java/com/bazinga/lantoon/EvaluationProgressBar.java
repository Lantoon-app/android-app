package com.bazinga.lantoon;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class EvaluationProgressBar extends LinearLayout {

    public EvaluationProgressBar(Context context) {
        super(context);
    }

    public EvaluationProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        int[] intArray = new int[]{0, 1, 0, 1, 0, 1, 1, 0, 0, 1};
        intArray[1] = 1;
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //Image View for close button

        for (int i = 0; i < intArray.length; i++) {
            final ImageView imageView = new ImageView(context);
            imageView.setBackground(getResources().getDrawable(R.drawable.svg_evaluation_progress_default));
            if(intArray[i] == 1)
            imageView.setBackgroundTintList(context.getResources().getColorStateList(R.color.evaluation_answer_correct));
           if(intArray[i] == 0)
            imageView.setBackgroundTintList(context.getResources().getColorStateList(R.color.evaluation_answer_wrong));
            addView(imageView);
        }

    }
}