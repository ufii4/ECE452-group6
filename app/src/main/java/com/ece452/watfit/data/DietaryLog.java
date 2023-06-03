package com.ece452.watfit.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "dietary_log")
public class DietaryLog {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String date;

    public DietaryLog(String date) {
        this.date = date;
    }
}
