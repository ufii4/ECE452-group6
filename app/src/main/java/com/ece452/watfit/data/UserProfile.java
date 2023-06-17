package com.ece452.watfit.data;

public class UserProfile {
    public String name = "Anonymous";
    public double height = 0;
    public double weight = 0;
    public int age = 0;
    public String gender = "Male";
    public double waist = 0;
    public double hip = 0;

    public UserProfile() {}

    public UserProfile(String name, double height, double weight, int age, String gender, Double waist, Double hip) {
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.gender = gender;
        this.waist = waist;
        this.hip = hip;
    }

    public String getName() {
        return name;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        if (gender == null) {
            return "Male";
        }
        return gender;
    }

    public double getWaist() {
        return waist;
    }

    public double getHip() {
        return hip;
    }
}
