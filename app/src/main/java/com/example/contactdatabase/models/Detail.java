package com.example.contactdatabase.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "details")
public class Detail {
    @PrimaryKey(autoGenerate = true)
    public long detail_id;
    public String name;
    public String dob;
    public String email;
    public String phone;
    public int avatar;
}
