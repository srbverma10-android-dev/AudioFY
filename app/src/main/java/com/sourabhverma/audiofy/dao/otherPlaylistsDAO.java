package com.sourabhverma.audiofy.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sourabhverma.audiofy.models.dataForRecyclerView;

import java.util.List;

@Dao
public interface otherPlaylistsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<dataForRecyclerView> dataForRecyclerViews);

    @Query("SELECT * FROM otherPlaylists")
    LiveData<List<dataForRecyclerView>> getAll();

}
