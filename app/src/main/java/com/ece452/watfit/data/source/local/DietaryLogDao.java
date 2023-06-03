package com.ece452.watfit.data.source.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ece452.watfit.data.DietaryLog;
import com.ece452.watfit.data.DietaryLogWithEntries;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface DietaryLogDao {
    @Insert
    Completable insert(DietaryLog dietaryLog);

    @Query("SELECT * FROM dietary_log WHERE id = :id")
    Flowable<DietaryLog> getDietaryLog(int id);

    @Query("SELECT * FROM dietary_log WHERE date = :date")
    Flowable<DietaryLog> getDietaryLog(String date);

    @Query("SELECT * FROM dietary_log WHERE date BETWEEN :startDate AND :endDate")
    Flowable<List<DietaryLog>> getDietaryLog(String startDate, String endDate);

    @Transaction
    @Query("SELECT * FROM dietary_log")
    Flowable<List<DietaryLogWithEntries>> getDietaryLogsWithEntries();

    @Transaction
    @Query("SELECT * FROM dietary_log WHERE date = :date")
    Flowable<DietaryLogWithEntries> getDietaryLogWithEntries(String date);

    @Transaction
    @Query("SELECT * FROM dietary_log WHERE date BETWEEN :startDate AND :endDate")
    Flowable<List<DietaryLogWithEntries>> getDietaryLogsWithEntries(String startDate, String endDate);

    @Transaction
    @Query("SELECT * FROM dietary_log WHERE id = :id")
    Flowable<DietaryLogWithEntries> getDietaryLogWithEntries(int id);
}
