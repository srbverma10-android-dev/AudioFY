package com.sourabhverma.audiofy.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sourabhverma.audiofy.models.upperSlideImages;

import java.util.List;

@Dao
public interface upperSlideImagesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<upperSlideImages> upperSlideImages);

    @Query("SELECT * FROM upperSlide")
    LiveData<List<upperSlideImages>> getAll();

}
