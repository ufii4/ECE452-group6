package com.ece452.watfit.data;

public class UserProfile {
    public String name;
    public double height;
    public double weight;
    public int age;
    public String gender;
    public Double waist;
    public Double hip;

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
        return gender;
    }

    public Double getWaist() {
        if (waist == null) {
            return 0.0;
        }
        return waist;
    }

    public Double getHip() {
        if (hip == null) {
            return 0.0;
        }
        return hip;
    }
}
