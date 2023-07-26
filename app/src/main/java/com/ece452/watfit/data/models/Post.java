package com.ece452.watfit.data.models;

import java.io.Serializable;
import java.time.Instant;

public class Post implements Serializable {
    public Post() {
        this.timestamp = Instant.now().toString();
    }

    public Post(String authorId, String title, String description) {
        this.author = authorId;
        this.title = title;
        this.description = description;
        this.timestamp = Instant.now().toString();
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

    public String timestamp;

    public Boolean isPublic = false;

    public PostType type = PostType.OTHER;

    public String id;
}
