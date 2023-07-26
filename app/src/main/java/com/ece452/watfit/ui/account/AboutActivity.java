package com.ece452.watfit.ui.account;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ece452.watfit.R;

public class AboutActivity extends AppCompatActivity {

    private TextView healthCanadaTextView;
    private TextView wellnessTogetherTextView;
    private TextView csepTextView;
    private TextView diabetesCanadaTextView;
    private TextView spoonacularTextView;
    private TextView apiNinjasTextView;
    private TextView ytbDataApiTextView;
    private TextView openaiApiTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        healthCanadaTextView = findViewById(R.id.lb_health_canada);
        wellnessTogetherTextView = findViewById(R.id.lb_wellness_together);
        csepTextView = findViewById(R.id.lb_csep);
        diabetesCanadaTextView = findViewById(R.id.lb_diabetes_canada);
        spoonacularTextView = findViewById(R.id.lb_spoonacular);
        apiNinjasTextView = findViewById(R.id.lb_api_ninjas);
        ytbDataApiTextView = findViewById(R.id.lb_ytb_data_api);
        openaiApiTextView = findViewById(R.id.lb_openai_api);

        // add back button to the top-left corner
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        }

        // navigate to corresponding websites when TextView is clicked
        healthCanadaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.canada.ca/en/health-canada.html";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
        wellnessTogetherTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.wellnesstogether.ca/en-CA";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
        csepTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://csep.ca/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
        diabetesCanadaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.diabetes.ca/nutrition---fitness";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        // navigate to corresponding api sites when textview is clicked
        spoonacularTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://spoonacular.com/food-api";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
        apiNinjasTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api-ninjas.com/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
        ytbDataApiTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://developers.google.com/youtube/v3";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        openaiApiTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://openai.com/blog/openai-api";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    // nav to AccountActivity when back button is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back button click
            startActivity(new Intent(AboutActivity.this, AccountActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}