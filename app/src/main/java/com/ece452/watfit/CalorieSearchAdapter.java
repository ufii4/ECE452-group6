package com.ece452.watfit;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ece452.watfit.R;
import com.ece452.watfit.data.Ingredient;
import com.ece452.watfit.data.MenuItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CalorieSearchAdapter extends ArrayAdapter<Ingredient> {
    private Context context;
    private List<Ingredient> itemList;
    private List<Ingredient> filteredItemList;

    public CalorieSearchAdapter(Context context, List<Ingredient> itemList) {
        super(context, R.layout.calorie_listview, itemList);
        this.context = context;
        this.itemList = itemList;
        this.filteredItemList = itemList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.calorie_listview, null);
        }

        Ingredient item = filteredItemList.get(position);

        ImageView imageView = convertView.findViewById(R.id.imageViewCalorie);
        TextView titleTextView = convertView.findViewById(R.id.ingredientSearchList);
//        TextView descriptionTextView = convertView.findViewById(R.id.ingredientCalorie);

        Picasso.get().load("https://spoonacular.com/cdn/ingredients_250x250/"+item.image).into(imageView);
        titleTextView.setText(item.name);

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
}
