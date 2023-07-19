package com.ece452.watfit.ui.tracker;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.ece452.watfit.EditPostActivity;
import com.ece452.watfit.R;
import com.ece452.watfit.data.ExerciseLog;
import com.ece452.watfit.data.Exercise;
import com.ece452.watfit.data.Caloriesburned;
import com.ece452.watfit.data.source.remote.ExerciseService;
import com.ece452.watfit.data.source.remote.NinjaDataSource;
import com.ece452.watfit.ExerciseDisplayAdapter;
import com.ece452.watfit.ExerciseSearchAdapter;

import com.ece452.watfit.ui.post.PostActivityHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Map;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

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
    private TextView textViewExerciseSelected;
    private Button submitButtonExercise;
    private ListView exerciseListView;
    private SearchView exerciseSearchView;
    private NinjaDataSource ninjaDataSource;
    private Button dateButton;
    private double dailyCalorie = 0;
    private Timestamp localDate = null;
    private List<Double> calorieList = new ArrayList<>();
    @Inject
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_exercise, container, false);
        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getUid());
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
        textViewExerciseSelected = root.findViewById(R.id.textViewExerciseSelected);
        dateButton = root.findViewById(R.id.dateButtonExercise);

        localDate = new Timestamp(System.currentTimeMillis());
        String selectedDate = new SimpleDateFormat("dd/MM/yyyy").format(localDate);
        dateButton.setText(selectedDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(localDate.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        localDate = new Timestamp(calendar.getTimeInMillis());

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Your UI updates here.
                calorieTotalExerciseTextView.setText(Double.toString(dailyCalorie));
                ListView listView = root.findViewById(R.id.exerciseSelectList);
                ExerciseDisplayAdapter = new ExerciseDisplayAdapter(root.getContext(), selectedExerciseList, calorieList);
                listView.setAdapter(ExerciseDisplayAdapter);
            }
        });

        docRef.collection("exerciseLogs").whereEqualTo("date", localDate)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Date logDate = documentSnapshot.getTimestamp("date").toDate();
                                if (logDate != null && isSameDate(logDate, localDate)) {
                                    double dailyCalorie = documentSnapshot.getDouble("dailyCalorie");
                                    List<Map<String, Object>> exerciseListData = (List<Map<String, Object>>) documentSnapshot.get("exerciseList");
                                    for (Map<String, Object> exerciseMap : exerciseListData) {
                                        String name = (String) exerciseMap.get("name");
                                        Exercise exercise = new Exercise(name);
                                        selectedExerciseList.add(exercise);
                                    }
                                    calorieList = (List<Double>) documentSnapshot.get("calorieList");

                                    calorieTotalExerciseTextView.setText(Double.toString(dailyCalorie));
                                    ListView listView = root.findViewById(R.id.exerciseSelectList);
                                    ExerciseDisplayAdapter = new ExerciseDisplayAdapter(root.getContext(), selectedExerciseList, calorieList);
                                    listView.setAdapter(ExerciseDisplayAdapter);
                                    break; // Exit the loop after finding the matching document
                                }
                            }
                        }
                    }
                });

        calorieTotalExerciseTextView.setText(Double.toString(dailyCalorie));

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
                displayElementExceptList(root);
                textViewExerciseSelected.setText(exerciseList.get(i).name);
                addButtonExercise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //get calorie information from api
                        List<Caloriesburned> burnedList = exerciseService.getCaloriesburned("skiing", null, estimateTimeEditText.getText().toString()).blockingFirst();
                        selectedExerciseList.add(exerciseList.get(i));
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

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Create a DatePickerDialog instance and set the listener
                DatePickerDialog datePickerDialog = new DatePickerDialog(root.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Update the dateEditText with the selected date
                                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                                dateButton.setText(selectedDate);
                                Calendar selectedCalendar = Calendar.getInstance();
                                selectedCalendar.set(year, month, dayOfMonth);
                                selectedCalendar.set(Calendar.HOUR_OF_DAY, 0);
                                selectedCalendar.set(Calendar.MINUTE, 0);
                                selectedCalendar.set(Calendar.SECOND, 0);
                                selectedCalendar.set(Calendar.MILLISECOND, 0);
                                localDate = new Timestamp(selectedCalendar.getTimeInMillis());


                                // Retrieve data from Firebase
                                docRef.collection("exerciseLogs")
                                        .whereEqualTo("date", localDate)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                if (!queryDocumentSnapshots.isEmpty()) {
                                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                        Date logDate = documentSnapshot.getTimestamp("date").toDate();
                                                        if (logDate != null) {
                                                            double dailyCalorie = documentSnapshot.getDouble("dailyCalorie");
                                                            List<Map<String, Object>> exerciseListData = (List<Map<String, Object>>) documentSnapshot.get("exerciseList");
                                                            for (Map<String, Object> foodMap : exerciseListData) {
                                                                String name = (String) foodMap.get("name");
                                                                Exercise exercise = new Exercise(name);
                                                                selectedExerciseList.add(exercise);
                                                            }
                                                            calorieList = (List<Double>) documentSnapshot.get("calorieList");

                                                            calorieTotalExerciseTextView.setText(Double.toString(dailyCalorie));
                                                            ListView listView = root.findViewById(R.id.exerciseSelectList);
                                                            ExerciseDisplayAdapter = new ExerciseDisplayAdapter(root.getContext(), selectedExerciseList, calorieList);
                                                            listView.setAdapter(ExerciseDisplayAdapter);
                                                            break; // Exit the loop after finding the matching document
                                                        }
                                                    }
                                                } else {
                                                    // No matching document found, update the UI accordingly
                                                    selectedExerciseList.clear();
                                                    calorieList.clear();
                                                    dailyCalorie = 0;
                                                    calorieTotalExerciseTextView.setText(Double.toString(dailyCalorie));
                                                    ListView listView = root.findViewById(R.id.exerciseSelectList);
                                                    listView.setAdapter(null);
                                                }
                                            }
                                        });
                            }
                        }, year, month, dayOfMonth);

                // Show the date picker dialog
                datePickerDialog.show();
            }
        });

        ListView list = root.findViewById(R.id.exerciseListView);
        list.setAdapter(null);

        submitButtonExercise.setOnClickListener(v -> submitExercises(root));

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

    private void submitExercises(View root) {
        if (localDate == null) {
            localDate = new Timestamp(System.currentTimeMillis());
        }
        dailyCalorie = 0;
        for (double d : calorieList) {
            dailyCalorie += d;
        }
        calorieTotalExerciseTextView.setText(Double.toString(dailyCalorie));
        List<ExerciseLog> exerciseLogList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(localDate.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        localDate = new Timestamp(calendar.getTimeInMillis());
        exerciseLogList.add(new ExerciseLog(dailyCalorie, calorieList, selectedExerciseList, localDate));
        db.collection("users")
        .document(FirebaseAuth.getInstance().getUid()).collection("exerciseLogs")
        .whereEqualTo("date", localDate)
        .get()
        .addOnSuccessListener(querySnapshot -> {
            // Check if a document with the given localDate already exists
            if (!querySnapshot.isEmpty()) {
                // A document with the same localDate already exists
                // You can update the existing document here if needed
                DocumentSnapshot document = querySnapshot.getDocuments().get(0);

                // Update the existing document with the new values
                document.getReference().set(
                        new ExerciseLog(dailyCalorie, calorieList, selectedExerciseList, localDate)
                );
            } else {
                db.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("exerciseLogs").add(new ExerciseLog(dailyCalorie, calorieList, selectedExerciseList, localDate));
            }    
        });
        Toast.makeText(root.getContext(), "You submitted your daily exercise successfully.", Toast.LENGTH_SHORT).show();
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
        if (item.getItemId() == android.R.id.home) {
            NavHostFragment.findNavController(this).popBackStack();
            return true;
        }
        if (item.getItemId() == R.id.share_post_button) {
            PostActivityHelper.startEditPostActivity(new Intent(getActivity(), EditPostActivity.class), getView(), getActivity());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        int year1 = cal1.get(Calendar.YEAR);
        int month1 = cal1.get(Calendar.MONTH);
        int day1 = cal1.get(Calendar.DAY_OF_MONTH);

        int year2 = cal2.get(Calendar.YEAR);
        int month2 = cal2.get(Calendar.MONTH);
        int day2 = cal2.get(Calendar.DAY_OF_MONTH);

        return (year1 == year2 && month1 == month2 && day1 == day2);
    }

    private void hideElementExceptList(View root){
        TextView t1 = root.findViewById(R.id.estimateTime);
        TextView t2 = root.findViewById(R.id.exerciseCalorieText);
        TextView t3 = root.findViewById(R.id.calorieExercise);
        TextView t4 = root.findViewById(R.id.calorieTotalExerciseText);
        TextView t5 = root.findViewById(R.id.calorieTotalExercise);
        TextView t6 = root.findViewById(R.id.textViewExerciseSelected);
        Button b2 = root.findViewById(R.id.addButtonExercise);
        Button b3 = root.findViewById(R.id.submitButtonExercise);
        ListView list = root.findViewById(R.id.exerciseListView);
        ListView list1 = root.findViewById(R.id.exerciseSelectList);
        t1.setVisibility(View.INVISIBLE);
        t2.setVisibility(View.INVISIBLE);
        t3.setVisibility(View.INVISIBLE);
        t4.setVisibility(View.INVISIBLE);
        t5.setVisibility(View.INVISIBLE);
        t6.setVisibility(View.INVISIBLE);
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
        TextView t6 = root.findViewById(R.id.textViewExerciseSelected);
        Button b2 = root.findViewById(R.id.addButtonExercise);
        Button b3 = root.findViewById(R.id.submitButtonExercise);
        ListView list = root.findViewById(R.id.exerciseListView);
        ListView list1 = root.findViewById(R.id.exerciseSelectList);
        t1.setVisibility(View.VISIBLE);
        t2.setVisibility(View.VISIBLE);
        t3.setVisibility(View.VISIBLE);
        t4.setVisibility(View.VISIBLE);
        t5.setVisibility(View.VISIBLE);
        t6.setVisibility(View.VISIBLE);
        b2.setVisibility(View.VISIBLE);
        b3.setVisibility(View.VISIBLE);
        list.setVisibility(View.INVISIBLE);
        list1.setVisibility(View.VISIBLE);
    }
}
