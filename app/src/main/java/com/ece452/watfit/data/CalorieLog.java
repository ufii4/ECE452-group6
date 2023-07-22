package com.ece452.watfit.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;
import java.util.Date;

public class CalorieLog {

    private double dailyCalorie = 0;
    private List<Double> calorieList = new ArrayList<>();
    private List<Ingredient>  foodList = new ArrayList<>();
    public Date date = null;

    public CalorieLog() {}

    public CalorieLog(double dailyCalorie, List<Double> calorieList, List<Ingredient> foodList, Date date) {
        this.dailyCalorie = dailyCalorie;
        this.calorieList = calorieList;
        this.foodList = foodList;
        this.date = date;
    }

    public CalorieLog(Map<String, Object> map) {
        if (map.get("foodList") != null) {
            this.foodList = (List<Ingredient>) map.get("foodList");
        }
        if (map.get("startDate") != null) {
            this.calorieList = (List<Double>) map.get("calorieList");
        }
        if (map.get("endDate") != null) {
            this.dailyCalorie = (double) map.get("dailyCalorie");
        }
    }

    public double getDailyCalorie() {
        return dailyCalorie;
    }

    public void setDailyCalorie(double dailyCalorie) {
        this.dailyCalorie = dailyCalorie;
    }

    public List<Double> getCalorieList() {
        return calorieList;
    }

    public void setCalorieList(List<Double> calorieList) {
        this.calorieList = calorieList;
    }

    public List<Ingredient> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<Ingredient> foodList) {
        this.foodList = foodList;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate(){
        return date != null ? date : null;
    }
}

