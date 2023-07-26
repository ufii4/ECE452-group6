package com.ece452.watfit.data.models;

public class MealPlanPost extends Post {
    public String breakfast;
    public String lunch;
    public String dinner;

    public MealPlanPost() {
        super();
        this.type = PostType.MEAL_PLAN;
    }

    public MealPlanPost(String authorId, String title, String description, String breakfast, String lunch, String dinner) {
        super(authorId, title, description);
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.type = PostType.MEAL_PLAN;
    }
}
