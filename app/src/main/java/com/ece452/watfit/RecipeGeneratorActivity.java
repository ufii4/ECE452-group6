package com.ece452.watfit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.ece452.watfit.data.Nutrition;
import com.ece452.watfit.data.RecipeGenNutrient;
import com.ece452.watfit.data.RecipeGenNutrition;
import com.ece452.watfit.data.RecipeGenerator;
import com.ece452.watfit.data.UserProfile;
import com.ece452.watfit.data.source.remote.RecipeGeneraterService;
import com.ece452.watfit.data.source.remote.RecipeGeneraterServiceRetrofitClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class RecipeGeneratorActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    //constant int called number_of_recipes
    private static final int NUMBER_OF_RECIPES = 30;

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

    List<RecipeGenerator> breakfast_recipes;
    List<RecipeGenerator> lunch_recipes;
    List<RecipeGenerator> dinner_recipes;

    @Inject
    FirebaseFirestore db;

    RecipeGeneraterServiceRetrofitClient recipeGeneraterService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_generator);

        LinearLayout linearLayout = findViewById(R.id.header_linearlayout_nav);
        CardView cardView_breakfast = linearLayout.findViewById(R.id.card_view_breakfast);
        CardView cardView_lunch = linearLayout.findViewById(R.id.card_view_lunch);
        CardView cardView_dinner = linearLayout.findViewById(R.id.card_view_dinner);
        LinearLayout breakfast_recipe_linearlayout = cardView_breakfast.findViewById(R.id.breakfast_recipe_linearlayout);
        LinearLayout lunch_recipe_linearlayout = cardView_lunch.findViewById(R.id.lunch_recipe_linearlayout);
        LinearLayout dinner_recipe_linearlayout = cardView_dinner.findViewById(R.id.dinner_recipe_linearlayout);
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

        bt_regenerate = linearLayout.findViewById(R.id.bt_regenerate_recipe);

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

                int randomNum_breakfast = ThreadLocalRandom.current().nextInt(1, NUMBER_OF_RECIPES + 1);
                int randomNum_lunch = ThreadLocalRandom.current().nextInt(1, NUMBER_OF_RECIPES + 1);
                int randomNum_dinner = ThreadLocalRandom.current().nextInt(1, NUMBER_OF_RECIPES + 1);
//
                Log.d("hihihi", "onClick: "+ randomNum_breakfast + " " + randomNum_lunch + " " + randomNum_dinner);
//
                Log.d("hihihi", "onClick: "+ breakfast_recipes.size() + " " + lunch_recipes.size() + " " + dinner_recipes.size());
                RecipeGenerator newBreakfast = breakfast_recipes.get(randomNum_breakfast);
                RecipeGenerator newLunch = lunch_recipes.get(randomNum_lunch);
                RecipeGenerator newDinner = dinner_recipes.get(randomNum_dinner);

                RecipeGenNutrition newBreakfastNutrition = newBreakfast.nutrition;
                RecipeGenNutrition newLunchNutrition = newLunch.nutrition;
                RecipeGenNutrition newDinnerNutrition = newDinner.nutrition;

                newBreakfastNutrition.genNutrients();
                newLunchNutrition.genNutrients();
                newDinnerNutrition.genNutrients();

                breakfast_RecipeUI(newBreakfast, newBreakfastNutrition);
                lunch_RecipeUI(newLunch, newLunchNutrition);
                dinner_RecipeUI(newDinner, newDinnerNutrition);



            }
        });

    }

    private void getRecipesGenerated(String mealType) {
        Call<RecipeGeneraterService.Result<RecipeGenerator>> call =RecipeGeneraterServiceRetrofitClient.getInstance()
                .getRecipeGeneraterService().searchRecipes(10, 50,
                        10, 50, 50 , 800, 10, 50, mealType,NUMBER_OF_RECIPES);

        call.enqueue(new Callback<RecipeGeneraterService.Result<RecipeGenerator>>() {
            @Override
            public void onResponse(Call<RecipeGeneraterService.Result<RecipeGenerator>> call, Response<RecipeGeneraterService.Result<RecipeGenerator>> response) {
                if(response.isSuccessful()){
//                    Log.d("RecipeGeneratorActivity", "onResponse: " + "========hihihihihihihi=======");

                    List<RecipeGenerator> list = response.body().results;
                    RecipeGenerator recipeGenerator = list.get(0);
                    RecipeGenNutrition nutrition = recipeGenerator.nutrition;
                    nutrition.genNutrients();

                    if(mealType.equals("breakfast")){
                        breakfast_recipes.addAll(list);
                        breakfast_RecipeUI(recipeGenerator,  nutrition);
                    }else if(mealType.equals("lunch")){
                        lunch_recipes.addAll(list);
                        lunch_RecipeUI(recipeGenerator,  nutrition);

                    }else if(mealType.equals("dinner")){
                        dinner_recipes.addAll(list);
                        dinner_RecipeUI(recipeGenerator,  nutrition);
                    }
                }
                else{
                    Log.d("hihi", " ==================onResponse: p13=================="+response.message());
                }
            }

            @Override
            public void onFailure(Call<RecipeGeneraterService.Result<RecipeGenerator>> call, Throwable t) {
                Log.e("RecipeGeneratorActivity", "onFailure: " + t.getMessage());
            }
        });
    }

    private void breakfast_RecipeUI(RecipeGenerator recipeGenerator, RecipeGenNutrition nutrition) {
        breakfast_dish_name.setText(recipeGenerator.title);
        breakfast_calories.setText("Calories: "+nutrition.calories.amount+" "+ nutrition.calories.unit);
        breakfast_protein.setText("Protein: "+nutrition.protein.amount +" " + nutrition.protein.unit);
        breakfast_fat.setText("Fat: "+nutrition.fat.amount +" " + nutrition.fat.unit);
        breakfast_carbs.setText("Carbohydrates: "+nutrition.carbs.amount +" " + nutrition.carbs.unit);
    }

    private void lunch_RecipeUI(RecipeGenerator recipeGenerator, RecipeGenNutrition nutrition) {
        lunch_dish_name.setText(recipeGenerator.title);
        lunch_calories.setText("Calories: "+nutrition.calories.amount+" "+ nutrition.calories.unit);
        lunch_protein.setText("Protein: "+nutrition.protein.amount +" " + nutrition.protein.unit);
        lunch_fat.setText("Fat: "+nutrition.fat.amount +" " + nutrition.fat.unit);
        lunch_carbs.setText("Carbohydrates: "+nutrition.carbs.amount +" " + nutrition.carbs.unit);
    }

    private void dinner_RecipeUI(RecipeGenerator recipeGenerator, RecipeGenNutrition nutrition) {
        dinner_dish_name.setText(recipeGenerator.title);
        dinner_calories.setText("Calories: "+nutrition.calories.amount+" "+ nutrition.calories.unit);
        dinner_protein.setText("Protein: "+nutrition.protein.amount +" " + nutrition.protein.unit);
        dinner_fat.setText("Fat: "+nutrition.fat.amount +" " + nutrition.fat.unit);
        dinner_carbs.setText("Carbohydrates: "+ nutrition.carbs.amount +" " +  nutrition.carbs.unit);
    }


}
