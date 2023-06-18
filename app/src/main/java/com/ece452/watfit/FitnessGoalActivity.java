package com.ece452.watfit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

public class FitnessGoalActivity extends AppCompatActivity {
    ImageButton bt_add;
    private int currentCardId;
    private ConstraintLayout parentContainer;
    private int cardCounter = 1;
    private ArrayAdapter<CharSequence> adapter;

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
        View goalCard1 = findViewById(R.id.goal_card1_fg);
        currentCardId = goalCard1.getId();
        // Set the reference to the parent container
        parentContainer = findViewById(R.id.parentContainer);

        /*********** Set Spinner content for first goal card ******************/
        // Add dropdown content to the first card
        adapter = ArrayAdapter.createFromResource(this, R.array.fitness_goal_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Get a reference to the Spinner in the new rectangle card
        Spinner spinnerGoalCfg = goalCard1.findViewById(R.id.sp_goal_cfg);
        // Apply the custom ArrayAdapter to the Spinner
        spinnerGoalCfg.setAdapter(adapter);

        // disable the remove button for the first goal card
        ImageButton bt_remove = goalCard1.findViewById(R.id.ibt_remove_cfg);
        bt_remove.setVisibility(View.INVISIBLE);

        bt_add = findViewById(R.id.ibt_add_fg);
        // Add new fitness goal section when add button is clicked
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGoalCard();
            }
        });

        // TODO: Change the EditText hint in the card to the unit that matches the spinner selection

        // TODO: add a submit button to this activity (preferably below the last card). Once user clicks submit,
        //       the data from the current cards gets saved to the database
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
        return super.onOptionsItemSelected(item);
    }

    // Create new fitness goal section below the current visible section
    public void addGoalCard(){
        // Create a new goal card section
        LayoutInflater inflater = LayoutInflater.from(FitnessGoalActivity.this);
        final View goalCard = inflater.inflate(R.layout.card_fitness_goal, null);

        // Generate a unique ID for the new goal card
        int newCardId = View.generateViewId();
        goalCard.setId(newCardId);

        // Set layout params for positioning the new goal card below the current visible card
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.topToBottom = currentCardId; // Set the top-to-bottom constraint to the current card ID
        goalCard.setLayoutParams(layoutParams);

        // Update the currentCardId with the ID of the new card
        currentCardId = newCardId;
        // Add the new goal card to the parent container
        parentContainer.addView(goalCard);
        // Increment the card counter
        cardCounter++;

        /******** Add Dropdown (Spinner) selections *************/
        // Get a reference to the Spinner in the new rectangle card
        Spinner spinnerGoalCfg = goalCard.findViewById(R.id.sp_goal_cfg);
        // Apply the custom ArrayAdapter to the Spinner
        spinnerGoalCfg.setAdapter(adapter);

        /********* Add OnClick listener for the remove button ***************/
        ImageButton bt_remove = goalCard.findViewById(R.id.ibt_remove_cfg);
        bt_remove.setOnClickListener(new View.OnClickListener() {
            // TODO: Bug- Remove will mess up the card position:
            //  - Cards below the removed one will move to the top and covers the first card (first card is unremovable and should always remain the the same position).
            //  - Add button won't add new cards below the lowest card
            @Override
            public void onClick(View v) {
                // Get the position of the goal card to be removed
                int index = parentContainer.indexOfChild(goalCard);
                // Remove the entire rectangle card
                parentContainer.removeView(goalCard);

                // Adjust the IDs and constraints of the remaining goal cards below the removed card
                for (int i = index + 1; i < parentContainer.getChildCount(); i++) {
                    View card = parentContainer.getChildAt(i);
                    int cardId = View.generateViewId();
                    card.setId(cardId);

                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) card.getLayoutParams();
                    layoutParams.topToBottom = parentContainer.getChildAt(i - 1).getId();
                    card.setLayoutParams(layoutParams);
                }
            }
        });
    }
}