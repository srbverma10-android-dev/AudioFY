package com.sourabhverma.audiofy.roomdatabase;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.sourabhverma.audiofy.dao.otherPlaylistsDAO;
import com.sourabhverma.audiofy.datatypeconverters.ArrayString;
import com.sourabhverma.audiofy.models.dataForRecyclerView;
import com.sourabhverma.audiofy.utils.Constants;

@Database(entities = {dataForRecyclerView.class}, version = 1)
@TypeConverters(ArrayString.class)
public abstract class otherPlaylistDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "otherPlaylistsDatabase.db";

    public static volatile otherPlaylistDatabase instance;

    private static final Object LOCK = new Object();

    public abstract otherPlaylistsDAO otherPlaylistsDAO();

    public static otherPlaylistDatabase getInstance(Context context) {

        if (instance == null){

            synchronized (LOCK) {

                if (instance == null){

                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            otherPlaylistDatabase.class, DATABASE_NAME).build();
                    Log.d(Constants.TAG, "getInstance: in otherPlaylists ");

                }

            }

        }

        return instance;

    }

}
