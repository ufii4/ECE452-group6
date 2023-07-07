package com.ece452.watfit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.ece452.watfit.data.Nutrition;
import com.ece452.watfit.data.Recipe;
import com.ece452.watfit.data.UserProfile;
import com.ece452.watfit.data.source.remote.RecipeService;
import com.ece452.watfit.data.source.remote.SpoonacularDataSource;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.annotations.NonNull;

@AndroidEntryPoint
public class RecipeGeneratorActivity extends AppCompatActivity implements PreferenceDialog.PreferenceDialogListener {
    private FirebaseAuth auth;

    //constant int called number_of_recipes
    private static final int NUMBER_OF_RECIPES = 30;
    private static final int NUMBER_OF_PREFERENCED_RECIPES = 10;

    TextView breakfast_dish_name;
    TextView lunch_dish_name;
    TextView dinner_dish_name;
    TextView breakfast_calories;
    TextView lunch_calories;
    TextView dinner_calories;
    TextView breakfast_protein;
    TextView lunch_protein;
    TextView dinner_protein;
    TextView breakfast_carbs;
    TextView lunch_carbs;
    TextView dinner_carbs;
    TextView breakfast_fat;
    TextView lunch_fat;
    TextView dinner_fat;
    Button bt_regenerate;
    Button bt_preferencebar;

    List<Recipe> breakfast_recipes;
    List<Recipe> lunch_recipes;
    List<Recipe> dinner_recipes;

    @Inject
    FirebaseFirestore db;

    @Inject
    SpoonacularDataSource spoonacular;

    RecipeService recipeService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_generator);

        // add back button to the top-left corner
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        }

        recipeService = spoonacular.recipeService;

        LinearLayout breakfast_recipe_linearlayout = findViewById(R.id.breakfast_recipe_linearlayout);
        LinearLayout lunch_recipe_linearlayout = findViewById(R.id.lunch_recipe_linearlayout);
        LinearLayout dinner_recipe_linearlayout = findViewById(R.id.dinner_recipe_linearlayout);
        LinearLayout breakfast_nutrition_linearlayout = breakfast_recipe_linearlayout.findViewById(R.id.breakfast_nutrition_linearlayout);
        LinearLayout lunch_nutrition_linearlayout = lunch_recipe_linearlayout.findViewById(R.id.lunch_nutrition_linearlayout);
        LinearLayout dinner_nutrition_linearlayout = dinner_recipe_linearlayout.findViewById(R.id.dinner_nutrition_linearlayout);
        breakfast_dish_name = breakfast_nutrition_linearlayout.findViewById(R.id.breakfast_dish_name);
        lunch_dish_name = lunch_nutrition_linearlayout.findViewById(R.id.lunch_dish_name);
        dinner_dish_name = dinner_nutrition_linearlayout.findViewById(R.id.dinner_dish_name);
        breakfast_calories = breakfast_nutrition_linearlayout.findViewById(R.id.breakfast_Calories);
        lunch_calories = lunch_nutrition_linearlayout.findViewById(R.id.lunch_Calories);
        dinner_calories = dinner_nutrition_linearlayout.findViewById(R.id.dinner_Calories);
        breakfast_protein = breakfast_nutrition_linearlayout.findViewById(R.id.breakfast_Protein);
        lunch_protein = lunch_nutrition_linearlayout.findViewById(R.id.lunch_Protein);
        dinner_protein = dinner_nutrition_linearlayout.findViewById(R.id.dinner_Protein);
        breakfast_fat = breakfast_nutrition_linearlayout.findViewById(R.id.breakfast_Fat);
        lunch_fat = lunch_nutrition_linearlayout.findViewById(R.id.lunch_Fat);
        dinner_fat = dinner_nutrition_linearlayout.findViewById(R.id.dinner_Fat);
        breakfast_carbs = breakfast_nutrition_linearlayout.findViewById(R.id.breakfast_Carbohydrates);
        lunch_carbs = lunch_nutrition_linearlayout.findViewById(R.id.lunch_Carbohydrates);
        dinner_carbs = dinner_nutrition_linearlayout.findViewById(R.id.dinner_Carbohydrates);

        bt_regenerate = findViewById(R.id.bt_regenerate_recipe);

        ///// preference bar
        LinearLayout header_linearlayout_nav = findViewById(R.id.header_linearlayout_nav);
        bt_preferencebar = header_linearlayout_nav.findViewById(R.id.preferencebar);
        bt_preferencebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        ///// get user profile from database
        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserProfile profile = documentSnapshot.toObject(UserProfile.class);

                if (profile == null) {
                    Log.e("RecipeGeneratorActivity", "onFailure: profile is null");
                }
                Log.d("RecipeGeneratorActivity", "onSuccess: " + profile.getName());
            }
        });

        breakfast_recipes = new ArrayList<>();
        lunch_recipes = new ArrayList<>();
        dinner_recipes = new ArrayList<>();

        ///// generate recipe from spoonacular api
        getRecipesGenerated("breakfast");
        getRecipesGenerated("lunch");
        getRecipesGenerated("dinner");



        /////regenerate button click event
        bt_regenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int randomNum_breakfast = ThreadLocalRandom.current().nextInt(0, breakfast_recipes.size());
                int randomNum_lunch = ThreadLocalRandom.current().nextInt(0, lunch_recipes.size());
                int randomNum_dinner = ThreadLocalRandom.current().nextInt(0, dinner_recipes.size());
