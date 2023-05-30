package com.ece452.watfit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.ece452.watfit.data.AppDataBase;
import com.ece452.watfit.data.model.DietaryLog;
import com.ece452.watfit.data.model.DietaryLogDao;

import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DietaryLogUnitTest {
    private DietaryLogDao dietaryLogDao;
    private AppDataBase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDataBase.class).build();
        dietaryLogDao = db.dietaryLogDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeDietaryLogAndReadInList() {
        DietaryLog log = new DietaryLog( "2023-05-30");
        dietaryLogDao.insert(log);
        List<DietaryLog> logs = dietaryLogDao.getDietaryLog("2023-05-01", "2023-05-31");
        assertEquals(logs.get(0).date, log.date);
    }
}