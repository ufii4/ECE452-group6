package com.ece452.watfit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ece452.watfit.data.FitnessGoal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FitnessGoalActivity extends AppCompatActivity {
    ImageButton bt_add;
    private ConstraintLayout parentContainer;

    private List<FitnessGoal> fitnessGoals = new ArrayList<>();

    private FitnessGoalAdapter adapter;

    @Inject
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_goal);

        // set back button in top-left corner
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        }

        // Set the initial value of currentCardId to the ID of the first card
        RecyclerView fitnessGoalCards = findViewById(R.id.rv_fitness_goal);
        DocumentReference ref = db.collection("users").document(FirebaseAuth.getInstance().getUid());
        ref.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<Map<String, Object>> goals = (List<Map<String, Object>>) documentSnapshot.get("goals");
                if (goals != null) {
                    for (Map<String, Object> goalMap : goals) {
                        fitnessGoals.add(new FitnessGoal(goalMap));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        adapter = new FitnessGoalAdapter(fitnessGoals);
        fitnessGoalCards.setAdapter(adapter);
        fitnessGoalCards.setLayoutManager(new LinearLayoutManager(this));

        bt_add = findViewById(R.id.ibt_add_fg);
        // Add new fitness goal section when add button is clicked
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGoalCard();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }

    // nav to AccountActivity when back button is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back button click
            startActivity(new Intent(FitnessGoalActivity.this, AccountActivity.class));
            finish();
            return true;
        }
        if (item.getItemId() == R.id.menu_submit_button) {
            DocumentReference ref = db.collection("users").document(FirebaseAuth.getInstance().getUid());
            ref.update("goals", fitnessGoals);
            Toast.makeText(FitnessGoalActivity.this, "Fitness Goal Saved!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Create new fitness goal section below the current visible section
    public void addGoalCard() {
        fitnessGoals.add(new FitnessGoal(FitnessGoal.Type.WEIGHT, null, LocalDate.now(), null));
        adapter.notifyItemInserted(fitnessGoals.size() - 1);
    }
}