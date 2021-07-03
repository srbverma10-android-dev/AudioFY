package com.sourabhverma.audiofy.roomdatabase;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.sourabhverma.audiofy.dao.ArtistIdAndSongIdDAO;
import com.sourabhverma.audiofy.datatypeconverters.ArrayToString;
import com.sourabhverma.audiofy.models.ArtistIdAndSongId;
import com.sourabhverma.audiofy.utils.Constants;

@Database(entities = {ArtistIdAndSongId.class}, version = 1)
@TypeConverters(ArrayToString.class)
public abstract class ArtistIdAndSongIdDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "artistIdAndSongId.db";

    public static volatile ArtistIdAndSongIdDatabase instance;

    private static final Object LOCK = new Object();

    public abstract ArtistIdAndSongIdDAO artistIdAndSongIdDAO();

    public static ArtistIdAndSongIdDatabase getInstance(Context context) {

        if (instance == null){

            synchronized (LOCK) {

                if (instance == null){

                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            ArtistIdAndSongIdDatabase.class, DATABASE_NAME).build();

                    Log.d(Constants.TAG, "getInstance: in artist");

                }

            }

        }

        return instance;

    }


}
