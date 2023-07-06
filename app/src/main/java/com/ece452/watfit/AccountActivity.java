package com.ece452.watfit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ece452.watfit.data.UserProfile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AccountActivity extends AppCompatActivity {


    private Button edit_profile;
    private Button set_goal;
    private Button change_password;
    private Button shared_with_me;
    private Button about;
    private TextView lb_title;
    private TextView lb_bmi;
    private TextView lb_bodyfat;
    private TextView lb_waisthip;

    @Inject
    FirebaseFirestore db;

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
        about = findViewById(R.id.bt_about_ac);
        lb_title = findViewById(R.id.lb_title_ac);
        lb_bmi = findViewById(R.id.lb_bmi_ac);
        lb_bodyfat = findViewById(R.id.lb_bodyfat_ac);
        lb_waisthip = findViewById(R.id.lb_waisthip_ac);

        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserProfile profile = documentSnapshot.toObject(UserProfile.class);

                if (profile == null) {
                    profile = new UserProfile();
                }

                // Display name of user in title
                lb_title.setText("Welcome " + profile.getName());
                double bmi = calculateBMI(profile.height, profile.weight);
                double bodyfat = calculateBodyfat(bmi, profile.age, profile.gender);
                double waisthip = calculatWaisthip(profile.getWaist(), profile.getHip());
                DecimalFormat df = new DecimalFormat("#.##");
                if(bmi == -1){
                    lb_bmi.setText("N/A");
                }
                else{
                    lb_bmi.setText(df.format(bmi));
                    if(bmi >= 25){
                        lb_bmi.setTextColor(Color.RED);
                    }
                    else{
                        lb_bmi.setTextColor(Color.GREEN);
                    }
                }

                if(bodyfat == -1){
                    lb_bodyfat.setText("N/A");
                }
                else{
                    lb_bodyfat.setText(df.format(bodyfat));
                }

                if(waisthip == -1){
                    lb_waisthip.setText("N/A");
                }
                else{
                    lb_waisthip.setText(df.format(waisthip));
                }
            }
        });

        // nav to BasicDiameterActivity if Edit Profile button is clicked
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, BasicDiameterActivity.class));
                finish();
            }
        });

        // nav to PasswordResetActivity if Change Password button is clicked
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
                startActivity(new Intent(AccountActivity.this, FitnessGoalActivity.class));
                finish();
            }
        });

        shared_with_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, SharedWithMeActivity.class));
                finish();
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, AboutActivity.class));
                finish();
            }
        });

    }

    // add logout button to action bar (header)
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    // nav to previous page if back button is clicked
    // logout if logout button is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back button click
//            startActivity(new Intent(AccountActivity.this, MainActivity.class));
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

    public double calculateBMI(double height_cm, double weight_kg){
        // error handling
        if(height_cm <= 0 || weight_kg <= 0){
            return -1;
        }
        // BMI = kg/m^2
        // (formula based on https://www.diabetes.ca/managing-my-diabetes/tools---resources/body-mass-index-(bmi)-calculator#:~:text=How%20to%20calculate%20Body%20Mass,range%20is%2018.5%20to%2024.9.)
        return weight_kg / ((height_cm/100) * (height_cm/100));
    }

    public double calculateBodyfat(double BMI, double age, String gender){
        // error handling
        if(BMI == -1 || age < 0 || gender == null){
            return -1;
        }
        // formula based on https://en.wikipedia.org/wiki/Body_fat_percentage#:~:text=The%20body%20fat%20percentage%20(BFP,fat%20and%20storage%20body%20fat.
        // (15 years and younger) child body fat = (1.51 * BMI) - (0.7 * age) - (3.6 * sex) + 1.4
        // adult body fat = (1.39 * BMI) - (0.16 * age) - (10.34 * sex) - 9
        double sex = 0; // sex factor = 0 for female and 1 for male
        if(gender.equals("male")){
            sex = 1;
        }
        // child body fat
        if(age <= 15){
            return (1.51 * BMI) - (0.7 * age) - (3.6 * sex) + 1.4;
        }
        // adult body fat
        else{
            return (1.39 * BMI) - (0.16 * age) - (10.34 * sex) - 9;
        }
    }

    public double calculatWaisthip(double waist_cm, double hip_cm){
        // error handling
        if(waist_cm <= 0 || hip_cm <= 0){
            return -1;
        }
        // formula based on https://nutritionalassessment.mumc.nl/en/waist-hip-ratio-whr-and-waist-circumference#:~:text=The%20Waist%2DHip%20Ratio%20(WHR)&text=The%20Waist%20Hip%20Ratio%20is,%3D%20waist%20circumference%20%2F%20hip%20circumference.
        // WHR = waist circumference / hip circumference
        return waist_cm/hip_cm;
    }
}