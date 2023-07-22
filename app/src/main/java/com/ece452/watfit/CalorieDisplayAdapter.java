package com.ece452.watfit;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ece452.watfit.R;
import com.ece452.watfit.data.Ingredient;
import com.ece452.watfit.data.MenuItem;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CalorieDisplayAdapter extends ArrayAdapter<Ingredient> {
    private Context context;
    private List<Ingredient> itemList;
    private List<Ingredient> filteredItemList;
    private List<Double> calorie;
    private TextView calorieTotal;
    private Double dailyCalorieDisplay;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    String unit;
    private OnDeleteButtonClickListener onDeleteButtonClickListener;

    public CalorieDisplayAdapter(Context context, List<Ingredient> itemList,List<Double> calorie, String unit,Double dailyCalorieDisplay,TextView calorieTotal) {
        super(context, R.layout.calorie_listview, itemList);
        this.context = context;
        this.itemList = itemList;
        this.filteredItemList = itemList;
        this.calorie = calorie;
        this.unit = unit;
        this.dailyCalorieDisplay = dailyCalorieDisplay;
        this.calorieTotal = calorieTotal;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.food_selected_listview, null);
        }

        Ingredient item = filteredItemList.get(position);

        ImageView imageView = convertView.findViewById(R.id.imageViewDisplayIngredient);
        TextView titleTextView = convertView.findViewById(R.id.ingredientDisplayList);
        TextView descriptionTextView = convertView.findViewById(R.id.ingredientCalorie);

        Picasso.get().load("https://spoonacular.com/cdn/ingredients_100x100/"+item.image).into(imageView);
        titleTextView.setText(item.name);
        descriptionTextView.setText(Double.toString(this.calorie.get(position)) + " " +this.unit);

        //deleteButton listener
        Button btnDelete = convertView.findViewById(R.id.btnDeleteFood);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete button click here
                dailyCalorieDisplay -= calorie.get(position);
                calorieTotal.setText(df.format(dailyCalorieDisplay)+"kcal");
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
    public Ingredient getItem(int position) {
        return filteredItemList.get(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                ArrayList<Ingredient> tempList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    tempList.addAll(itemList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Ingredient item : itemList) {
//                        if (item.getTitle().toLowerCase().contains(filterPattern)) {
//                            tempList.add(item);
//                        }
                    }
                }

                filterResults.values = tempList;
                filterResults.count = tempList.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredItemList = (List<Ingredient>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    public void filter(String query) {
        getFilter().filter(query);
    }

    // Setter for the delete button click listener
    public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener listener) {
        this.onDeleteButtonClickListener = listener;
    }

    // Interface for handling delete button click
    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClick(int position);
    }
}
