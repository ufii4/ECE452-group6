package com.ece452.watfit.data.source.remote;

import com.ece452.watfit.data.MenuItem;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import retrofit2.http.GET;

public interface FoodService {
    class Result<T> {
        int offset;
        int number;
        int totalMenuItems;
        List<MenuItem> menuItems;
    }

    @GET("food/menuItems/search")
    Flowable<Result<MenuItem>> searchMenuItems(
            @retrofit2.http.Query("query") String query,
            @retrofit2.http.Query("number") int number
    );

    @GET("food/menuItems/{id}")
    Flowable<MenuItem> getMenuItem(
            @retrofit2.http.Path("id") int id
    );
}
