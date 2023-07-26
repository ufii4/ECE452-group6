package com.ece452.watfit.data.models;

public class DietaryLogPost extends Post {
    public String dietaryLogId;

    public DietaryLogPost() {
        super();
        this.type = PostType.DIETARY_LOG;
    }

    public DietaryLogPost(String authorId, String title, String description, String dietaryLogId) {
        super(authorId, title, description);
        this.dietaryLogId = dietaryLogId;
        this.type = PostType.DIETARY_LOG;
    }
}
