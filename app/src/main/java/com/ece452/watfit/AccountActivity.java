package com.ece452.watfit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends AppCompatActivity {


    private Button edit_profile;
    private Button set_goal;
    private Button change_password;
    private Button shared_with_me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // add back button to the top-left corner
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        }

        edit_profile = findViewById(R.id.bt_editprofile_ac);
        set_goal = findViewById(R.id.bt_setgoal_ac);
        change_password = findViewById(R.id.bt_changepass_ac);
        shared_with_me = findViewById(R.id.bt_share_ac);

        // nav to BasicDiameterActivity if Edit Profile button is clicked
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, BasicDiameterActivity.class));
                finish();
            }
        });

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, PasswordResetActivity.class));
                finish();
            }
        });

        set_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: nav to Fitness goal screen
            }
        });

        shared_with_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: nav to friend sharing screen
            }
        });

    }

    // add lgout button to action bar (header)
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    // nav to MainActivity if back button is clicked
    // logout if logout button is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back button click
            startActivity(new Intent(AccountActivity.this, MainActivity.class));
            finish();
            return true;
        }
        if (item.getItemId() == R.id.logout_button) {
            // handle logout button click
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(AccountActivity.this, "Log Out Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AccountActivity.this, StartActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}