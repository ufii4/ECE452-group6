package com.ece452.watfit.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;

public class ExerciseLog {

    private double dailyCalorie = 0;
    private List<Double> calorieList = new ArrayList<>();
    private List<Exercise>  exerciseList = new ArrayList<>();
    public Timestamp date = null;

    public ExerciseLog() {}

    public ExerciseLog(double dailyCalorie, List<Double> calorieList, List<Exercise> exerciseList, Timestamp date) {
        this.dailyCalorie = dailyCalorie;
        this.calorieList = calorieList;
        this.exerciseList = exerciseList;
        this.date = date;
    }

    public ExerciseLog(Map<String, Object> map) {
        if (map.get("exerciseList") != null) {
            this.exerciseList = (List<Exercise>) map.get("exerciseList");
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

    public List<Exercise> getExerciseList() {
        return exerciseList;
    }

    public void setExerciseList(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Timestamp getDate(){
        return date != null ? date : null;
    }
}

