package com.ece452.watfit.data.models;

public class Caloriesburned {
    public String name;
    public String calories_per_hour;
    public String duration_minutes;
    public String total_calories;


    public Caloriesburned(String name, String calories_per_hour, String duration_minutes, String total_calories) {
        this.name = name;
        this.calories_per_hour = calories_per_hour;
        this.duration_minutes = duration_minutes;
        this.total_calories = total_calories;
    }

    public Caloriesburned() {}
}
