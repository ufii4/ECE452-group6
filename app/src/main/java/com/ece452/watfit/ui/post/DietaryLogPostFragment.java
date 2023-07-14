package com.ece452.watfit.ui.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.ece452.watfit.data.DietaryLogPost;

public class DietaryLogPostFragment extends PostFragment {
    public DietaryLogPostFragment(DietaryLogPost post) {
        super(post);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }
}
