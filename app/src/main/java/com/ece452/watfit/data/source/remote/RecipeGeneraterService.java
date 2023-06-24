package com.ece452.watfit.data.source.remote;

import com.ece452.watfit.data.Recipe;
import com.ece452.watfit.data.RecipeGenerator;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeGeneraterService {
    class Result<T> {
       public List<RecipeGenerator> results;
    }

    @GET("recipes/complexSearch")
    Call<Result<RecipeGenerator>> searchRecipes(
            @retrofit2.http.Query("minCarbs") int minCarbs,
            @retrofit2.http.Query("maxCarbs") int maxCarbs,
            @retrofit2.http.Query("minProtein") int minProtein,
            @retrofit2.http.Query("maxProtein") int maxsProtein,
            @retrofit2.http.Query("minCalories") int minCalories,
            @retrofit2.http.Query("maxCalories") int maxCalories,
            @retrofit2.http.Query("minFat") int minFat,
            @retrofit2.http.Query("maxFat") int maxFat,
            @retrofit2.http.Query("cuisine") String cuisine,
            @retrofit2.http.Query("query") String mealType,
            @retrofit2.http.Query("number") int number
            );
}
