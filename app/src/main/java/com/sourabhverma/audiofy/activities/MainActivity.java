package com.sourabhverma.audiofy.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.sourabhverma.audiofy.R;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ImageView logo ;
    private Pair<View, String> pairs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo = findViewById(R.id.imageView);
        Handler handler = new Handler();

        handler.postDelayed(() -> {

            if (isSignedIn()) {



                SharedPreferences sharedPreferences = getSharedPreferences("CURRENT_SONG",MODE_PRIVATE);
                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                sharedPreferencesEditor.putString("SONG_ID", null);
                sharedPreferencesEditor.putString("SONG_NAME", null);
                sharedPreferencesEditor.putString("SONG_PHOTO", null);
                sharedPreferencesEditor.putInt("IS_PLAYING", -1);
                sharedPreferencesEditor.putInt("MAX_DURATION", -1);
                sharedPreferencesEditor.apply();

                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pairs = new Pair<>(logo, "logo");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
                startActivity(intent, options.toBundle());
            }

        }, 2000);

    }

    private boolean isSignedIn() {

        return GoogleSignIn.getLastSignedInAccount(this) != null;

    }

}