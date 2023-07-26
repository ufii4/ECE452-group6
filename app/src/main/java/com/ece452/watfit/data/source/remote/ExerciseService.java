package com.ece452.watfit.data.source.remote;

import com.ece452.watfit.data.ExerciseRepository;
import com.ece452.watfit.data.models.Exercise;
import com.ece452.watfit.data.models.Caloriesburned;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import retrofit2.http.GET;

public interface ExerciseService extends ExerciseRepository {
    @GET("v1/exercises")
    Flowable<List<Exercise>> getExercises(
            @retrofit2.http.Query("name") String name,
            @retrofit2.http.Query("type") String type,
            @retrofit2.http.Query("muscle") String muscle,
            @retrofit2.http.Query("difficulty") String difficulty
    );

    @GET("v1/caloriesburned")
    Flowable<List<Caloriesburned>> getCaloriesburned(
            @retrofit2.http.Query("activity") String activity,
            @retrofit2.http.Query("weight") String weight,
            @retrofit2.http.Query("duration") String duration
    );
}
