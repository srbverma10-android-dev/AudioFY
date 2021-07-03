package com.sourabhverma.audiofy.Notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.sourabhverma.audiofy.R;
import com.sourabhverma.audiofy.helperClasses.BottomSheetHelperClass;
import com.sourabhverma.audiofy.helperClasses.MusicService;
import com.sourabhverma.audiofy.models.RUnderR;
import com.sourabhverma.audiofy.models.upperSlideImages;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.sourabhverma.audiofy.utils.Constants.TAG;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String ACTION_PLAY = "PLAY";
    public static final String ACTION_PREV = "PREV";
    public static final String ACTION_NEXT = "NEXT";

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("CURRENT_SONG", MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

        MusicService musicService = new MusicService();

        String actionName = intent.getAction();
        Log.d(TAG, "onReceive: " + actionName);
        
        if (actionName != null) {
            
            switch (actionName) {
                
                case ACTION_PLAY : {
                    if (musicService.isPlaying()) {
                        updateUi(context,sharedPreferencesEditor,2, null, null);
                        musicService.MediaPlayerPause(context);
                    } else {
                        updateUi(context,sharedPreferencesEditor,1, null, null);
                        musicService.MediaPlayerPlay(context);
                    }
                    break;
                }
                
                case ACTION_PREV : {

                    break;
                }
                
                case ACTION_NEXT : {

                    Log.d(TAG, "onReceive: action Next");
                    break;

                }
                default:{
                    Log.d(TAG, "onReceive: ");
                    break;
                }
                
            }
            
        }


    }

    private Bitmap changeStringToBitmap(String songPhoto) {
        Bitmap decodedImage;
        byte[] imageInByte = Base64.decode(songPhoto, Base64.DEFAULT);
        decodedImage = BitmapFactory.decodeByteArray(imageInByte,0, imageInByte.length);

        return decodedImage;
    }

    private void updateUi(Context context, SharedPreferences.Editor sharedPreferencesEditor, int i, List<upperSlideImages> upperSlideImagesList, List<RUnderR> rUnderRS) {

//        sharedPreferencesEditor.putString("SONG_ID",);
//        sharedPreferencesEditor.putString("SONG_NAME",);
//        sharedPreferencesEditor.putString("SONG_PHOTO",);
        sharedPreferencesEditor.putInt("IS_PLAYING", i);
        sharedPreferencesEditor.apply();


    }

}
