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
    Flowable<Result<Recipe>> searchRecipes(
            @retrofit2.http.Query("query") String query,
            @retrofit2.http.Query("number") int number,
            @retrofit2.http.Query("addRecipeNutrition") Boolean includeNutrition
    );
}
