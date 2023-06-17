package com.ece452.watfit.ui.tracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.ece452.watfit.R;
import com.ece452.watfit.databinding.FragmentTrackerBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class TrackerFragment extends Fragment {

    private FragmentTrackerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTrackerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.calorieLogButton.setOnClickListener(v ->
                Navigation.findNavController(root).navigate(R.id.action_trackerFragment_to_calorieFragment));

        binding.exerciseLogButton.setOnClickListener(v ->
                Navigation.findNavController(root).navigate(R.id.action_trackerFragment_to_exerciseFragment));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
