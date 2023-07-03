package com.ece452.watfit.data.source.remote;

import com.ece452.watfit.data.Recipe;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import retrofit2.http.GET;

public interface RecipeService {
    class Result<T> {
        public List<Recipe> results;
    }

    @GET("recipes/complexSearch")
    Flowable<Result<Recipe>> searchRecipeWithPreference(
            @retrofit2.http.Query("minCarbs") int minCarbs,
            @retrofit2.http.Query("maxCarbs") int maxCarbs,
            @retrofit2.http.Query("minProtein") int minProtein,
            @retrofit2.http.Query("maxProtein") int maxsProtein,
            @retrofit2.http.Query("minCalories") int minCalories,
            @retrofit2.http.Query("maxCalories") int maxCalories,
            @retrofit2.http.Query("minFat") int minFat,
            @retrofit2.http.Query("maxFat") int maxFat,
            @retrofit2.http.Query("query") String mealType,
            @retrofit2.http.Query("number") int number
    );

    @GET("recipes/complexSearch")
    Flowable<Result<Recipe>> searchRecipes(
            @retrofit2.http.Query("query") String query,
            @retrofit2.http.Query("number") int number,
            @retrofit2.http.Query("addRecipeNutrition") Boolean includeNutrition
    );
}
