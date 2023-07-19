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

import com.ece452.watfit.ui.post.MealPlanEditPostActivity;
import com.ece452.watfit.ui.post.PostActivityHelper;

import java.util.concurrent.ThreadLocalRandom;

import io.reactivex.rxjava3.annotations.NonNull;

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
    int[] indoor_high_cal = {10,10,8,8,7,8,8,7,8,7};
    int[] outdoor_low_cal = {4,5,5,5,4,3,3,4,2,3};
    int[] outdoor_high_cal = {11,10,8,11,9,9,9,11,9,10};

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

        generateList();
        Button regenButton = (Button) findViewById(R.id.bt_regenerate_schedule);
        regenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateList();
            }
        });
        Button bt_fitness_preference_bar = findViewById(R.id.fitness_preferencebar);
        bt_fitness_preference_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    protected void generateList() {
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
        int t1 = ThreadLocalRandom.current().nextInt(1, 4);
        int t2 = ThreadLocalRandom.current().nextInt(1, 4);
        int t3 = ThreadLocalRandom.current().nextInt(1, 4);
        int t4 = ThreadLocalRandom.current().nextInt(1, 4);
        int t5 = ThreadLocalRandom.current().nextInt(1, 4);
        int t6 = ThreadLocalRandom.current().nextInt(1, 4);
        int t7 = ThreadLocalRandom.current().nextInt(1, 4);
        int t1_1 = ThreadLocalRandom.current().nextInt(0, 10);
        int t1_2 = ThreadLocalRandom.current().nextInt(0, 10);
        int t1_3 = ThreadLocalRandom.current().nextInt(0, 10);
        int t1_4 = ThreadLocalRandom.current().nextInt(0, 10);
        int t1_5 = ThreadLocalRandom.current().nextInt(0, 10);
        int t1_6 = ThreadLocalRandom.current().nextInt(0, 10);
        int t1_7 = ThreadLocalRandom.current().nextInt(0, 10);
        Mon1.setText("Exercise: "+indoor_low[t1_1]);
        Tue1.setText("Exercise: "+indoor_low[t1_2]);
        Wed1.setText("Exercise: "+indoor_low[t1_3]);
        Thu1.setText("Exercise: "+indoor_low[t1_4]);
        Fri1.setText("FExercise: "+indoor_low[t1_5]);
        Sat1.setText("Exercise: "+indoor_low[t1_6]);
        Sun1.setText("Exercise: "+indoor_low[t1_7]);
        Mon2.setText("Duration/Calories Burnt: "+String.valueOf(t1*10)+"min / "+String.valueOf(t1*10*indoor_low_cal[t1_1])+"cal");
        Tue2.setText("Duration/Calories Burnt: "+String.valueOf(t2*10)+"min / "+String.valueOf(t1*10*indoor_low_cal[t1_2])+"cal");
        Wed2.setText("Duration/Calories Burnt: "+String.valueOf(t3*10)+"min / "+String.valueOf(t1*10*indoor_low_cal[t1_3])+"cal");
        Thu2.setText("Duration/Calories Burnt: "+String.valueOf(t4*10)+"min / "+String.valueOf(t1*10*indoor_low_cal[t1_4])+"cal");
        Fri2.setText("Duration/Calories Burnt: "+String.valueOf(t5*10)+"min / "+String.valueOf(t1*10*indoor_low_cal[t1_5])+"cal");
        Sat2.setText("Duration/Calories Burnt: "+String.valueOf(t6*10)+"min / "+String.valueOf(t1*10*indoor_low_cal[t1_6])+"cal");
        Sun2.setText("Duration/Calories Burnt: "+String.valueOf(t7*10)+"min / "+String.valueOf(t1*10*indoor_low_cal[t1_7])+"cal");
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
    }
}