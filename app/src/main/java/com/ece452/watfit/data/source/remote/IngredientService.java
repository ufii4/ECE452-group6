package com.ece452.watfit.data.source.remote;

import com.ece452.watfit.data.Ingredient;
import com.ece452.watfit.data.MenuItem;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import retrofit2.http.GET;

public interface IngredientService {
    class Result<T> {
        int offset;
        int number;
        int totalResults;
        public List<Ingredient> results;
    }
    @GET("food/ingredients/search")
    Flowable<Result<Ingredient>> searchIngredient(
            @retrofit2.http.Query("query") String query
    );

    @GET("food/ingredients/{id}/information")
    Flowable<Ingredient> getIngredientInformation(
            @retrofit2.http.Path("id") int id,
            @retrofit2.http.Query("amount") int amount,
            @retrofit2.http.Query("unit") String unit
    );

    @GET("food/ingredients/{id}/information")
    Flowable<Ingredient> getIngredientInformationBasic(
            @retrofit2.http.Path("id") int id
    );

}
