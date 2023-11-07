package com.example.contactdatabase.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.contactdatabase.models.Detail;

import java.util.List;

@Dao
public interface DetailDAO {
    @Insert
    long insertDetail(Detail detail);

    @Query("SELECT * FROM details ORDER BY name")
    List<Detail> getAllDetails();

    @Delete
    void deleteDetail(Detail detail);

    @Query("DELETE FROM details")
    void deleteAll();
}
