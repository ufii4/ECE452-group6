package com.ece452.watfit.data.models;

import java.util.List;

public class Ingredient {
        public String name;
        public int id;
        public Nutrition nutrition;
        public String image;
        public List<String> possibleUnits;

        public Ingredient(String name, int id, String image) {
                this.name = name;
                this.id = id;
                this.image = image;
        }
}
