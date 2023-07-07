package com.ece452.watfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText email_1;
    private EditText password_1;
    private Button login_1;
    private TextView registerTextView;

    private FirebaseAuth auth; // firebase authentication variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_1 = findViewById(R.id.email_1);
        password_1 = findViewById(R.id.password_1);
        login_1 = findViewById(R.id.login_1);
        registerTextView = findViewById(R.id.lb_register_al);

        auth = FirebaseAuth.getInstance();

        // handle login button click
        login_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email_1.getText().toString();
                String txt_password = password_1.getText().toString();
                loginUser(txt_email, txt_password);
            }

            private void loginUser(String email, String password) {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                            StartActivity.sa.finish(); // end StartActivity
                            finish();
                        } else{
                            Toast.makeText(LoginActivity.this, "Invalid Credential", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // navigate to RegisterActivity when register TextView is clicked
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }
}