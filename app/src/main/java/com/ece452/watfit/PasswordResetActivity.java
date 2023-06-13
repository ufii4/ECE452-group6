package com.ece452.watfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordResetActivity extends AppCompatActivity {

    private Button bt_confirm;
    private EditText et_old_password;
    private EditText et_new_password;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        auth = FirebaseAuth.getInstance();

        // add back button to the top-left corner
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        }

        bt_confirm = findViewById(R.id.bt_confirm_pr);
        et_old_password = findViewById(R.id.et_oldpassword_pr);
        et_new_password = findViewById(R.id.et_newpassword_pr);

        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredOldPass = et_old_password.getText().toString(); // get user entered old password
                String enteredNewPass = et_new_password.getText().toString(); // get user entered new password
                FirebaseUser currentUser = auth.getCurrentUser();
                // check if they matches
                String userEmail = currentUser.getEmail();
                AuthCredential credential = EmailAuthProvider.getCredential(userEmail, enteredOldPass);
                currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            // password verification successful
                            // check if new password meets requirement
                            if(TextUtils.isEmpty(enteredNewPass) || enteredNewPass.length() < 6){
                                Toast.makeText(PasswordResetActivity.this, "Invalid New Password. Make Sure It Has At Least 6 Characters", Toast.LENGTH_LONG).show();
                            }
                            else if(enteredNewPass.equals(enteredOldPass)){
                                Toast.makeText(PasswordResetActivity.this, "You Can't Reuse an Old Password", Toast.LENGTH_LONG).show();
                            }
                            else{
                                // reset password if it meets requirements
                                updatePassword(currentUser, enteredNewPass);
                            }
                        } else{
                            // password verification failed
                            Toast.makeText(PasswordResetActivity.this, "Invalid Old Password. Please Try Again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private void updatePassword(FirebaseUser currentUser, String new_password) {
        currentUser.updatePassword(new_password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> updateTask) {
                if (updateTask.isSuccessful()) {
                    // Password update successful, navigate to homescreen
                    Toast.makeText(PasswordResetActivity.this, "Password Changed", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PasswordResetActivity.this, MainActivity.class));
                    finish();
                } else {
                    // Password update failed
                    Toast.makeText(PasswordResetActivity.this, "Failed to Change Password. Please Try Again Later", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // nav to AccountActivity when back button is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back button click
            startActivity(new Intent(PasswordResetActivity.this, AccountActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}