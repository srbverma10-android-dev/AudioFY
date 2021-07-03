package com.sourabhverma.audiofy.helperClasses;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sourabhverma.audiofy.utils.Constants.TAG;

public class MusicService extends Service {

    private final Binder mBinder = new MusicServiceBinder();

    private final GetSongBase64String getSongBase64String = GetSongBase64String.retrofit.create(GetSongBase64String.class);

    private static MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG, "onRebind: ");
    }

    public class MusicServiceBinder extends Binder {
        public com.sourabhverma.audiofy.helperClasses.MusicService getService(String songId, Context context){
            Log.d(TAG, "getService: ");

            getDataFromApi(songId,context);
            
            otherFeatures(context);

            return com.sourabhverma.audiofy.helperClasses.MusicService.this;
        }

    }

    public boolean isPlaying () {
        return mediaPlayer.isPlaying();
    }

    public int getCurrentPositon(){
        return mediaPlayer.getCurrentPosition();
    }

    private void otherFeatures(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("CURRENT_SONG",MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        
        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            Log.d(TAG, "onCompletion: ");
            SharedPreferences sharedPreferences1 = context.getSharedPreferences("CURRENT_SONG",MODE_PRIVATE);
            SharedPreferences.Editor sharedPreferencesEditor1 = sharedPreferences1.edit();
            sharedPreferencesEditor1.putString("SONG_ID", null);
            sharedPreferencesEditor1.putString("SONG_NAME", null);
            sharedPreferencesEditor1.putString("SONG_PHOTO", null);
            sharedPreferencesEditor1.putInt("IS_PLAYING", -1);
            sharedPreferencesEditor1.putInt("MAX_DURATION", -1);
            sharedPreferencesEditor1.apply();
            mediaPlayer.stop();
            mediaPlayer.reset();
        });
        
        mediaPlayer.setOnPreparedListener(mediaPlayer -> {
            sharedPreferencesEditor.putInt("MAX_DURATION", mediaPlayer.getDuration());
            sharedPreferencesEditor.apply();
        });

        
    }

    public void setSeekTo(int i){
        mediaPlayer.seekTo(i);
    }

    public MusicService() {
        Log.d(TAG, "MusicService: ");
    }
    
    private void getDataFromApi(String songId, Context context){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Call<String> dataCall = getSongBase64String.getMp3(songId);
            dataCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                    Log.d(TAG, "onResponse: " + response.body());
                    byte[] bytes = Base64.decode(response.body(),0);
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        try {
                            mediaPlayer.setDataSource(context, Uri.fromFile(Mp3ToFileConverter(bytes)));
                            mediaPlayer.prepare();
                            MediaPlayerPlay(context);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        mediaPlayer = MediaPlayer.create(context,Uri.fromFile(Mp3ToFileConverter(bytes)));
                        MediaPlayerPlay(context);
                    }
                }

                @Override
                public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {

                    Log.d(TAG, "onFailure: " + t);

                }
            });
        });
    }

    private File Mp3ToFileConverter(byte[] bytearray) {
        try {
            File tempFile = File.createTempFile("mobile", "mp3", getCacheDir());
            tempFile.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(bytearray);
            fos.close();
            return tempFile;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return mBinder;
    }

    public void MediaPlayerPlay(Context context) {
        mediaPlayer.start();

        SharedPreferences sharedPreferences = context.getSharedPreferences("CURRENT_SONG",MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putInt("IS_PLAYING", 1);
        sharedPreferencesEditor.apply();
    }


    public void MediaPlayerPause(Context context) {
        mediaPlayer.pause();
        SharedPreferences sharedPreferences = context.getSharedPreferences("CURRENT_SONG",MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putInt("IS_PLAYING", 2);
        sharedPreferencesEditor.apply();
    }

    @Override
    public void onDestroy() {

        Log.d(TAG, "onDestroy: ");

        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {

        Log.d(TAG, "onUnbind: ");

        return super.onUnbind(intent);
    }
}