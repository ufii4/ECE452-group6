package com.ece452.watfit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import com.ece452.watfit.R;
import com.ece452.watfit.data.Exercise;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ExerciseDisplayAdapter extends ArrayAdapter<Exercise> {
    private Context context;
    private List<Exercise> itemList;
    private List<Exercise> filteredItemList;
    private List<Double> calorie;
    private Double dailyCalorie;
    private TextView calorieTotalExerciseTextView;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public ExerciseDisplayAdapter(Context context, List<Exercise> itemList, List<Double> calorie,Double dailyCalorie,TextView calorieTotalExerciseTextView) {
        super(context, R.layout.exercise_listview, itemList);
        this.context = context;
        this.itemList = itemList;
        this.filteredItemList = itemList;
        this.calorie = calorie;
        this.dailyCalorie = dailyCalorie;
        this.calorieTotalExerciseTextView = calorieTotalExerciseTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.exercise_selected_listview, null);
        }

        Exercise item = filteredItemList.get(position);

        TextView titleTextView = convertView.findViewById(R.id.exerciseDisplayList);
        TextView descriptionTextView = convertView.findViewById(R.id.exerciseCalorie);

        titleTextView.setText(item.name);
        descriptionTextView.setText(Double.toString(this.calorie.get(position))+"kcal");


        Button btnDelete = convertView.findViewById(R.id.btnDeleteExercise);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete button click here
                // You can access the clicked item position using the 'position' parameter
                // For example:
                dailyCalorie-=calorie.get(position);
                calorieTotalExerciseTextView.setText(Double.toString(dailyCalorie)+"kcal");
                itemList.remove(position);
                calorie.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Delete Successfully but remember to submit!", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return filteredItemList.size();
    }

    @Override
    public Exercise getItem(int position) {
        return filteredItemList.get(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                ArrayList<Exercise> tempList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    tempList.addAll(itemList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Exercise item : itemList) {
                        if (item.name.toLowerCase().contains(filterPattern)) {
                            tempList.add(item);
                        }
                    }
                }

                filterResults.values = tempList;
                filterResults.count = tempList.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredItemList = (List<Exercise>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void filter(String query) {
        getFilter().filter(query);
    }
}
