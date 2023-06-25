package com.ece452.watfit.data;

import java.util.ArrayList;

public class RecipeGenNutrition {
    public ArrayList<RecipeGenNutrient> nutrients;

    public RecipeGenNutrient calories;
    public RecipeGenNutrient protein;
    public RecipeGenNutrient fat;
    public RecipeGenNutrient carbs;


    public void genNutrients() {
        for (RecipeGenNutrient nutrient : this.nutrients) {
            if (nutrient.name.equals("Calories")) {
                this.calories = nutrient;
            } else if (nutrient.name.equals("Protein")) {
                this.protein = nutrient;
            } else if (nutrient.name.equals("Fat")) {
                this.fat = nutrient;
            } else if (nutrient.name.equals("Carbohydrates")) {
                this.carbs = nutrient;
            }
        }
    }

}
