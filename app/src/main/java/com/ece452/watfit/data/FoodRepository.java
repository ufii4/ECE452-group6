package com.ece452.watfit.data;

import com.ece452.watfit.data.models.MenuItem;
import com.ece452.watfit.data.source.remote.FoodService;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface FoodRepository {
    class Result<T> {
        int offset;
        int number;
        int totalMenuItems;
        List<MenuItem> menuItems;
    }

    Flowable<FoodService.Result<MenuItem>> searchMenuItems(String query,int number);

    Flowable<MenuItem> getMenuItem(int id);
}
