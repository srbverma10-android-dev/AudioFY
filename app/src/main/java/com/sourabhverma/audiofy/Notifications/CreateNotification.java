package com.sourabhverma.audiofy.Notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;

import androidx.core.app.NotificationCompat;

import com.sourabhverma.audiofy.R;
import com.sourabhverma.audiofy.models.RUnderR;
import com.sourabhverma.audiofy.models.upperSlideImages;

import java.util.ArrayList;
import java.util.List;

public class CreateNotification {

    public static final String CHANNEL_ID = "2012";
    public static final String ACTION_PLAY = "PLAY";
    public static final String ACTION_PREV = "PREV";
    public static final String ACTION_NEXT = "NEXT";

    public static void createNotification(Context context, List<upperSlideImages> upperSlideImages, ArrayList<RUnderR> rUnderRS, int position, int button1, int button2, int button3){
        NotificationManager notificationManager ;
        Intent intentPlay = new Intent(context,NotificationReceiver.class).setAction(ACTION_PLAY);
        PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(context.getApplicationContext(),0,intentPlay,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentPrev = new Intent(context,NotificationReceiver.class).setAction(ACTION_PREV);
        PendingIntent pendingIntentPrev = PendingIntent.getBroadcast(context.getApplicationContext(),1,intentPrev,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentNext = new Intent(context,NotificationReceiver.class).setAction(ACTION_NEXT);
        PendingIntent pendingIntentNext = PendingIntent.getBroadcast(context.getApplicationContext(),2,intentNext,PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "name";
            String description = "des";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager= context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            NotificationCompat.Builder builder;
            if (upperSlideImages == null) {
                builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("AudioFY")
                        .setContentText(rUnderRS.get(position).getSongName())
                        .setLargeIcon(changeStringToBitmap(rUnderRS.get(position).getSongPhoto()))
                        .addAction(button1, "PREV", pendingIntentPrev)
                        .addAction(button2, "PLAY", pendingIntentPlay)
                        .addAction(button3, "NEXT", pendingIntentNext)
                        .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                                .setShowActionsInCompactView(0, 1, 2))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            } else {
                builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("AudioFY")
                        .setContentText(upperSlideImages.get(position).getSongName())
                        .setLargeIcon(changeStringToBitmap(upperSlideImages.get(position).getSongPhoto()))
                        .addAction(button1, "PREV", pendingIntentPrev)
                        .addAction(button2, "PLAY", pendingIntentPlay)
                        .addAction(button3, "NEXT", pendingIntentNext)
                        .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                                .setShowActionsInCompactView(0, 1, 2))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            }
            notificationManager.notify(18, builder.build());
        }
    }

    private static Bitmap changeStringToBitmap(String songPhoto) {
        Bitmap decodedImage;
        byte[] imageInByte = Base64.decode(songPhoto, Base64.DEFAULT);
        decodedImage = BitmapFactory.decodeByteArray(imageInByte,0, imageInByte.length);

        return decodedImage;
    }




}
