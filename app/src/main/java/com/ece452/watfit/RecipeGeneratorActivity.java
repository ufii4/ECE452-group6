package com.ece452.watfit;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ece452.watfit.data.RecipeGenerator;
import com.ece452.watfit.data.UserProfile;
import com.ece452.watfit.data.source.remote.RecipeGeneraterService;
import com.ece452.watfit.data.source.remote.RecipeGeneraterServiceRetrofitClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class RecipeGeneratorActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Inject
    FirebaseFirestore db;

    RecipeGeneraterServiceRetrofitClient recipeGeneraterService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_generator);

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

        ///// generate recipe from spoonacular api
        getRecipesGenerated();
    }

    private void getRecipesGenerated(){
        Call<RecipeGeneraterService.Result<RecipeGenerator>> call =RecipeGeneraterServiceRetrofitClient.getInstance()
                .getRecipeGeneraterService().searchRecipes(10, 50,
                        10, 50, 50 , 800, 10, 50,
                        "chinese", "lunch",5);
        call.enqueue(new Callback<RecipeGeneraterService.Result<RecipeGenerator>>() {
            @Override
            public void onResponse(Call<RecipeGeneraterService.Result<RecipeGenerator>> call, Response<RecipeGeneraterService.Result<RecipeGenerator>> response) {
                if(response.isSuccessful()){
                    List<RecipeGenerator> list = response.body().results;
                    for(RecipeGenerator recipe : list){
                        Log.d("RecipeGeneratorActivity", "onResponse: " + recipe.title);
                    }
                }
            }

            @Override
            public void onFailure(Call<RecipeGeneraterService.Result<RecipeGenerator>> call, Throwable t) {
                Log.e("RecipeGeneratorActivity", "onFailure: " + t.getMessage());
            }

        });
    }


}
