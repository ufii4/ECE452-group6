package com.ece452.watfit.data.source.remote;

import android.telecom.Call;

import com.ece452.watfit.data.Ingredient;

import io.reactivex.rxjava3.core.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IngredientService {
    @GET("food/ingredients/search")
    Flowable<Result<Ingredient>> searchIngredient(
            @retrofit2.http.Query("query") String query
    );

}
