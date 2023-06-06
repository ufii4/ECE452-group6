package com.ece452.watfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class BasicDiameterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText et_height;
    private EditText et_weight;
    private EditText et_age;
    private EditText et_waist;
    private EditText et_hip;
    private Spinner sp_gender;
    private Button bt_submit;
    private String selected_gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_diameter);

        et_height = findViewById(R.id.et_height_bd);
        et_weight = findViewById(R.id.et_weight_bd);
        et_age = findViewById(R.id.et_age_bd);
        et_waist = findViewById(R.id.et_waist_bd);
        et_hip = findViewById(R.id.et_hip_bd);
        bt_submit = findViewById(R.id.bt_submit_bd);

        sp_gender = findViewById(R.id.sp_gender_bd);
        sp_gender.setOnItemSelectedListener(this); // link spinner to spinnerListen to read selected value
        // add genders to spinner (dropdown)
        String[] gender_types = getResources().getStringArray(R.array.genders); // get items from gender.xml
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, gender_types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_gender.setAdapter(adapter);

        // If SUBMIT button is clicked
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // read EditText values
                double height = Double.parseDouble(et_height.getText().toString());
                double weight = Double.parseDouble(et_weight.getText().toString());
                double age = Double.parseDouble(et_age.getText().toString());
                double waist = Double.parseDouble(et_waist.getText().toString());
                double hip = Double.parseDouble(et_hip.getText().toString());

                // TODO: save user body diameters to database

                Toast.makeText(BasicDiameterActivity.this, "Body Diameter Updated Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(BasicDiameterActivity.this, MainActivity.class)); // navigate to home screen
                finish();
            }
        });
    }

    // take actions based on item selected in spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.sp_gender_bd){
            selected_gender = parent.getItemAtPosition(position).toString(); // read selected string in dropdown
            // Log.d("Info", selected_gender); validate string value
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}