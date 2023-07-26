package com.ece452.watfit.data;

import com.ece452.watfit.data.models.Caloriesburned;
import com.ece452.watfit.data.models.Exercise;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface ExerciseRepository {
    Flowable<List<Exercise>> getExercises(String name, String type, String muscle, String difficulty);
    Flowable<List<Caloriesburned>> getCaloriesburned(String activity, String weight, String duration);
}
