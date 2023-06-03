package com.ece452.watfit.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "dietary_log_entry")
public class DietaryLogEntry {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "dietary_log_id")
    public int dietaryLogId;

    @ColumnInfo(name = "food_id")
    public int foodId;

    public int quantity;

    public DietaryLogEntry(int dietaryLogId, int foodId, int quantity) {
        this.dietaryLogId = dietaryLogId;
        this.foodId = foodId;
        this.quantity = quantity;
    }
}
