package com.ece452.watfit.data;

import com.ece452.watfit.data.models.Recipe;
import com.ece452.watfit.data.models.RecipeInformation;
import com.ece452.watfit.data.source.remote.RecipeService;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface RecipeRepository {
    class Result<T> {
        public List<Recipe> results;
    }

    Flowable<RecipeInformation> searchRecipeInformation(String id);

    Flowable<RecipeService.Result<Recipe>> searchRecipeWithPreference(int minCarbs, int maxCarbs, int minProtein, int maxsProtein, int minCalories, int maxCalories, int minFat, int maxFat, String mealType, int number);

    Flowable<RecipeService.Result<Recipe>> searchRecipes(String query, int number, Boolean includeNutrition);
}
