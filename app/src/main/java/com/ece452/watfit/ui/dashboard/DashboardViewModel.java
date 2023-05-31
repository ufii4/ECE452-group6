package com.ece452.watfit.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ece452.watfit.data.DietaryLogWithEntries;
import com.ece452.watfit.data.DietaryRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Flowable;

@HiltViewModel
public class DashboardViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    private final DietaryRepository dietaryRepository;

    @Inject
    public DashboardViewModel(DietaryRepository dietaryRepository) {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
        this.dietaryRepository = dietaryRepository;

        dietaryRepository.getDietaryLog("2023-05-30").subscribe(dietaryLog -> {
            mText.setValue(dietaryLog.date);
        });
    }

    public Flowable<DietaryLogWithEntries> getDietaryLogWithEntries() {
        return this.dietaryRepository.getDietaryLogWithEntries("2023-05-30");
    }

    public LiveData<String> getText() {
        return mText;
    }
}