package com.ece452.watfit.data.source.remote;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.ece452.watfit.data.MenuItem;
import com.ece452.watfit.data.Nutrition;
import com.ece452.watfit.data.Recipe;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class FoodServiceTest {
    private SpoonacularDataSource spoonacularDataSource;
    private FoodService foodService;

    @Before
    public void setUp() {
        spoonacularDataSource = new SpoonacularDataSource();
        foodService = spoonacularDataSource.foodService;
    }

    @Test
    public void searchMenuItems() {
        foodService.searchMenuItems("chicken", 1).test().awaitCount(1).assertValue(response -> {
            List<MenuItem> menuItems = response.menuItems;
            return menuItems.size() == 1;
        });
    }

    @Test
    public void getMenuItem() {
        foodService.getMenuItem(424571).test().awaitCount(1).assertValue(response -> {
            MenuItem menuItem = response;
            boolean correctCalories = false;
            for (Nutrition.Nutrient nutrient : menuItem.nutrition.nutrients) {
                if (nutrient.name.equals("Calories")) {
                    correctCalories = nutrient.amount == 1040.0;
                }
            }
            return menuItem.id == 424571 && menuItem.title.equals("Bacon King Burger") && correctCalories;
        });
    }
}