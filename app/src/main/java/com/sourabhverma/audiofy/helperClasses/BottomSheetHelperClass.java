package com.sourabhverma.audiofy.helperClasses;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.sourabhverma.audiofy.R;
import com.sourabhverma.audiofy.datatypeconverters.ArrayToString;
import com.sourabhverma.audiofy.models.RUnderR;
import com.sourabhverma.audiofy.models.upperSlideImages;
import com.sourabhverma.audiofy.roomdatabase.ArtistIdAndSongIdDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;
import static com.sourabhverma.audiofy.utils.Constants.TAG;

public class BottomSheetHelperClass {

    private final View view1;

    private final ImageView imageViewInToolbar;
    private final TextView textViewInToolbar;
    private final ImageView imageViewInBottomSheet;
    private final TextView textViewInBottomSheet;

    private final ImageView playPauseButtonInToolBar;
    private final ImageView playPauseButtonInBottomSheet;

    private final ImageView nextSongInToolBar;
    private final ImageView previousSongInToolBar;

    private final ImageView nextSongInBottomSheet;
    private final ImageView previousSongInBottomSheet;

    private final ProgressBar progressBar;
    private final ProgressBar progressBar1;

    private final TextView artist;

    private final TextView seekBarStartTime;
    private final TextView seekBarEndTime;

    private final SeekBar seekBar;

    private static MusicService musicService;
    private boolean mBound = false;

    private MusicService.MusicServiceBinder musicServiceBinder;

    private final Handler handler = new Handler();

    private final ArtistIdAndSongIdDatabase artistIdAndSongIdDatabase;

    public static void getInstance(Activity context, List<upperSlideImages> upperSlideImagesList, ArrayList<RUnderR> arrayList, int position) {
        new BottomSheetHelperClass(context, upperSlideImagesList, arrayList, position);
    }

    private BottomSheetHelperClass (Activity context, List<upperSlideImages> upperSlideImagesList,ArrayList<RUnderR> arrayList, int position) {

        ServiceConnection mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

                musicServiceBinder =
                        (MusicService.MusicServiceBinder) iBinder;

                if (upperSlideImagesList == null) {
                    musicService = musicServiceBinder.getService(arrayList.get(position).getSongId(), context);
                } else {
                    musicService = musicServiceBinder.getService(upperSlideImagesList.get(position).getSongId(), context);
                }
                mBound = true;
                Log.d(TAG, "onServiceConnected: ");

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        Intent intent = new Intent(context, MusicService.class);
        context.bindService(intent, mServiceConnection,Context.BIND_AUTO_CREATE);

        SharedPreferences sharedPreferences = context.getSharedPreferences("CURRENT_SONG", Context.MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
        view1 = context.findViewById(R.id.bottomSheet);
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(view1);
        Toolbar toolbar = context.findViewById(R.id.toolBarInBottomSheet);
        ImageView cutImage = context.findViewById(R.id.cutImageView);
        playPauseButtonInToolBar = context.findViewById(R.id.PlayPauseButtonInToolBar);
        playPauseButtonInBottomSheet = context.findViewById(R.id.PlayPauseButtonInBottomSheet);
        imageViewInToolbar = context.findViewById(R.id.imageViewInToolBar);
        textViewInToolbar = context.findViewById(R.id.textViewInToolBar);
        imageViewInBottomSheet = context.findViewById(R.id.viewPagerBottomSheet);
        textViewInBottomSheet = context.findViewById(R.id.textViewInBottomSheet);
        nextSongInToolBar = context.findViewById(R.id.NextSongInToolBar);
        previousSongInToolBar = context.findViewById(R.id.PreviousSongInToolBar);
        nextSongInBottomSheet = context.findViewById(R.id.NextSongInBottomSheet);
        previousSongInBottomSheet = context.findViewById(R.id.PreviousSongInBottomSheet);
        progressBar = context.findViewById(R.id.progressBarInBottomSheet);
        progressBar1 = context.findViewById(R.id.progressBarInBottomSheet1);
        seekBarStartTime = context.findViewById(R.id.seekBarStartTime);
        seekBarEndTime = context.findViewById(R.id.seekBarEndTime);
        seekBar = context.findViewById(R.id.seekBarInBottomSheet);
        artist = context.findViewById(R.id.textViewInBottomSheet2);
        artistIdAndSongIdDatabase = ArtistIdAndSongIdDatabase.getInstance(context);

        textViewInToolbar.setSelected(true);
        textViewInBottomSheet.setSelected(true);
        artist.setSelected(true);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {


                toolbar.setAlpha(1-slideOffset);
                imageViewInBottomSheet.setAlpha(slideOffset);
                cutImage.setAlpha(slideOffset);

                cutImage.setEnabled(cutImage.getAlpha() != 0);
                toolbar.setEnabled(toolbar.getAlpha() != 0);


            }
        });
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (mBound){

                    int mCurrentPosition = musicService.getCurrentPositon();
                    seekBar.setProgress(mCurrentPosition/1000);
                    seekBarStartTime.setText(formetedTime(mCurrentPosition/1000));
                }

