package com.ece452.watfit.data.source.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ece452.watfit.data.models.DietaryLogEntry;

@Dao
public interface DietaryLogEntryDao {
    @Insert
    void insert(DietaryLogEntry entry);

    @Query("SELECT * FROM dietary_log_entry WHERE id = :id")
    DietaryLogEntry getDietaryLogEntry(int id);

    @Query("SELECT * FROM dietary_log_entry WHERE dietary_log_id = :dietaryLogId")
    DietaryLogEntry getDietaryLogEntryByDietaryLogId(int dietaryLogId);
}
