package com.ece452.watfit.data.source.remote;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.ece452.watfit.data.models.Exercise;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ExerciseServiceTest {
    private NinjaDataSource ninjaDataSource;
    private ExerciseService exerciseService;

    @Before
    public void setUp() {
        ninjaDataSource = new NinjaDataSource();
        exerciseService = ninjaDataSource.exerciseService;
    }

    @Test
    public void getExercisesTest() {
        exerciseService.getExercises("press", "strength", "chest", "beginner").test().awaitCount(1).assertValue(response -> {
            List<Exercise> exercises = response;
            return exercises.size() > 0 && exercises.stream().allMatch(exercise -> exercise.name.toLowerCase().contains("press"));
        });
    }
}