package com.sourabhverma.audiofy.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sourabhverma.audiofy.models.ArtistIdAndSongId;

@Dao
public interface ArtistIdAndSongIdDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ArtistIdAndSongId artistIdAndSongId);

    @Query("Select stringArrayList from artistIdAndSongId where songId = :songId")
    String get(String songId);

}