//
                Log.d("hihihi", "onClick: "+ randomNum_breakfast + " " + randomNum_lunch + " " + randomNum_dinner);
//
                Log.d("hihihi", "onClick: "+ breakfast_recipes.size() + " " + lunch_recipes.size() + " " + dinner_recipes.size());
                Recipe newBreakfast = breakfast_recipes.get(randomNum_breakfast);
                Recipe newLunch = lunch_recipes.get(randomNum_lunch);
                Recipe newDinner = dinner_recipes.get(randomNum_dinner);

                Nutrition newBreakfastNutrition = newBreakfast.nutrition;
                Nutrition newLunchNutrition = newLunch.nutrition;
                Nutrition newDinnerNutrition = newDinner.nutrition;

                newBreakfastNutrition.genNutrients();
                newLunchNutrition.genNutrients();
                newDinnerNutrition.genNutrients();

                breakfast_RecipeUI(newBreakfast, newBreakfastNutrition);
                lunch_RecipeUI(newLunch, newLunchNutrition);
                dinner_RecipeUI(newDinner, newDinnerNutrition);
            }
        });

    }

    // add Account & Sharing button to action bar (header)
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_account_button, menu);
        getMenuInflater().inflate(R.menu.menu_share_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            // go back to previous activity
            finish();
            return true;
        }
        if(item.getItemId() == R.id.account_button) {
            // handle account button click
            startActivity(new Intent(RecipeGeneratorActivity.this, AccountActivity.class));
            return true;
        }
        if(item.getItemId() == R.id.share_post_button) {
            // handle share button click
            // TODO: take a screenshot on the RecipeGeneratorActivity before navigate to EditPostActivity
            startActivity(new Intent(RecipeGeneratorActivity.this, EditPostActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getPreferencedRecipeGenerated(int minCarbs, int maxCarbs,
                                               int minProtein, int maxProtein,
                                               int minCalories, int maxCalories,
                                               int minFat, int maxFat,
                                               String mealType){
        String query = "healthy";
        switch (mealType) {
            case "breakfast":
                query = "breakfast";
                break;
            case "lunch":
                query = "chicken";
                break;
            case "dinner":
                query = "beef";
                break;
        }
        recipeService.searchRecipeWithPreference(minCarbs, maxCarbs,
                minProtein, maxProtein,
                minCalories, maxCalories,
                minFat, maxFat, query ,NUMBER_OF_PREFERENCED_RECIPES)
                .subscribe(
                        result -> {
                            List<Recipe> list = result.results;
                            Recipe recipe = list.get(0);
                            Nutrition nutrition = recipe.nutrition;
                            nutrition.genNutrients();

                            if (mealType.equals("breakfast")) {
                                breakfast_recipes.clear();
                                breakfast_recipes.addAll(list);
                                breakfast_RecipeUI(recipe, nutrition);
                            } else if (mealType.equals("lunch")) {
                                lunch_recipes.clear();
                                lunch_recipes.addAll(list);
                                lunch_RecipeUI(recipe, nutrition);
                            } else if (mealType.equals("dinner")) {
                                dinner_recipes.clear();
                                dinner_recipes.addAll(list);
                                dinner_RecipeUI(recipe, nutrition);
                            }
                        },
                        error -> {
                            Log.e("RecipeGeneratorActivity", "onFailure: " + error.getLocalizedMessage());
                        },
                        () -> {
                            Log.d("RecipeGeneratorActivity", "onComplete: ");
                        }
                );

    }

    private void getRecipesGenerated(String mealType) {
        String query = "healthy";
        switch (mealType) {
            case "breakfast":
                query = "breakfast";
                break;
            case "lunch":
                query = "burger";
                break;
            case "dinner":
                query = "chicken breast";
                break;
        }
        recipeService.searchRecipes(query, NUMBER_OF_RECIPES, true)
                .subscribe(
                        result -> {
                            List<Recipe> list = result.results;
                            Recipe recipe = list.get(0);
                            Nutrition nutrition = recipe.nutrition;
                            nutrition.genNutrients();

                            if (mealType.equals("breakfast")) {
                                breakfast_recipes.addAll(list);
                                breakfast_RecipeUI(recipe, nutrition);
                            } else if (mealType.equals("lunch")) {
                                lunch_recipes.addAll(list);
                                lunch_RecipeUI(recipe, nutrition);
                            } else if (mealType.equals("dinner")) {
                                dinner_recipes.addAll(list);
                                dinner_RecipeUI(recipe, nutrition);
                            }
                        },
                        error -> {
                            Log.e("RecipeGeneratorActivity", "onFailure: " + error.getLocalizedMessage());
                        },
                        () -> {
                            Log.d("RecipeGeneratorActivity", "onComplete: ");
                        }
                );
    }

    private void breakfast_RecipeUI(Recipe recipeGenerator, Nutrition nutrition) {
        breakfast_dish_name.setText(recipeGenerator.title);
        breakfast_calories.setText("Calories: "+nutrition.calories.amount+" "+ nutrition.calories.unit);
        breakfast_protein.setText("Protein: "+nutrition.protein.amount +" " + nutrition.protein.unit);
        breakfast_fat.setText("Fat: "+nutrition.fat.amount +" " + nutrition.fat.unit);
        breakfast_carbs.setText("Carbohydrates: "+nutrition.carbs.amount +" " + nutrition.carbs.unit);
    }

    private void lunch_RecipeUI(Recipe recipeGenerator, Nutrition nutrition) {
        lunch_dish_name.setText(recipeGenerator.title);
        lunch_calories.setText("Calories: "+nutrition.calories.amount+" "+ nutrition.calories.unit);
        lunch_protein.setText("Protein: "+nutrition.protein.amount +" " + nutrition.protein.unit);
        lunch_fat.setText("Fat: "+nutrition.fat.amount +" " + nutrition.fat.unit);
        lunch_carbs.setText("Carbohydrates: "+nutrition.carbs.amount +" " + nutrition.carbs.unit);
    }

    private void dinner_RecipeUI(Recipe recipeGenerator, Nutrition nutrition) {
        dinner_dish_name.setText(recipeGenerator.title);
        dinner_calories.setText("Calories: "+nutrition.calories.amount+" "+ nutrition.calories.unit);
        dinner_protein.setText("Protein: "+nutrition.protein.amount +" " + nutrition.protein.unit);
        dinner_fat.setText("Fat: "+nutrition.fat.amount +" " + nutrition.fat.unit);
        dinner_carbs.setText("Carbohydrates: "+ nutrition.carbs.amount +" " +  nutrition.carbs.unit);
    }


    public void openDialog(){
        PreferenceDialog preferenceDialog = new PreferenceDialog();
        preferenceDialog.show(getSupportFragmentManager(),"preference dialog");
    }

    @Override
    public void applyTexts(String calorie,String protein, String fat, String carbohydrates ) {
        Log.d("hihihi", "applyTexts: calorie is "+ calorie +" carbohydrates is " + carbohydrates +
                " fat is "+fat + " protein is "+protein);

        int [] calories = parseNumbers(calorie);
        int [] proteins = parseNumbers(protein);
        int [] fats = parseNumbers(fat);
        int [] carbohydrateses = parseNumbers(carbohydrates);

        int minCalorie = calories[0];
        int maxCaloire = calories[1];
        int minProtein = proteins[0];
        int maxProtein = proteins[1];
        int minFat = fats[0];
        int maxFat = fats[1];
        int minCarbohydrates = carbohydrateses[0];
        int maxCarbohydrates = carbohydrateses[1];


        getPreferencedRecipeGenerated(minCarbohydrates, maxCarbohydrates,
                minProtein,  maxProtein,
                minCalorie, maxCaloire,
                minFat, maxFat, "breakfast");
        getPreferencedRecipeGenerated(minCarbohydrates, maxCarbohydrates,
                minProtein,  maxProtein,
                maxCaloire, maxCaloire,
                minFat, maxFat, "lunch");
        getPreferencedRecipeGenerated(minCarbohydrates, maxCarbohydrates,
                minProtein,  maxProtein,
                maxCaloire, maxCaloire,
                minFat, maxFat, "dinner");

    }

    private static int[] parseNumbers(String input) {
        String[] parts = input.split("~");
        int[] numbers = new int[2];

        try {
            numbers[0] = Integer.parseInt(parts[0]);
            numbers[1] = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input: " + input);
        }

        return numbers;
    }
}
