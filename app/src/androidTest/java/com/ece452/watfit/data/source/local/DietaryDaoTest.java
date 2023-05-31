package com.ece452.watfit.data.source.local;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.ece452.watfit.data.DietaryLog;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDate;

@RunWith(AndroidJUnit4.class)
public class DietaryDaoTest {
    AppDatabase db;
    DietaryLogDao dietaryLogDao;
    DietaryLogEntryDao dietaryLogEntryDao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).allowMainThreadQueries().build();
        dietaryLogDao = db.dietaryLogDao();
        dietaryLogEntryDao = db.dietaryLogEntryDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeDietaryLogAndReadInList() throws ParseException {
        DietaryLog log = new DietaryLog(LocalDate.parse("2023-05-30"));
        DietaryLog log1 = new DietaryLog(LocalDate.parse("2023-06-01"));

        dietaryLogDao.insert(log).test().assertComplete();
        dietaryLogDao.insert(log1).test().assertComplete();

        dietaryLogDao.getAllDietaryLogs().test().awaitCount(2).assertValue(logs -> {
            return logs.size() == 2;
        });

        dietaryLogDao.getDietaryLogs("2023-05-01", "2023-05-31").test().awaitCount(1).assertValue(logs -> {
            return logs.size() == 1;
        });
    }
}
