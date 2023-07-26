package com.ece452.watfit.ui.tracker;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.ece452.watfit.R;
import com.ece452.watfit.data.models.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExerciseSearchAdapter extends ArrayAdapter<Exercise> {
    private Context context;
    private List<Exercise> exerciseList;
    private List<Exercise> filteredExerciseList;

    public ExerciseSearchAdapter(Context context, List<Exercise> exerciseList) {
        super(context, R.layout.exercise_listview, exerciseList);
        this.context = context;
        this.exerciseList = exerciseList;
        this.filteredExerciseList = exerciseList;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.exercise_listview, null);
        }

        Exercise item = filteredExerciseList.get(position);

        TextView titleTextView = convertView.findViewById(R.id.exerciseSearchList);

        titleTextView.setText(item.name);

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<String> suggestions = new ArrayList<>();

                if (constraint != null) {
                    for (Exercise exercise : exerciseList) {
                        if (exercise.name.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            suggestions.add(exercise.name);
                        }
                    }
                }

                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                if (results != null && results.count > 0) {
                    addAll((List<Exercise>) results.values);
                } else {
                    addAll(exerciseList);
                }
                notifyDataSetChanged();
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return resultValue == null ? "" : resultValue.toString();
            }
        };
    }
}
