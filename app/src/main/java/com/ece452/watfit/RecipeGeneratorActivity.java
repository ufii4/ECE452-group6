package com.ece452.watfit;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ece452.watfit.data.SimilarRecipeResponse;
import com.ece452.watfit.data.UserProfile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

@AndroidEntryPoint
public class RecipeGeneratorActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Inject
    FirebaseFirestore db;



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
// https://spoonacular.com/food-api/docs#Generate-Meal-Plan


    }

    public  void getSimilarRecipes(SimilarRecipesListener listener, int id){

        // instantiate retrofit
        
        Call<List<SimilarRecipeResponse>> call = callSimilarRecipe.callSimilarRecipe(id, "5", "eeadd8f318854251b83b19945deeed4e");
        call.enqueue(new Callback<List<SimilarRecipeResponse>>() {
            @Override
            public void onResponse(Call<List<SimilarRecipeResponse>> call, Response<List<SimilarRecipeResponse>> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<List<SimilarRecipeResponse>> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    private interface  CallSimilarRecipe{
        @GET("recipes/{id}/similar")
        Call<List<SimilarRecipeResponse>> callSimilarRecipe(
                @Path("id") int id,
                @Query("number") String number,
                @Query("apiKey") String apiKey
        );
    }


}
