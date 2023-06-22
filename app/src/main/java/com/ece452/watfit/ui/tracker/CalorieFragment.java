package com.ece452.watfit.ui.tracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.navigation.fragment.NavHostFragment;

import com.ece452.watfit.R;
import com.ece452.watfit.data.Ingredient;
import com.ece452.watfit.data.Recipe;
import com.ece452.watfit.data.source.remote.IngredientService;
import com.ece452.watfit.data.source.remote.Result;
import com.ece452.watfit.data.source.remote.SpoonacularDataSource;
import com.ece452.watfit.databinding.FragmentCalorieIntakeBinding;
import com.ece452.watfit.ui.dashboard.DashboardFragment;
import com.ece452.watfit.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    String[] ingredientList;
    ArrayList<String> foodList = new ArrayList<>();
    String[] ingredientList1 = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};;
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search action here
                performSearch(query);
                //add search result to the page
                ListView list = root.findViewById(R.id.listview);
                ArrayAdapter adapterSearch=new ArrayAdapter<String>(root.getContext(), R.layout.calorie_listview,ingredientList);
//                ArrayAdapter adapter=new ArrayAdapter<String>(root.getContext(), R.layout.calorie_listview,ingredientList1);
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
//display on the page
        ListView ingredientSelectList = root.findViewById(R.id.listview);
        ListView ingredientDisplayList = root.findViewById(R.id.foodSelectList);
//        TextView ingredientDisplayList = root.findViewById(R.id.foodList);
        ingredientSelectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                ingredientDisplayList.setText(ingredientList[i]);
                foodList.add(ingredientList[i]);
                ingredientList = null;
                ingredientSelectList.setAdapter(null);
//                foodList.add(ingredientList1[i]);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(root.getContext(), R.layout.food_selected_listview, foodList);
                ListView listView = root.findViewById(R.id.foodSelectList);
                listView.setAdapter(adapter);
                displayElementExceptList(root);
                searchView.setQuery("",false);
            }
        });
        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavHostFragment.findNavController(this).navigate(R.id.navigation_tracker);
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
//            ingredientService.searchIngredient(query).subscribe(new DisposableSubscriber<Result<Ingredient>>() {
//                @Override
//                public void onNext(Result<Ingredient> ingredientResult) {
//                    for (int i = 0; i < ingredientResult.results.size(); i++) {
//                        System.out.println("result:"+ingredientResult.results.get(i).name);
////                        ingredientList[i] = ingredientResult.results.get(i).name;
////                        System.out.println("result"+ingredientList[i]);
//
//                    }
//
//                    System.out.println(ingredientResult.results.size());
//                }
//
//                @Override
//                public void onError(Throwable t) {
//
//                }
//
//                @Override
//                public void onComplete() {
//                }
//            });
        Result<Ingredient> list = ingredientService.searchIngredient(query).blockingFirst();
        ingredientList = new String[list.results.size()];
        for (int i = 0; i < list.results.size(); i++) {
//            System.out.println(list.results.get(i).name);
            ingredientList[i] = list.results.get(i).name;
        }
        }

    private void hideElementExceptList(View root){
        TextView t1 = root.findViewById(R.id.calorieHint);
        TextView t2 = root.findViewById(R.id.calorieInput);
        TextView t3 = root.findViewById(R.id.estimateCalorie);
        TextView t4 = root.findViewById(R.id.calorieTotalText);
        TextView t5 = root.findViewById(R.id.calorieTotal);
        Button b1 = root.findViewById(R.id.backButton);
        Button b2 = root.findViewById(R.id.addButton);
        Button b3 = root.findViewById(R.id.submitButton);
        ListView list = root.findViewById(R.id.listview);
        ListView list1 = root.findViewById(R.id.foodSelectList);
        t1.setVisibility(View.INVISIBLE);
        t2.setVisibility(View.INVISIBLE);
        t3.setVisibility(View.INVISIBLE);
        t4.setVisibility(View.INVISIBLE);
        t5.setVisibility(View.INVISIBLE);
        b1.setVisibility(View.INVISIBLE);
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
        Button b1 = root.findViewById(R.id.backButton);
        Button b2 = root.findViewById(R.id.addButton);
        Button b3 = root.findViewById(R.id.submitButton);
        ListView list = root.findViewById(R.id.listview);
        ListView list1 = root.findViewById(R.id.foodSelectList);
        t1.setVisibility(View.VISIBLE);
        t2.setVisibility(View.VISIBLE);
        t3.setVisibility(View.VISIBLE);
        t4.setVisibility(View.VISIBLE);
        t5.setVisibility(View.VISIBLE);
        b1.setVisibility(View.VISIBLE);
        b2.setVisibility(View.VISIBLE);
        b3.setVisibility(View.VISIBLE);
        list.setVisibility(View.INVISIBLE);
        list1.setVisibility(View.VISIBLE);
    }
    }
}
