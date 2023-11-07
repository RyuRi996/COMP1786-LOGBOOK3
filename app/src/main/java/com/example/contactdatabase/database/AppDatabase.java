package com.example.contactdatabase.database;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.contactdatabase.dao.DetailDAO;
import com.example.contactdatabase.models.Detail;

@Database(entities = {Detail.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DetailDAO detailDAO();
}
