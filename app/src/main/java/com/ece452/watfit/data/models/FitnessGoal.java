package com.ece452.watfit.data.models;

import java.time.LocalDate;
import java.util.Map;

public class FitnessGoal {
    public enum Type {
        WEIGHT("Weight"),
        CALORIE_INTAKE("Calorie Intake per Day"),
        CALORIE_CONSUMPTION("Calorie Consumption per Day"),
        BMI("BMI");

        private final String displayName;

        Type(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public Type type = null;
    public Integer value = null;
    public LocalDate startDate = null;
    public LocalDate endDate = null;

    public FitnessGoal() {}

    public FitnessGoal(Map<String, Object> map) {
        if (map.get("type") != null) {
            this.type = Type.valueOf((String) map.get("type"));
        }
        if (map.get("value") != null) {
            this.value = ((Long) map.get("value")).intValue();
        }
        if (map.get("startDate") != null) {
            this.startDate = LocalDate.parse((String) map.get("startDate"));
        }
        if (map.get("endDate") != null) {
            this.endDate = LocalDate.parse((String) map.get("endDate"));
        }
    }

    public FitnessGoal(Type type, Integer value, LocalDate startDate, LocalDate endDate) {
        this.type = type;
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public FitnessGoal(Type type, Integer value, String startDate, String endDate) {
        this.type = type;
        this.value = value;
        this.startDate = LocalDate.parse(startDate);
        this.endDate = LocalDate.parse(endDate);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getUnit() {
        switch (type) {
            case WEIGHT:
                return "kg";
            case CALORIE_INTAKE:
            case CALORIE_CONSUMPTION:
                return "kcal";
            case BMI:
                return "";
        }
        return "";
    }

    public String getStartDate(){
        return startDate != null ? startDate.toString() : null;
    }

    public String getEndDate() {
        return endDate != null ? endDate.toString() : null;
    }
}
