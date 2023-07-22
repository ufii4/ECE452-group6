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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.ece452.watfit.CalorieDisplayAdapter;
import com.ece452.watfit.CalorieSearchAdapter;
import com.ece452.watfit.EditPostActivity;
import com.ece452.watfit.R;
import com.ece452.watfit.data.CalorieLog;
import com.ece452.watfit.data.Ingredient;
import com.ece452.watfit.data.Nutrition;
import com.ece452.watfit.data.source.remote.IngredientService;
import com.ece452.watfit.data.source.remote.SpoonacularDataSource;
import com.ece452.watfit.databinding.FragmentCalorieIntakeBinding;
import com.ece452.watfit.ui.post.PostActivityHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Date;

import javax.inject.Inject;

public class CalorieFragment extends Fragment {
    private FragmentCalorieIntakeBinding binding;
    private List<Ingredient> ingredientList;
    private String[] ingredientImageList;
    private List<Integer> ingredientIDList;
    private String selectedUnit;
    private double dailyCalorie = 0;
//    private double dailyCalorieDisplay = 0;
    private Timestamp localDate = null;
    private List<Double> calorieList = new ArrayList<>();
    private List<Ingredient> foodList = new ArrayList<>();
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private CalorieDisplayAdapter calorieAdapter;

    @Inject
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCalorieIntakeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getUid());
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        setHasOptionsMenu(true);

        // Search ingredients
        SearchView searchView = root.findViewById(R.id.searchViewCalorie);
        TextView calorieTotal = root.findViewById(R.id.calorieTotal);
        calorieTotal.setText(Double.toString(dailyCalorie)+"kcal");
        Button submitButton = root.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(v -> submit());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search action here
                performSearch(query);
                // Add search result to the page
                ListView list = root.findViewById(R.id.listview);
                CalorieSearchAdapter adapterSearch = new CalorieSearchAdapter(root.getContext(), ingredientList);
                list.setAdapter(adapterSearch);
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

        // Display on the page
        ListView ingredientSelectList = root.findViewById(R.id.listview);
        ingredientSelectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ingredientSelectList.setAdapter(null);
                displayElementExceptList(root);
                // Display selected ingredient pic on the page
                ImageView ingredientSelected = root.findViewById(R.id.imageViewCalorieSelected);
                Picasso.get().load("https://spoonacular.com/cdn/ingredients_100x100/" + ingredientList.get(i).image).into(ingredientSelected);
                // Search for calorie information and get possible units
                List<String> possibleUnits = ingredientService.getIngredientInformationBasic(ingredientIDList.get(i)).blockingFirst().possibleUnits;
                Spinner spinnerCalorie = root.findViewById(R.id.spinnerCalorie);
                ArrayAdapter<String> adapterDropDown = new ArrayAdapter<>(root.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, possibleUnits);
                spinnerCalorie.setAdapter(adapterDropDown);
                spinnerCalorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedUnit = adapterView.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                Button addButton = root.findViewById(R.id.addButton);
                TextView amountText = root.findViewById(R.id.estimateCalorie);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Get calorie information from API
                        foodList.add(ingredientList.get(i));
                        int amount = Integer.parseInt(amountText.getText().toString());
                        Nutrition.Nutrient[] nutrientList = ingredientService.getIngredientInformation(ingredientIDList.get(i), amount, selectedUnit).blockingFirst().nutrition.nutrients;
                        for (Nutrition.Nutrient n : nutrientList) {
                            if (n.name.equals("Calories")) {
                                TextView calorieInput = root.findViewById(R.id.calorieInput);
                                calorieList.add(n.amount);
                                //update total calorie
//                                dailyCalorieDisplay+=n.amount;
                                TextView calorieTotal = root.findViewById(R.id.calorieTotal);
                                String calorieText = calorieTotal.getText().toString();
                                dailyCalorie = Double.parseDouble(calorieText.substring(0,calorieText.length()-4));
                                dailyCalorie+=n.amount;
                                calorieTotal.setText(df.format(dailyCalorie)+"kcal");
                                calorieInput.setText(df.format(n.amount)+"kcal");

                                // Display on the page
                                calorieAdapter = new CalorieDisplayAdapter(root.getContext(), foodList, calorieList, n.unit,dailyCalorie,calorieTotal);
                                ListView listView = root.findViewById(R.id.foodSelectList);
                                listView.setAdapter(calorieAdapter);
                            }
                        }
                    }
                });
                amountText.setText("");
                searchView.setQuery("", false);
            }
        });

        // Initialize date
        Button dateButton = root.findViewById(R.id.dateButtonCalorie);
        localDate = new Timestamp(System.currentTimeMillis());
        String selectedDate = new SimpleDateFormat("dd/MM/yyyy").format(localDate);
        dateButton.setText(selectedDate);

        // Get today's log from database
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(localDate.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        localDate = new Timestamp(calendar.getTimeInMillis());

        docRef.collection("calorieLogs").whereEqualTo("date", localDate)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Date logDate = documentSnapshot.getTimestamp("date").toDate();
                                if (logDate != null && isSameDate(logDate, localDate)) {
                                    double dailyCalorie_fire = documentSnapshot.getDouble("dailyCalorie");
                                    dailyCalorie = dailyCalorie_fire;
                                    List<Map<String, Object>> foodListData = (List<Map<String, Object>>) documentSnapshot.get("foodList");
                                    foodList = new ArrayList<>();
                                    for (Map<String, Object> foodMap : foodListData) {
                                        String name = (String) foodMap.get("name");
                                        int id = ((Long) foodMap.get("id")).intValue();
                                        String image = (String) foodMap.get("image");
                                        Ingredient ingredient = new Ingredient(name, id, image);
                                        foodList.add(ingredient);
                                    }
                                    calorieList = (List<Double>) documentSnapshot.get("calorieList");

                                    TextView calorieTotal = root.findViewById(R.id.calorieTotal);
                                    calorieTotal.setText(df.format(dailyCalorie)+"kcal");
                                    ListView listView = root.findViewById(R.id.foodSelectList);
                                    calorieAdapter = new CalorieDisplayAdapter(root.getContext(), foodList, calorieList, "kcal",dailyCalorie,calorieTotal);
                                    listView.setAdapter(calorieAdapter);
                                    break; // Exit the loop after finding the matching document
                                }
                            }
                        }
                    }
                });


        // Modify date
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
                                docRef.collection("calorieLogs")
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
                                                            dailyCalorie = dailyCalorie;
                                                            List<Map<String, Object>> foodListData = (List<Map<String, Object>>) documentSnapshot.get("foodList");
                                                            foodList = new ArrayList<>();
                                                            for (Map<String, Object> foodMap : foodListData) {
                                                                String name = (String) foodMap.get("name");
                                                                int id = ((Long) foodMap.get("id")).intValue();
                                                                String image = (String) foodMap.get("image");
                                                                Ingredient ingredient = new Ingredient(name, id, image);
                                                                foodList.add(ingredient);
                                                            }
                                                            calorieList = (List<Double>) documentSnapshot.get("calorieList");

                                                            TextView calorieTotal = root.findViewById(R.id.calorieTotal);
                                                            calorieTotal.setText(df.format(dailyCalorie)+"kcal");
                                                            ListView listView = root.findViewById(R.id.foodSelectList);
                                                            calorieAdapter = new CalorieDisplayAdapter(root.getContext(), foodList, calorieList, "kcal",dailyCalorie,calorieTotal);
                                                            listView.setAdapter(calorieAdapter);
                                                            break; // Exit the loop after finding the matching document
                                                        }
                                                    }
                                                } else {
                                                    // No matching document found, update the UI accordingly
                                                    foodList.clear();
                                                    calorieList.clear();
                                                    dailyCalorie = 0;
                                                    TextView calorieTotal = root.findViewById(R.id.calorieTotal);
                                                    calorieTotal.setText(df.format(dailyCalorie)+"kcal");
                                                    ListView listView = root.findViewById(R.id.foodSelectList);
                                                    listView.setAdapter(null);
                                                    CalorieDisplayAdapter adapter = new CalorieDisplayAdapter(root.getContext(), foodList, calorieList, "kcal",dailyCalorie,calorieTotal);
                                                    listView.setAdapter(adapter);
                                                }
                                            }
                                        });
                            }
                        }, year, month, dayOfMonth);

                // Show the date picker dialog
                datePickerDialog.show();
            }
        });

        ListView list = root.findViewById(R.id.listview);
        list.setAdapter(null);
        Spinner spinnerCalorie = root.findViewById(R.id.spinnerCalorie);
        spinnerCalorie.setAdapter(null);

        // Submit button
        Button submit_bt = root.findViewById(R.id.submitButton);
        submit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (localDate == null) {
                    localDate = new Timestamp(System.currentTimeMillis());
                }
                dailyCalorie = 0;
                for (double d : calorieList) {
                    dailyCalorie += d;
                }
                TextView calorieTotal = root.findViewById(R.id.calorieTotal);
                calorieTotal.setText(df.format(dailyCalorie)+"kcal");
                List<CalorieLog> calorieLogList = new ArrayList<>();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(localDate.getTime());
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                localDate = new Timestamp(calendar.getTimeInMillis());
                calorieLogList.add(new CalorieLog(dailyCalorie, calorieList, foodList, localDate));
                db.collection("users")
                .document(FirebaseAuth.getInstance().getUid()).collection("calorieLogs")
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
                                new CalorieLog(dailyCalorie, calorieList, foodList, localDate)
                        );
                    } else {
                        db.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("calorieLogs").add(new CalorieLog(dailyCalorie, calorieList, foodList, localDate));
                    }    
                });
                Toast.makeText(root.getContext(), "You submitted your daily intake successfully.", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
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
            NavHostFragment.findNavController(this).navigate(R.id.navigation_tracker);
            return true;
        }
        // share button is clicked
        if (item.getItemId() == R.id.share_post_button) {
            PostActivityHelper.startEditPostActivity(new Intent(getActivity(), EditPostActivity.class), getView(), getActivity());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private SpoonacularDataSource spoonacularDataSource;
    private IngredientService ingredientService;
    private void performSearch(String query) {
        // Perform your search logic here
        // You can use the query parameter to search for matching data or filter a list
        // For example, you can update a RecyclerView adapter with the filtered results
            spoonacularDataSource = new SpoonacularDataSource();
            ingredientService = spoonacularDataSource.ingredientService;

        List<Ingredient> list = ingredientService.searchIngredient(query).blockingFirst().results;
            ingredientImageList = new String[list.size()];
            ingredientIDList = new ArrayList<>(list.size());
            ingredientList = new ArrayList<>(list.size());
            for (int i = 0; i < list.size(); i++) {
                ingredientIDList.add(list.get(i).id);
                ingredientImageList[i] = list.get(i).image;
                ingredientList.add(list.get(i));
            }
    }

    private void submit() {
        NavHostFragment.findNavController(this).popBackStack();
    }

    private void hideElementExceptList(View root){
        TextView t1 = root.findViewById(R.id.calorieHint);
        TextView t2 = root.findViewById(R.id.calorieInput);
        TextView t3 = root.findViewById(R.id.estimateCalorie);
        TextView t4 = root.findViewById(R.id.calorieTotalText);
        TextView t5 = root.findViewById(R.id.calorieTotal);
        Button b2 = root.findViewById(R.id.addButton);
        Button b3 = root.findViewById(R.id.submitButton);
        ImageView ingredientSelected = root.findViewById(R.id.imageViewCalorieSelected);
        Spinner spinnerCalorie = root.findViewById(R.id.spinnerCalorie);
        ingredientSelected.setVisibility(View.INVISIBLE);
        spinnerCalorie.setVisibility(View.INVISIBLE);
        ListView list = root.findViewById(R.id.listview);
        ListView list1 = root.findViewById(R.id.foodSelectList);
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
        TextView t1 = root.findViewById(R.id.calorieHint);
        TextView t2 = root.findViewById(R.id.calorieInput);
        TextView t3 = root.findViewById(R.id.estimateCalorie);
        TextView t4 = root.findViewById(R.id.calorieTotalText);
        TextView t5 = root.findViewById(R.id.calorieTotal);
        Button b2 = root.findViewById(R.id.addButton);
        Button b3 = root.findViewById(R.id.submitButton);
        ListView list = root.findViewById(R.id.listview);
        ListView list1 = root.findViewById(R.id.foodSelectList);
        ImageView ingredientSelected = root.findViewById(R.id.imageViewCalorieSelected);
        Spinner spinnerCalorie = root.findViewById(R.id.spinnerCalorie);
        ingredientSelected.setVisibility(View.VISIBLE);
        spinnerCalorie.setVisibility(View.VISIBLE);
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
}
