package com.ece452.watfit.ui.tracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.ece452.watfit.R;
import com.ece452.watfit.databinding.FragmentTrackerBinding;

public class TrackerFragment extends Fragment {

    private FragmentTrackerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TrackerViewModel trackerViewModel =
                new ViewModelProvider(this).get(TrackerViewModel.class);

        binding = FragmentTrackerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button calorieButton = root.findViewById(R.id.calorieLogButton);
        calorieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.removeAllViews();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, new CalorieFragment(),null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        ViewGroup mContainer = (ViewGroup) getActivity().findViewById(R.id.nav_host_fragment_activity_main);
        mContainer.removeAllViews();
        super.onDestroyView();
        binding = null;
    }
}