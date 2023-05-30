package com.ece452.watfit.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "dietary_log")
public class DietaryLog {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String date;

    @Ignore
    public List<DietaryLogEntry> entries;

    public DietaryLog(String date) {
        this.date = date;
    }
}
