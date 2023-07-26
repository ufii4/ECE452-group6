package com.ece452.watfit.data.models;

public class Exercise {
    public String name;
    public String type;
    public String muscle;
    public String equipment;
    public String difficulty;
    public String instructions;

    public Exercise(String name) {
        this.name = name;
    }

    public Exercise() {}
}
