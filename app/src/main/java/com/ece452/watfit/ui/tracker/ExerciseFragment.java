package com.ece452.watfit.ui.tracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.ece452.watfit.EditPostActivity;
import com.ece452.watfit.R;
import com.ece452.watfit.data.Exercise;
import com.ece452.watfit.data.Caloriesburned;
import com.ece452.watfit.data.source.remote.ExerciseService;
import com.ece452.watfit.data.source.remote.NinjaDataSource;
import com.ece452.watfit.ExerciseDisplayAdapter;
import com.ece452.watfit.ExerciseSearchAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import io.reactivex.rxjava3.subscribers.TestSubscriber;

public class ExerciseFragment extends Fragment {

    private ExerciseService exerciseService;
    private ExerciseDisplayAdapter ExerciseDisplayAdapter;
    private List<Exercise> exerciseList;
    private List<Exercise> selectedExerciseList;

    private EditText estimateTimeEditText;
    private Button addButtonExercise;
    private TextView calorieTotalExerciseTextView;
    private TextView textViewViewExerciseSelected;
    private Button submitButtonExercise;
    private ListView exerciseListView;
    private SearchView exerciseSearchView;
    private NinjaDataSource ninjaDataSource;
    private double dailyCalorie = 0;
    private List<Double> calorieList = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_exercise, container, false);

        ((AppCompatActivity) requireActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        setHasOptionsMenu(true);

        exerciseList = new ArrayList<>();
        selectedExerciseList = new ArrayList<>();

        exerciseSearchView = root.findViewById(R.id.searchViewExercise);
        exerciseListView = root.findViewById(R.id.exerciseListView);
        estimateTimeEditText = root.findViewById(R.id.estimateTime);
        addButtonExercise = root.findViewById(R.id.addButtonExercise);
        calorieTotalExerciseTextView = root.findViewById(R.id.calorieTotalExercise);
        submitButtonExercise = root.findViewById(R.id.submitButtonExercise);
        textViewViewExerciseSelected = root.findViewById(R.id.textViewViewExerciseSelected);

        exerciseSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performExerciseSearch(query);
                ExerciseSearchAdapter adapterSearch = new ExerciseSearchAdapter(root.getContext(), exerciseList);
                exerciseListView.setAdapter(adapterSearch);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    displayElementExceptList(root);
                } else {
                    hideElementExceptList(root);
                }

                return false;
            }
        });

        ListView exerciseDisplayList = root.findViewById(R.id.exerciseSelectList);
        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                exerciseListView.setAdapter(null);
                displayElementExceptList(root);
                textViewViewExerciseSelected.setText(exerciseList.get(i).name);
                addButtonExercise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //get calorie information from api
                        selectedExerciseList.add(exerciseList.get(i));
                        List<Caloriesburned> burnedList = exerciseService.getCaloriesburned("skiing", null, estimateTimeEditText.getText().toString()).blockingFirst();
                        for (int j = 0; j < 10; j++) {
                            if(j == i){
                                TextView calorieExercise = root.findViewById(R.id.calorieExercise);
                                calorieExercise.setText(burnedList.get(j).total_calories);
                                calorieList.add(Double.parseDouble(burnedList.get(j).total_calories));
                                //display on the page
                                ExerciseDisplayAdapter = new ExerciseDisplayAdapter(root.getContext(),selectedExerciseList,calorieList);
                                exerciseDisplayList.setAdapter(ExerciseDisplayAdapter);
                                dailyCalorie += Double.parseDouble(burnedList.get(j).total_calories);
                                calorieTotalExerciseTextView.setText(Double.toString(dailyCalorie));
                            }
                        }
                    }
                });
                estimateTimeEditText.setText("");
                exerciseSearchView.setQuery("",false);
            }
        });
        exerciseListView.setAdapter(null);

        submitButtonExercise.setOnClickListener(v -> submitExercises());

        return root;
    }

    private void performExerciseSearch(String query) {
        ninjaDataSource = new NinjaDataSource();
        exerciseService = ninjaDataSource.exerciseService;
        List<Exercise> exercises = exerciseService.getExercises(query, null, null, null).blockingFirst();
        if (exercises != null) {
            exerciseList.clear();
            exerciseList.addAll(exercises);
        }
    }

    private void addExercise() {
        int position = exerciseListView.getCheckedItemPosition();
        if (position != ListView.INVALID_POSITION) {
            Exercise exercise = exerciseList.get(position);
            selectedExerciseList.add(exercise);
            int totalCalories = calculateTotalCalories(selectedExerciseList);
            calorieTotalExerciseTextView.setText(String.valueOf(totalCalories));
        }
    }

    private void submitExercises() {
        NavHostFragment.findNavController(this).popBackStack();
    }

    private int calculateTotalCalories(List<Exercise> exercises) {
        int totalCalories = 0;
        for (Exercise exercise : exercises) {
            // Calculate total calories based on exercise duration, intensity, etc.
            // Add the calories burned by each exercise to the total
            // totalCalories += exercise.getCaloriesBurned();
        }
        return totalCalories;
    }

    /********* Sharing Button ***********/
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }
    // add account button to action bar (header)
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_share_button, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // back button is clicked
        if (item.getItemId() == android.R.id.home) {
            NavHostFragment.findNavController(this).popBackStack();
            return true;
        }
        // share button is clicked
        if (item.getItemId() == R.id.share_post_button) {
            // handle account button click
            // TODO: take a screenshot on the current exercise fragment before navigate to EditPostActivity
            startActivity(new Intent(getActivity(), EditPostActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        exerciseList.clear();
    }

    private void hideElementExceptList(View root){
        TextView t1 = root.findViewById(R.id.estimateTime);
        TextView t2 = root.findViewById(R.id.exerciseCalorieText);
        TextView t3 = root.findViewById(R.id.calorieExercise);
        TextView t4 = root.findViewById(R.id.calorieTotalExerciseText);
        TextView t5 = root.findViewById(R.id.calorieTotalExercise);
        Button b2 = root.findViewById(R.id.addButtonExercise);
        Button b3 = root.findViewById(R.id.submitButtonExercise);
        ListView list = root.findViewById(R.id.exerciseListView);
        ListView list1 = root.findViewById(R.id.exerciseSelectList);
        t1.setVisibility(View.INVISIBLE);
        t2.setVisibility(View.INVISIBLE);
        t3.setVisibility(View.INVISIBLE);
        t4.setVisibility(View.INVISIBLE);
        t5.setVisibility(View.INVISIBLE);
        b2.setVisibility(View.INVISIBLE);
        b3.setVisibility(View.INVISIBLE);
        list.setVisibility(View.VISIBLE);
        list1.setVisibility(View.INVISIBLE);
    }
    private void displayElementExceptList(View root) {
        TextView t1 = root.findViewById(R.id.estimateTime);
        TextView t2 = root.findViewById(R.id.exerciseCalorieText);
        TextView t3 = root.findViewById(R.id.calorieExercise);
        TextView t4 = root.findViewById(R.id.calorieTotalExerciseText);
        TextView t5 = root.findViewById(R.id.calorieTotalExercise);
        Button b2 = root.findViewById(R.id.addButtonExercise);
        Button b3 = root.findViewById(R.id.submitButtonExercise);
        ListView list = root.findViewById(R.id.exerciseListView);
        ListView list1 = root.findViewById(R.id.exerciseSelectList);
        t1.setVisibility(View.VISIBLE);
        t2.setVisibility(View.VISIBLE);
        t3.setVisibility(View.VISIBLE);
        t4.setVisibility(View.VISIBLE);
        t5.setVisibility(View.VISIBLE);
        b2.setVisibility(View.VISIBLE);
        b3.setVisibility(View.VISIBLE);
        list.setVisibility(View.INVISIBLE);
        list1.setVisibility(View.VISIBLE);
    }
}
