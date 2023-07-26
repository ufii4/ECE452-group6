package com.ece452.watfit.data;

import com.ece452.watfit.data.models.DietaryLog;
import com.ece452.watfit.data.source.local.DietaryLogDao;
import com.ece452.watfit.data.source.local.DietaryLogEntryDao;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DietaryRepository {
    private final DietaryLogDao dietaryLogDao;
    private final DietaryLogEntryDao dietaryLogEntryDao;

    public DietaryRepository(DietaryLogDao dietaryLogDao, DietaryLogEntryDao dietaryLogEntryDao) {
        this.dietaryLogDao = dietaryLogDao;
        this.dietaryLogEntryDao = dietaryLogEntryDao;
    }

    public Flowable<DietaryLog> getDietaryLog(String date) {
        return dietaryLogDao.getDietaryLog(date).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Completable insertDietaryLog(DietaryLog dietaryLog) {
        return dietaryLogDao.insert(dietaryLog).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}