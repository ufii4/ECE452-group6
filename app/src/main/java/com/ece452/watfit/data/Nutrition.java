package com.ece452.watfit.data;

public class Nutrition {
    public class Nutrient {
        public String name;
        public String unit;
        public double amount;
    }

    public Nutrient nutrients[];

    public Nutrient calories;
    public Nutrient protein;
    public Nutrient fat;
    public Nutrient carbs;

    public void genNutrients() {
        for (Nutrient nutrient : this.nutrients) {
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
