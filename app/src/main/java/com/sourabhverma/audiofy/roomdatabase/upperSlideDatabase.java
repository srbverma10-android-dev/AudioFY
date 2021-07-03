package com.sourabhverma.audiofy.roomdatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sourabhverma.audiofy.dao.upperSlideImagesDAO;
import com.sourabhverma.audiofy.models.upperSlideImages;

@Database(entities = {upperSlideImages.class}, version = 1)
public abstract class upperSlideDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "upperSlideDatabase.db";

    public static volatile upperSlideDatabase instance;

    private static final Object LOCK = new Object();

    public abstract upperSlideImagesDAO upperSlideImagesDAO();

    public static upperSlideDatabase getInstance(Context context) {

        if (instance == null){

            synchronized (LOCK) {

                if (instance == null){

                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            upperSlideDatabase.class, DATABASE_NAME).build();

                }

            }

        }

        return instance;

    }

}
