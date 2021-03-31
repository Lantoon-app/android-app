package com.bazinga.lantoon.home.chapter.lesson;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.home.chapter.lesson.model.Help;
import com.bazinga.lantoon.home.chapter.lesson.model.HelpData;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpPopup {
    //PopupWindow display method
HelpData helpData;
MediaPlayer mediaPlayer;
    public HelpPopup(String type,int reflanguageid,int chapterno, int lessonno, String cellval){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Help> call = apiInterface.getQuestionHelp(reflanguageid, chapterno, lessonno,cellval);
        call.enqueue(new Callback<Help>() {
            @Override
            public void onResponse(Call<Help> call, Response<Help> response) {

                if (response.isSuccessful()) {
                    if(response.body().getStatus().getCode() == 1018)
                    helpData = response.body().getData();
                    Log.d("response ", new GsonBuilder().setPrettyPrinting().create().toJson(response.body().getData()));


                } else {
                    Log.e("response message= ", response.message() + response.code());
                }


            }

            @Override
            public void onFailure(Call<Help> call, Throwable t) {

                call.cancel();
                t.printStackTrace();
                Log.e("response ERROR= ", "" + t.getMessage() + " " + t.getLocalizedMessage());
            }
        });
    }

    public void showPopupWindow(final View view) {


        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_help, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler

        TextView test2 = popupView.findViewById(R.id.tvQuestionNameHelpPopup);
        test2.setText(helpData.getWord());

        Button btnAudioHelpPopup = popupView.findViewById(R.id.btnAudioHelpPopup);
        btnAudioHelpPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playAudio(helpData.getAudioPath());

            }
        });
        Button btnAudioSlowHelpPopup = popupView.findViewById(R.id.btnAudioSlowHelpPopup);
        btnAudioSlowHelpPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio(helpData.getAudioPath());
            }
        });



        //Handler for clicking on the inactive zone of the window

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

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
