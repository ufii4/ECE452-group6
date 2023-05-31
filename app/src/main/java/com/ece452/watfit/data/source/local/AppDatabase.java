package com.ece452.watfit.data.source.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ece452.watfit.data.Converters;
import com.ece452.watfit.data.DietaryLog;
import com.ece452.watfit.data.DietaryLogEntry;

@Database(entities = {DietaryLog.class, DietaryLogEntry.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract DietaryLogDao dietaryLogDao();
    public abstract DietaryLogEntryDao dietaryLogEntryDao();
}
