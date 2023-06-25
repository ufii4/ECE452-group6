package com.ece452.watfit.data.source.remote;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.ece452.watfit.data.Recipe;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RunWith(AndroidJUnit4.class)
public class RecipeServiceTest {
    private SpoonacularDataSource spoonacularDataSource;
    private RecipeService recipeService;

    @Before
    public void setUp() {
        spoonacularDataSource = new SpoonacularDataSource();
        recipeService = spoonacularDataSource.recipeService;
    }

    @Test
    public void searchRecipes() {
        recipeService.searchRecipes("chicken", 1, false).test().awaitCount(1).assertValue(response -> {
            List<Recipe> recipes = response.results;
            return recipes.size() == 1;
        });
    }
}
