package com.ece452.watfit.data.source.remote;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.ece452.watfit.data.Ingredient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RunWith(AndroidJUnit4.class)
public class IngredientServiceTest {
    private SpoonacularDataSource spoonacularDataSource;
    private IngredientService ingredientService;

    @Before
    public void setUp() {
        spoonacularDataSource = new SpoonacularDataSource();
        ingredientService = spoonacularDataSource.ingredientService;
    }

    @Test
    public void searchIngredients() {
        ingredientService.searchIngredient("banana");
    }
}
