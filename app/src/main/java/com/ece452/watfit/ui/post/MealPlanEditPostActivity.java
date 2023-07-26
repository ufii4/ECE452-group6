package com.ece452.watfit.ui.post;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.ece452.watfit.data.models.MealPlanPost;
import com.ece452.watfit.data.models.Post;

public class MealPlanEditPostActivity extends EditPostActivity {
    public static final String BREAKFAST = "breakfast";
    public static final String LUNCH = "lunch";
    public static final String DINNER = "dinner";

    private String breakfastId;
    private String lunchId;
    private String dinnerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        breakfastId = getIntent().getStringExtra(BREAKFAST);
        lunchId = getIntent().getStringExtra(LUNCH);
        dinnerId = getIntent().getStringExtra(DINNER);
    }

    @Override
    public Post getPost(String authorId, String title, String description, Bitmap screenShot) {
        return new MealPlanPost(authorId, title, description, breakfastId, lunchId, dinnerId);
    }
}