                handler.post(this);

            }
        });
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {

            StringBuilder artistNameInString;
            artistNameInString = new StringBuilder();

            if (arrayList != null) {
                Log.d(TAG, "run: " + ArrayToString.fromString(artistIdAndSongIdDatabase.artistIdAndSongIdDAO().get(arrayList.get(position).getSongId())));
            } else {
                ArrayList<String> stringArrayList = ArrayToString.fromString(artistIdAndSongIdDatabase.artistIdAndSongIdDAO().get(upperSlideImagesList.get(position).getSongId()));
                if (stringArrayList != null) {
                    for(int j = 0 ; j < stringArrayList.size(); j++){
                        artistNameInString.append(stringArrayList.get(j)).append(" | ");
                    }
                    artist.setText(artistNameInString);
                }
            }
        });



        cutImage.setOnClickListener(view -> bottomSheetBehavior.setState(STATE_COLLAPSED));

        toolbar.setOnClickListener(view -> {
            Log.d(TAG, "onClick: toolbar ");
            bottomSheetBehavior.setState(STATE_EXPANDED);
        });

        onClicks(sharedPreferences, context, upperSlideImagesList, arrayList, position);
    }

    SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            switch (key) {
                case "SONG_PHOTO": {
                    String songPhoto = sharedPreferences.getString(key, "");
                    imageViewInToolbar.setImageBitmap(changeStringToBitmap(songPhoto));
                    imageViewInBottomSheet.setImageBitmap(changeStringToBitmap(songPhoto));
                    break;
                }
                case "SONG_NAME": {
                    String songName = sharedPreferences.getString(key, "yash");
                    textViewInToolbar.setText(songName);
                    textViewInBottomSheet.setText(songName);
                    break;
                }
                case "MAX_DURATION": {
                    seekBar.setMax(sharedPreferences.getInt(key, 0) / 1000);
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                            if (mBound && b) {
                                musicService.setSeekTo(i * 1000);
                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });
                    seekBarEndTime.setText(formetedTime(sharedPreferences.getInt(key, 0) / 1000));
                    break;
                }
                case "IS_PLAYING": {
                    int isPlaying = sharedPreferences.getInt(key, 0);
                    switch (isPlaying) {
                        case -1: {
                            Log.d(TAG, "onSharedPreferenceChanged: case -1 ");
                            view1.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case 1: {
                            Log.d(TAG, "onSharedPreferenceChanged: case 1 ");
                            view1.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            progressBar1.setVisibility(View.GONE);
                            playPauseButtonInToolBar.setVisibility(View.VISIBLE);
                            playPauseButtonInToolBar.setImageResource(R.drawable.pause);
                            playPauseButtonInBottomSheet.setVisibility(View.VISIBLE);
                            playPauseButtonInBottomSheet.setImageResource(R.drawable.pause);
                            break;
                        }
                        case 2:
                        case 3: {
                            Log.d(TAG, "onSharedPreferenceChanged: case 2 3  ");
                            view1.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            progressBar1.setVisibility(View.GONE);
                            playPauseButtonInToolBar.setVisibility(View.VISIBLE);
                            playPauseButtonInToolBar.setImageResource(R.drawable.play);
                            playPauseButtonInBottomSheet.setVisibility(View.VISIBLE);
                            playPauseButtonInBottomSheet.setImageResource(R.drawable.play);
                            break;
                        }
                        default: {
                            view1.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.VISIBLE);
                            progressBar1.setVisibility(View.VISIBLE);
                            playPauseButtonInToolBar.setVisibility(View.GONE);
                            playPauseButtonInBottomSheet.setVisibility(View.GONE);
                            break;
                        }

                    }

                }

            }

        }
    };

    private String formetedTime(int mCurrentPosition) {

        String totalOut;
        String totalNew;
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition /60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if (seconds.length() == 1){
            return totalNew;
        } else {
            return totalOut;
        }
    }

    private void onClicks(SharedPreferences sharedPreferences, Activity context, List<upperSlideImages> upperSlideImagesList, ArrayList<RUnderR> arrayList, int position) {

        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

        playPauseButtonInToolBar.setOnClickListener(view -> {

            if (mBound){

                if (sharedPreferences.getInt("IS_PLAYING",0) == 1){
                    musicService.MediaPlayerPause(context);
                } else if (sharedPreferences.getInt("IS_PLAYING",0) == 2) {
                    musicService.MediaPlayerPlay(context);
                }

            }

        });

        playPauseButtonInBottomSheet.setOnClickListener(view -> {

            if (mBound){

                if (sharedPreferences.getInt("IS_PLAYING",0) == 1){
                    musicService.MediaPlayerPause(context);
                } else if (sharedPreferences.getInt("IS_PLAYING",0) == 2) {
                    musicService.MediaPlayerPlay(context);
                }

            }

        });

        previousSongInToolBar.setOnClickListener(view -> {
            if (arrayList != null) {
                if (position-1 >= 0 && position-1 < arrayList.size()) {
                    BottomSheetHelperClass.getInstance(context, upperSlideImagesList, arrayList,position-1);
                    UpdateUiOfBottomSheet(sharedPreferencesEditor, upperSlideImagesList, position-1);
                } else {
                    BottomSheetHelperClass.getInstance(context, upperSlideImagesList,arrayList,position);
                    UpdateUiOfBottomSheet(sharedPreferencesEditor, upperSlideImagesList, position);
                }
            } else {
                if (position-1 >= 0 && position-1 < upperSlideImagesList.size()) {
                    BottomSheetHelperClass.getInstance(context, upperSlideImagesList, null,position-1);
                    UpdateUiOfBottomSheet(sharedPreferencesEditor, upperSlideImagesList, position-1);
                } else {
                    BottomSheetHelperClass.getInstance(context, upperSlideImagesList,null,position);
                    UpdateUiOfBottomSheet(sharedPreferencesEditor, upperSlideImagesList, position);
                }
            }
        });

        nextSongInToolBar.setOnClickListener(view -> {
            if (arrayList != null) {
                if (position+1 >= 0 && position+1 < arrayList.size()) {
                    Log.d(TAG, "onClick: in if ");

                    BottomSheetHelperClass.getInstance(context,upperSlideImagesList,arrayList,position+1);
                    UpdateUiOfBottomSheet(sharedPreferencesEditor, upperSlideImagesList, position+1);
                } else {
                    Log.d(TAG, "onClick: in else ");

                    BottomSheetHelperClass.getInstance(context,upperSlideImagesList,arrayList,position);
                    UpdateUiOfBottomSheet(sharedPreferencesEditor, upperSlideImagesList, position);
                }
            } else {
                if (position+1 >= 0 && position+1 < upperSlideImagesList.size()) {
                    Log.d(TAG, "onClick: in if ");
                    BottomSheetHelperClass.getInstance(context,upperSlideImagesList,null,position+1);
                    UpdateUiOfBottomSheet(sharedPreferencesEditor, upperSlideImagesList, position+1);
                } else {
                    Log.d(TAG, "onClick: in else ");
                    BottomSheetHelperClass.getInstance(context,upperSlideImagesList,null,position);
                    UpdateUiOfBottomSheet(sharedPreferencesEditor, upperSlideImagesList, position);
                }
            }
        });

        previousSongInBottomSheet.setOnClickListener(view -> {
            if (arrayList != null) {
                if (position-1 >= 0 && position-1 <= arrayList.size()) {
                    BottomSheetHelperClass.getInstance(context,upperSlideImagesList,arrayList,position-1);
                    UpdateUiOfBottomSheet(sharedPreferencesEditor, upperSlideImagesList, position-1);
                } else {
                    BottomSheetHelperClass.getInstance(context,upperSlideImagesList,arrayList,position);
                    UpdateUiOfBottomSheet(sharedPreferencesEditor, upperSlideImagesList, position);
                }
            } else {
                if (position-1 >= 0 && position-1 <= upperSlideImagesList.size()) {
                    BottomSheetHelperClass.getInstance(context,upperSlideImagesList,null,position-1);
                    UpdateUiOfBottomSheet(sharedPreferencesEditor, upperSlideImagesList, position-1);
                } else {
                    BottomSheetHelperClass.getInstance(context,upperSlideImagesList,null,position);
                    UpdateUiOfBottomSheet(sharedPreferencesEditor, upperSlideImagesList, position);
                }
            }
        });

        nextSongInBottomSheet.setOnClickListener(view -> {
            if (arrayList != null) {
                if (position+1 >= 0 && position+1 <= arrayList.size()) {
                    BottomSheetHelperClass.getInstance(context,upperSlideImagesList,arrayList,position+1);
                    UpdateUiOfBottomSheet(sharedPreferencesEditor, upperSlideImagesList, position+1);
                } else {
                    BottomSheetHelperClass.getInstance(context,upperSlideImagesList,arrayList,position);
                    UpdateUiOfBottomSheet(sharedPreferencesEditor, upperSlideImagesList, position);
                }
            } else {
                if (position+1 >= 0 && position+1 <= upperSlideImagesList.size()) {
                    BottomSheetHelperClass.getInstance(context,upperSlideImagesList,null,position+1);
                    UpdateUiOfBottomSheet(sharedPreferencesEditor, upperSlideImagesList, position+1);
                } else {
                    BottomSheetHelperClass.getInstance(context,upperSlideImagesList,null,position);
                    UpdateUiOfBottomSheet(sharedPreferencesEditor, upperSlideImagesList, position);
                }
            }
        });

    }

    private void UpdateUiOfBottomSheet(SharedPreferences.Editor sharedPreferencesEditor, List<upperSlideImages> upperSlideImagesList, int position) {

        sharedPreferencesEditor.putString("SONG_ID", upperSlideImagesList.get(position).getSongId());
        sharedPreferencesEditor.putString("SONG_NAME", upperSlideImagesList.get(position).getSongName());
        sharedPreferencesEditor.putString("SONG_PHOTO", upperSlideImagesList.get(position).getSongPhoto());
        sharedPreferencesEditor.putInt("IS_PLAYING", 0);
        sharedPreferencesEditor.apply();

    }


    private Bitmap changeStringToBitmap(String songPhoto) {

        Bitmap decodedImage;
        byte[] imageInByte = Base64.decode(songPhoto, Base64.DEFAULT);
        decodedImage = BitmapFactory.decodeByteArray(imageInByte,0, imageInByte.length);

        return decodedImage;
    }


}

