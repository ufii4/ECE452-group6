package com.ece452.watfit.data;

import java.time.LocalDate;

public class Post {
    public Post() {
        this.date = LocalDate.now().toString();
    }

    public Post(String authorId, String title, String description) {
        this.author = authorId;
        this.title = title;
        this.description = description;
        this.date = LocalDate.now().toString();
    }

    public enum PostType {
        DIETARY_LOG,
        WORKOUT_LOG,
        FITNESS_DASHBOARD,
        RECIPE,
        MEAL_PLAN,
        WORKOUT_PLAN,
        OTHER
    }

    public String title;

    public String description;

    public String author;

    public String date;

    public Boolean isPublic = false;

    public PostType type = PostType.OTHER;
}
