package com.ece452.watfit.service;

public interface RecommendationService {
    class UserPotrait {
        public int fitnessLevel;
        public int nutritionLevel;
    }

    UserPotrait getUserPotrait();
}
