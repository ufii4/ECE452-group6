package com.ece452.watfit.ui.dashboard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ece452.watfit.data.DietaryRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DashboardViewModel extends ViewModel {
    private final MutableLiveData<Integer> healthScore;
    private final MutableLiveData<String> suggestion1;
    private final MutableLiveData<String> suggestion2;
    private final MutableLiveData<String> suggestion3;

    private final DietaryRepository dietaryRepository;

    @Inject
    public DashboardViewModel(DietaryRepository dietaryRepository) {
        this.dietaryRepository = dietaryRepository;
        
        healthScore = new MutableLiveData<>();
        suggestion1 = new MutableLiveData<>();
        suggestion2 = new MutableLiveData<>();
        suggestion3 = new MutableLiveData<>();

        // Set initial values
        healthScore.setValue(80);
        suggestion1.setValue("");
        suggestion2.setValue("");
        suggestion3.setValue("");

    }
}