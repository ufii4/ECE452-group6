package com.ece452.watfit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ece452.watfit.data.FitnessGoal;
import com.ece452.watfit.ui.post.MealPlanEditPostActivity;
import com.ece452.watfit.ui.post.PostActivityHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.annotations.NonNull;
@AndroidEntryPoint
public class FitnessSchedulerActivity extends AppCompatActivity implements FitnessSchedulerPreferenceDialog.FitnessSchedulerPreferenceDialogListener {
    String[] indoor_low = {"Walking in place",
            "Yoga",
            "Pilates",
            "Tai Chi",
            "Stationary cycling",
            "Chair exercises",
            "Light aerobics",
            "Resistance band",
            "Dancing",
            "Stretching"
            };
    String[] indoor_medium = {"Elliptical training",
            "Low-impact aerobics",
            "Stair-stepping",
            "Rowing machine",
            "Jumping jacks",
            "High knees",
            "Low-impact Zumba",
            "TRX suspension",
            "Stability ball",
            "Core workouts"
    };
    String[] indoor_high = {"Jumping rope",
            "HIIT",
            "Circuit training",
            "Kickboxing",
            "Zumba",
            "Indoor cycling",
            "Step aerobics",
            "HI dancing",
            "Cardio kickboxing",
            "Rowing machine"
    };
    String[] outdoor_low = {"Walking",
            "Hiking",
            "Cycling",
            "Light swimming",
            "Canoeing",
            "Golfing",
            "Gardening",
            "Kung Fu",
            "Outdoor Yoga",
            "Pilates classes"
    };
    String[] outdoor_medium = {"Brisk walking",
            "Recreational cycling",
            "Rollerblading",
            "Tennis",
            "Badminton",
            "Paddleboarding",
            "Soccer",
            "Frisbee",
            "Volleyball",
            "Rock climbing"
    };
    String[] outdoor_high = {"Running",
            "Jogging",
            "Cycling",
            "Hill sprints",
            "HI circuit",
            "Boot-camping",
            "Swimming laps",
            "Skiing",
            "Boxing",
            "Outdoor HIIT"
    };
    int[] indoor_low_cal = {4,3,3,4,5,3,5,4,5,3};
    int[] indoor_medium_cal = {7,6,6,7,9,9,6,6,5,6};
    int[] indoor_high_cal = {10,10,8,8,7,8,8,7,8,7};
    int[] outdoor_low_cal = {4,5,5,5,4,3,3,4,2,3};
    int[] outdoor_medium_cal = {5,5,6,6,5,5,6,4,6,7};
    int[] outdoor_high_cal = {11,10,8,11,9,9,9,11,9,10};

    private static String exerciseType = "indoor";
    private static String intensity ="low";


