package com.ece452.watfit;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ece452.watfit.data.DietaryRepository;
import com.ece452.watfit.ui.dashboard.DashboardFragment;
import com.ece452.watfit.ui.home.HomeFragment;
import com.ece452.watfit.ui.tracker.TrackerFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.fragment.NavHostFragment;

import com.ece452.watfit.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationBarView;
import android.widget.LinearLayout;
import android.view.ViewGroup;
import android.util.Log;
import android.view.LayoutInflater;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityMainBinding binding;
    private FirebaseAuth auth; // firebase authentication variable

    @Inject
    DietaryRepository dietaryRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // auth variable will help check whether user is signed in or not (see onStart method below)
        auth = FirebaseAuth.getInstance();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_tracker)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    navController.navigate(R.id.navigation_home);
                    return true;
                } else if (item.getItemId() == R.id.navigation_dashboard) {
                    navController.navigate(R.id.navigation_dashboard);
                    return true;
                } else if (item.getItemId() == R.id.navigation_tracker) {
                    navController.navigate(R.id.navigation_tracker);
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        // check if user is signed in (non-null) and update UI accordingly
        FirebaseUser currentUser = auth.getCurrentUser();
        // if user is not signed in, navigate to StartActivity. Else, stay at MainActivity
        if(currentUser == null){
            startActivity(new Intent(MainActivity.this, StartActivity.class));
            finish();
        }
    }

    // add account button to action bar (header)
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_account_button, menu);
        return true;
    }

    // handle account button clicked action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.account_button) {
            // handle account button click
            startActivity(new Intent(MainActivity.this, AccountActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }

}