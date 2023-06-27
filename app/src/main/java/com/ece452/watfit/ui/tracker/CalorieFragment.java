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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.navigation.fragment.NavHostFragment;

import com.ece452.watfit.AccountActivity;
import com.ece452.watfit.CalorieDisplayAdapter;
import com.ece452.watfit.CalorieSearchAdapter;
import com.ece452.watfit.EditPostActivity;
import com.ece452.watfit.R;
import com.ece452.watfit.data.Ingredient;
import com.ece452.watfit.data.Nutrition;
import com.ece452.watfit.data.Recipe;
import com.ece452.watfit.data.source.remote.IngredientService;
import com.ece452.watfit.data.source.remote.SpoonacularDataSource;
import com.ece452.watfit.databinding.FragmentCalorieIntakeBinding;
import com.ece452.watfit.ui.dashboard.DashboardFragment;
import com.ece452.watfit.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import io.reactivex.rxjava3.subscribers.TestSubscriber;

public class CalorieFragment extends Fragment {
    private FragmentCalorieIntakeBinding binding;
    private String[] ingredientList;
    private List<Ingredient> ingredientList1;
    private String[] ingredientImageList;
    private int[] ingredientIDList;
    private String selectedUnit;
    private double dailyCalorie = 0;
    private List<Double> calorieList = new ArrayList<>();
    private List<Ingredient>  foodList = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCalorieIntakeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        setHasOptionsMenu(true);

        //Search ingredients
        SearchView searchView = root.findViewById(R.id.searchViewCalorie);
        TextView calorieTotal = root.findViewById(R.id.calorieTotal);
        calorieTotal.setText(Double.toString(dailyCalorie));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search action here
                performSearch(query);
                //add search result to the page
                ListView list = root.findViewById(R.id.listview);
//                ArrayAdapter adapterSearch=new ArrayAdapter<String>(root.getContext(), R.layout.calorie_listview,ingredientList);
//                ArrayAdapter adapter=new ArrayAdapter<String>(root.getContext(), R.layout.calorie_listview,ingredientList1);
                CalorieSearchAdapter adapterSearch = new CalorieSearchAdapter(root.getContext(), ingredientList1);
                list.setAdapter(adapterSearch);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    displayElementExceptList(root);
//                    ingredientIDList = null;
//                    ingredientList = null;
                } else {
                    hideElementExceptList(root);
                }

                return false;
            }
        });
//display on the page
        ListView ingredientSelectList = root.findViewById(R.id.listview);
        ListView ingredientDisplayList = root.findViewById(R.id.foodSelectList);
//        TextView ingredientDisplayList = root.findViewById(R.id.foodList);
        ingredientSelectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ingredientSelectList.setAdapter(null);
                displayElementExceptList(root);
                //display selected ingredient pic on the page
                ImageView ingredientSelected = root.findViewById(R.id.imageViewCalorieSelected);
                Picasso.get().load("https://spoonacular.com/cdn/ingredients_100x100/"+ingredientList1.get(i).image).into(ingredientSelected);
                //search for calorie information and get possible units
                List<String> possibleUnits = ingredientService.getIngredientInformationBasic(ingredientIDList[i]).blockingFirst().possibleUnits;
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
                        //get calorie information from api
                        foodList.add(ingredientList1.get(i));
                        int amount = Integer.parseInt(amountText.getText().toString());
                        Nutrition.Nutrient[] nutrientList = ingredientService.getIngredientInformation(ingredientIDList[i],amount,selectedUnit).blockingFirst().nutrition.nutrients;
                        for (Nutrition.Nutrient n: nutrientList) {
                            if(n.name.equals("Calories")){
                                TextView calorieInput = root.findViewById(R.id.calorieInput);
                                calorieList.add(n.amount);
                                calorieInput.setText(Double.toString(n.amount));
                                //display on the page
                                CalorieDisplayAdapter adapter = new CalorieDisplayAdapter(root.getContext(),foodList,calorieList,n.unit);
                                ListView listView = root.findViewById(R.id.foodSelectList);
                                listView.setAdapter(adapter);
                                dailyCalorie += n.amount;
                                TextView calorieTotal = root.findViewById(R.id.calorieTotal);
                                calorieTotal.setText(Double.toString(dailyCalorie));
                            }
                        }
                    }
                });
                amountText.setText("");
                searchView.setQuery("",false);
            }
        });
        ListView list = root.findViewById(R.id.listview);
        //unbound listview from adapter to hide
        list.setAdapter(null);
        Spinner spinnerCalorie = root.findViewById(R.id.spinnerCalorie);
        spinnerCalorie.setAdapter(null);
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
            // handle account button click
            // TODO: take a screenshot on the current Calorie fragment before navigate to EditPostActivity
            startActivity(new Intent(getActivity(), EditPostActivity.class));
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
            ingredientList = new String[list.size()];
            ingredientImageList = new String[list.size()];
            ingredientIDList = new int[list.size()];
            ingredientList1 = new ArrayList<>(list.size());
            for (int i = 0; i < list.size(); i++) {
                ingredientList[i] = list.get(i).name;
                ingredientIDList[i] = list.get(i).id;
                ingredientImageList[i] = list.get(i).image;
                ingredientList1.add(list.get(i));
            }
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

}
