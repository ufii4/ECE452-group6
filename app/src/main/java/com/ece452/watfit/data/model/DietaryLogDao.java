package com.ece452.watfit.data.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DietaryLogDao {
    @Insert
    void insert(DietaryLog dietaryLog);

    @Query("SELECT * FROM dietary_log WHERE id = :id")
    DietaryLog getDietaryLog(int id);

    @Query("SELECT * FROM dietary_log WHERE date = :date")
    DietaryLog getDietaryLog(String date);

    @Query("SELECT * FROM dietary_log WHERE date BETWEEN :startDate AND :endDate")
    List<DietaryLog> getDietaryLog(String startDate, String endDate);
}
