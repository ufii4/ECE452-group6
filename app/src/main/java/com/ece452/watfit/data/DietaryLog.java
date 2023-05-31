package com.ece452.watfit.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.Date;

@Entity(tableName = "dietary_log")
public class DietaryLog {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public LocalDate date;

    public DietaryLog(LocalDate date) {
        this.date = date;
    }
}
