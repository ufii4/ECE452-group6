package com.ece452.watfit.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.ece452.watfit.data.model.DietaryLog;
import com.ece452.watfit.data.model.DietaryLogDao;

@Database(entities = {DietaryLog.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract DietaryLogDao dietaryLogDao();
}
