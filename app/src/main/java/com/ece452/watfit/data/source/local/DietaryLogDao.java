package com.ece452.watfit.data.source.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ece452.watfit.data.models.DietaryLog;

import java.time.LocalDate;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface DietaryLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(DietaryLog dietaryLog);

    @Query("SELECT * FROM dietary_log")
    Flowable<List<DietaryLog>> getAllDietaryLogs();

    @Query("SELECT * FROM dietary_log WHERE date = :date")
    Flowable<DietaryLog> getDietaryLog(LocalDate date);

    @Query("SELECT * FROM dietary_log WHERE date = :date")
    Flowable<DietaryLog> getDietaryLog(String date);

    @Query("SELECT * FROM dietary_log WHERE date BETWEEN :startDate AND :endDate")
    Flowable<List<DietaryLog>> getDietaryLogs(LocalDate startDate, LocalDate endDate);

    @Query("SELECT * FROM dietary_log WHERE date BETWEEN :startDate AND :endDate")
    Flowable<List<DietaryLog>> getDietaryLogs(String startDate, String endDate);
}
