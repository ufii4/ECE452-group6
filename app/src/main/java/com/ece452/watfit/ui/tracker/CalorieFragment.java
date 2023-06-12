package com.ece452.watfit.ui.tracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
        View root = binding.getRoot();

        Button backButton = root.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            TrackerFragment trackerFragment = new TrackerFragment();

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, trackerFragment);
            fragmentTransaction.addToBackStack(null);  // This line is optional, and will add the transaction to the back stack

            fragmentTransaction.commit();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
