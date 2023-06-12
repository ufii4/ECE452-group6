package com.ece452.watfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button bt_register;
    private FirebaseAuth auth; // authentication variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        bt_register = findViewById(R.id.bt_login);

        auth = FirebaseAuth.getInstance();

        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(RegisterActivity.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                } else if(txt_password.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Password Too Short", Toast.LENGTH_SHORT).show();
                } else{
                    registerUser(txt_email,txt_password);
                }
            }
        });
    }

    private void registerUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Registering Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, BasicDiameterActivity.class));
                    StartActivity.sa.finish(); // end StartActivity
                    finish();
                } else{
                    Toast.makeText(RegisterActivity.this, "Registering Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}