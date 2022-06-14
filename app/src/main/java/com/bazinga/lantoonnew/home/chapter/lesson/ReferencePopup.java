package com.bazinga.lantoonnew.home.chapter.lesson;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bazinga.lantoonnew.R;
import com.bazinga.lantoonnew.home.chapter.lesson.model.Reference;

import java.io.IOException;

public class ReferencePopup {
    //PopupWindow display method
    Reference helpData;
    MediaPlayer mediaPlayer;

    public ReferencePopup(@NonNull Reference reference) {
        this.helpData = reference;
    }

    public void showPopupWindow(final View view) {


        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_reference, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = false;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler

        TextView tvQuestionNameHelpPopup = popupView.findViewById(R.id.tvQuestionNameHelpPopup);
        TextView tvQuestionNameHelpPopup2 = popupView.findViewById(R.id.tvQuestionNameHelpPopup2);
        Button btnAudioHelpPopup = popupView.findViewById(R.id.btnAudioHelpPopup);
        Button btnAudioSlowHelpPopup = popupView.findViewById(R.id.btnAudioSlowHelpPopup);
        Button btnAudioHelpPopup2 = popupView.findViewById(R.id.btnAudioHelpPopup2);
        Button btnAudioSlowHelpPopup2 = popupView.findViewById(R.id.btnAudioSlowHelpPopup2);
        ImageButton imgBtnClose = popupView.findViewById(R.id.imgBtnClose);
        LinearLayout llQtypes = popupView.findViewById(R.id.llQtypes);

        tvQuestionNameHelpPopup.setText(helpData.getWord());

        btnAudioHelpPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playAudio(helpData.getAudioPath());

            }
        });

        btnAudioSlowHelpPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio(helpData.getAudioPath());
            }
        });

        imgBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer!=null)
                    mediaPlayer.release();
                popupWindow.dismiss();
            }
        });


        if(helpData.getAnsWord().equals("")) {
            llQtypes.setVisibility(View.GONE);
        }else {
            llQtypes.setVisibility(View.VISIBLE);

            tvQuestionNameHelpPopup2.setText(helpData.getAnsWord());


            btnAudioHelpPopup2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    playAudio(helpData.getAnsAudioPath());

                }
            });

            btnAudioSlowHelpPopup2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playAudio(helpData.getAnsAudioPath());
                }
            });
        }


        //Handler for clicking on the inactive zone of the window

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mediaPlayer!=null)
                    mediaPlayer.release();
                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });
    }

    private void playAudio(String audioUrl) {


        // initializing media player
        mediaPlayer = new MediaPlayer();

        // below line is use to set the audio
        // stream type for our media player.
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // below line is use to set our
        // url to our media player.
        try {
            mediaPlayer.setDataSource(audioUrl);
            // below line is use to prepare
            // and start our media player.
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