    @Inject
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_scheduler);

        // add back button to the top-left corner
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        }

        Button bt_fitness_preference_bar = findViewById(R.id.fitness_preferencebar);
        bt_fitness_preference_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        ///fitness goal
        DocumentReference ref = db.collection("users").document(FirebaseAuth.getInstance().getUid());
        ref.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<Map<String, Object>> goals = (List<Map<String, Object>>) documentSnapshot.get("goals");
                if (goals != null) {
                    boolean isFitnessGoalCC = false;
                    for (Map<String, Object> goalMap : goals) {
                        if((FitnessGoal.Type.valueOf((String) goalMap.get("type"))== FitnessGoal.Type.CALORIE_CONSUMPTION)){
                            int calorie_Consumption = ((Long) goalMap.get("value")).intValue();
                            Log.d("HIHIHI", "calorie_Consumption:  "+calorie_Consumption);
                            isFitnessGoalCC = true;

                            generateList(calorie_Consumption);


                            Button regenButton = (Button) findViewById(R.id.bt_regenerate_schedule);
                            regenButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    generateList(calorie_Consumption);
                                }
                            });


                        }
                    }

                    if(!isFitnessGoalCC){
                        generateList(Integer.MIN_VALUE);
                        Button regenButton = (Button) findViewById(R.id.bt_regenerate_schedule);
                        regenButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                generateList(Integer.MIN_VALUE);
                            }
                        });

                    }
                }
            }
        });
    }

    protected void generateList(int calorie_Consumption) {
        String[] selectedArray;
        int [] selectCalArray;
        int floor = 1;
        int ceiling = 4;

        if (exerciseType.equals("indoor") && intensity.equals("low")) {
            selectedArray = indoor_low;
            selectCalArray = indoor_low_cal;
            floor = 1;
            ceiling = 4;
        } else if (exerciseType.equals("indoor") && intensity.equals("medium")) {
            selectedArray = indoor_medium;
            selectCalArray = indoor_medium_cal;
            floor = 5;
            ceiling = 8;
        } else if (exerciseType.equals("indoor") && intensity.equals("high")) {
            selectedArray = indoor_high;
            selectCalArray = indoor_high_cal;
            floor = 9;
            ceiling = 12;
        } else if (exerciseType.equals("outdoor") && intensity.equals("low")) {
            selectedArray = outdoor_low;
            selectCalArray = outdoor_low_cal;
            floor = 1;
            ceiling = 4;
        } else if (exerciseType.equals("outdoor") && intensity.equals("medium")) {
            selectedArray = outdoor_medium;
            selectCalArray = outdoor_medium_cal;
            floor = 5;
            ceiling = 8;
        } else if (exerciseType.equals("outdoor") && intensity.equals("high")) {
            selectedArray = outdoor_high;
            selectCalArray = outdoor_high_cal;
            floor = 9;
            ceiling = 12;
        } else {
            return;
        }

        TextView Mon1 = (TextView)findViewById(R.id.Monday_exercise_description);
        TextView Mon2 = (TextView)findViewById(R.id.Monday_exercise_Calories);
        TextView Tue1 = (TextView)findViewById(R.id.Tuesday_exercise_description);
        TextView Tue2 = (TextView)findViewById(R.id.Tuesday_exercise_Calories);
        TextView Wed1 = (TextView)findViewById(R.id.Wednesday_exercise_description);
        TextView Wed2 = (TextView)findViewById(R.id.Wednesday_exercise_Calories);
        TextView Thu1 = (TextView)findViewById(R.id.Thursday_exercise_description);
        TextView Thu2 = (TextView)findViewById(R.id.Thursday_exercise_Calories);
        TextView Fri1 = (TextView)findViewById(R.id.Friday_exercise_description);
        TextView Fri2 = (TextView)findViewById(R.id.Friday_exercise_Calories);
        TextView Sat1 = (TextView)findViewById(R.id.Saturday_exercise_description);
        TextView Sat2 = (TextView)findViewById(R.id.Saturday_exercise_Calories);
        TextView Sun1 = (TextView)findViewById(R.id.Sunday_exercise_description);
        TextView Sun2 = (TextView)findViewById(R.id.Sunday_exercise_Calories);
        int t1 = ThreadLocalRandom.current().nextInt(floor, ceiling);
        int t2 = ThreadLocalRandom.current().nextInt(floor, ceiling);
        int t3 = ThreadLocalRandom.current().nextInt(floor, ceiling);
        int t4 = ThreadLocalRandom.current().nextInt(floor, ceiling);
        int t5 = ThreadLocalRandom.current().nextInt(floor, ceiling);
        int t6 = ThreadLocalRandom.current().nextInt(floor, ceiling);
        int t7 = ThreadLocalRandom.current().nextInt(floor, ceiling);
        int t1_1 = ThreadLocalRandom.current().nextInt(0, 10);
        int t1_2 = ThreadLocalRandom.current().nextInt(0, 10);
        int t1_3 = ThreadLocalRandom.current().nextInt(0, 10);
        int t1_4 = ThreadLocalRandom.current().nextInt(0, 10);
        int t1_5 = ThreadLocalRandom.current().nextInt(0, 10);
        int t1_6 = ThreadLocalRandom.current().nextInt(0, 10);
        int t1_7 = ThreadLocalRandom.current().nextInt(0, 10);
        Mon1.setText("Exercise: "+selectedArray[t1_1]);
        Tue1.setText("Exercise: "+selectedArray[t1_2]);
        Wed1.setText("Exercise: "+selectedArray[t1_3]);
        Thu1.setText("Exercise: "+selectedArray[t1_4]);
        Fri1.setText("Exercise: "+selectedArray[t1_5]);
        Sat1.setText("Exercise: "+selectedArray[t1_6]);
        Sun1.setText("Exercise: "+selectedArray[t1_7]);

        int [] a1 = calorie_calc(t1, selectCalArray[t1_1], calorie_Consumption);
        int [] a2 = calorie_calc(t2, selectCalArray[t1_2], calorie_Consumption);
        int [] a3 = calorie_calc(t3, selectCalArray[t1_3], calorie_Consumption);
        int [] a4 = calorie_calc(t4, selectCalArray[t1_4], calorie_Consumption);
        int [] a5 = calorie_calc(t5, selectCalArray[t1_5], calorie_Consumption);
        int [] a6 = calorie_calc(t6, selectCalArray[t1_6], calorie_Consumption);
        int [] a7 = calorie_calc(t7, selectCalArray[t1_7], calorie_Consumption);

        int t1_cal = a1[0];
        int t2_cal = a2[0];
        int t3_cal = a3[0];
        int t4_cal = a4[0];
        int t5_cal = a5[0];
        int t6_cal = a6[0];
        int t7_cal = a7[0];
        int t1_time = a1[1];
        int t2_time = a2[1];
        int t3_time = a3[1];
        int t4_time = a4[1];
        int t5_time = a5[1];
        int t6_time = a6[1];
        int t7_time = a7[1];

        Mon2.setText("Duration/Calories Burnt: "+String.valueOf(t1_time)+"min / "+String.valueOf(t1_cal)+"kcal");
        Tue2.setText("Duration/Calories Burnt: "+String.valueOf(t2_time)+"min / "+String.valueOf(t2_cal)+"kcal");
        Wed2.setText("Duration/Calories Burnt: "+String.valueOf(t3_time)+"min / "+String.valueOf(t3_cal)+"kcal");
        Thu2.setText("Duration/Calories Burnt: "+String.valueOf(t4_time)+"min / "+String.valueOf(t4_cal)+"kcal");
        Fri2.setText("Duration/Calories Burnt: "+String.valueOf(t5_time)+"min / "+String.valueOf(t5_cal)+"kcal");
        Sat2.setText("Duration/Calories Burnt: "+String.valueOf(t6_time)+"min / "+String.valueOf(t6_cal)+"kcal");
        Sun2.setText("Duration/Calories Burnt: "+String.valueOf(t7_time)+"min / "+String.valueOf(t7_cal)+"kcal");
    }


    private int[] calorie_calc(int t, int calArray_val , int calorie_Consumption){
        int calo = t * 10 * calArray_val;
        int time = t * 10;

        if(calorie_Consumption>calo){
            return new int[] {calorie_Consumption, (calorie_Consumption / calo) *time };
        }

        return new int[] {calo, time};
    }

    public void openDialog(){
        FitnessSchedulerPreferenceDialog preferenceDialog = new FitnessSchedulerPreferenceDialog();
        preferenceDialog.show(getSupportFragmentManager(),"fitness schedular preference dialog");
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
            startActivity(new Intent(FitnessSchedulerActivity.this, AccountActivity.class));
            return true;
        }
        if(item.getItemId() == R.id.share_post_button) {
            PostActivityHelper.startEditPostActivity(
                    new Intent(FitnessSchedulerActivity.this, EditPostActivity.class),
                    getWindow().getDecorView().getRootView(), this
            );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void applyTexts(String exercise_type, String intensity) {
        Log.d("!!!!!!!!!!!!", "applyTexts: "+ exercise_type+"  "+intensity);
        this.exerciseType = exercise_type.trim().toLowerCase();
        this.intensity = intensity.trim().toLowerCase();
        generateList(Integer.MIN_VALUE);
    }
}

