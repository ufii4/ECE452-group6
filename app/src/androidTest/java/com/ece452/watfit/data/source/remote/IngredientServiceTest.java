package com.ece452.watfit.data.source.remote;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.ece452.watfit.data.models.Ingredient;
import com.ece452.watfit.data.models.Nutrition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

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

//        ingredientService.searchIngredient("banana");
        ingredientService.searchIngredient("banana").test().awaitCount(1).assertValue(response -> {
            List<Ingredient> menuItems = response.results;
            System.out.println();
            return menuItems != null;
        });
    }

    @Test
    public void searchIngredientInformation() {

//        ingredientService.searchIngredient("banana");
        ingredientService.getIngredientInformation(9040,1,"medium").test().awaitCount(1).assertValue(response -> {
            Nutrition.Nutrient[] menuItems = response.nutrition.nutrients;
            System.out.println(menuItems[1].name);
            return menuItems != null;
        });
    }

    @Test
    public void searchIngredientInformationBasic() {

//        ingredientService.searchIngredient("banana");
        Nutrition.Nutrient n[] = ingredientService.getIngredientInformation(9040,1,"small").blockingFirst().nutrition.nutrients;
        System.out.println(n[0].name);
    }
}
