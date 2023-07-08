package com.ece452.watfit.data;

import java.util.ArrayList;


public class RecipeInformation{
    public int id;
    public String title;
    public String image;
    public String imageType;
    public int servings;
    public int readyInMinutes;
    public String license;
    public String sourceName;
    public String sourceUrl;
    public String spoonacularSourceUrl;
    public double healthScore;
    public double spoonacularScore;
    public double pricePerServing;
    public ArrayList<Object> analyzedInstructions;
    public boolean cheap;
    public String creditsText;
    public ArrayList<Object> cuisines;
    public boolean dairyFree;
    public ArrayList<Object> diets;
    public String gaps;
    public boolean glutenFree;
    public String instructions;
    public boolean ketogenic;
    public boolean lowFodmap;
    public ArrayList<Object> occasions;
    public boolean sustainable;
    public boolean vegan;
    public boolean vegetarian;
    public boolean veryHealthy;
    public boolean veryPopular;
    public boolean whole30;
    public int weightWatcherSmartPoints;
    public ArrayList<String> dishTypes;
    public ArrayList<ExtendedIngredient> extendedIngredients;
    public String summary;
    public WinePairing winePairing;
}



 class Measures{
    public Metric metric;
    public Us us;
}

 class Metric{
    public double amount;
    public String unitLong;
    public String unitShort;
}

 class ProductMatch{
    public int id;
    public String title;
    public String description;
    public String price;
    public String imageUrl;
    public double averageRating;
    public double ratingCount;
    public double score;
    public String link;
}


 class Us{
    public double amount;
    public String unitLong;
    public String unitShort;
}

 class WinePairing{
    public ArrayList<String> pairedWines;
    public String pairingText;
    public ArrayList<ProductMatch> productMatches;
}
