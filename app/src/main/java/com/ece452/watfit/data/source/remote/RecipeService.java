package com.ece452.watfit.data.source.remote;

import com.ece452.watfit.data.Recipe;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import retrofit2.http.GET;

public interface RecipeService {
    @GET("recipes/complexSearch")
    Flowable<Result<Recipe>> searchRecipes(
            @retrofit2.http.Query("query") String query,
            @retrofit2.http.Query("number") int number
    );
}
