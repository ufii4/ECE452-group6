package com.ece452.watfit.ui.tracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ece452.watfit.R;
import com.ece452.watfit.databinding.FragmentCalorieIntakeBinding;

public class CalorieFragment extends Fragment {
    private FragmentCalorieIntakeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TrackerViewModel trackerViewModel =
                new ViewModelProvider(this).get(TrackerViewModel.class);

        binding = FragmentCalorieIntakeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();return root;
    }

    @Override
    public void onDestroyView() {
        ViewGroup mContainer = (ViewGroup) getActivity().findViewById(R.id.nav_host_fragment_activity_main);
        mContainer.removeAllViews();
        super.onDestroyView();
        binding = null;
    }

}
