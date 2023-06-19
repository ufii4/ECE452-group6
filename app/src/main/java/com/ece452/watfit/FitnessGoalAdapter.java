package com.ece452.watfit;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ece452.watfit.data.FitnessGoal;

import java.util.List;

public class FitnessGoalAdapter extends RecyclerView.Adapter<FitnessGoalAdapter.FitnessGoalViewHolder> {
    private List<FitnessGoal> fitnessGoals;
    private ArrayAdapter adapter;

    public FitnessGoalAdapter(List<FitnessGoal> fitnessGoals) {
        this.fitnessGoals = fitnessGoals;
    }

    @NonNull
    @Override
    public FitnessGoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_fitness_goal, parent, false);
        if (adapter == null) {
            adapter = new ArrayAdapter(parent.getContext(), android.R.layout.simple_spinner_item, FitnessGoal.Type.values());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }
        return new FitnessGoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FitnessGoalViewHolder holder, int position) {
        FitnessGoal fitnessGoal = fitnessGoals.get(position);
        holder.goalType.setAdapter(adapter);
        holder.goalType.setSelection(fitnessGoal.getType().ordinal());
        if (fitnessGoal.getUnit() != null) {
            holder.goalValue.setHint(fitnessGoal.getUnit());
        }
        Integer goalValue = fitnessGoal.getValue();
        if (goalValue != null) {
            holder.goalValue.setText(String.valueOf(goalValue));
        } else {
            holder.goalValue.setText("");
        }

        holder.goalValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value = s.toString();
                if (value.isEmpty()) {
                    fitnessGoal.setValue(null);
                } else {
                    fitnessGoal.setValue(Integer.parseInt(value));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.goalType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fitnessGoal.setType((FitnessGoal.Type) parent.getItemAtPosition(position));
                holder.goalValue.setHint(fitnessGoal.getUnit());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.bt_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.goalValue.hasFocus()) {
                    holder.goalValue.clearFocus();
                }
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    fitnessGoals.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                }
            }
        });
//        holder.goalUnit.setText(fitnessGoal.getUnit());
//        holder.goalStartDate.setText(fitnessGoal.getStartDate().toString());
//        holder.goalEndDate.setText(fitnessGoal.getEndDate().toString());
    }

    @Override
    public int getItemCount() {
        return fitnessGoals.size();
    }

    public class FitnessGoalViewHolder extends RecyclerView.ViewHolder {
        public Spinner goalType;
        public EditText goalValue;
        public ImageButton bt_remove;
//        TextView goalUnit;
//        TextView goalStartDate;
//        TextView goalEndDate;

        public FitnessGoalViewHolder(@NonNull View itemView) {
            super(itemView);
            goalType = itemView.findViewById(R.id.sp_goal_cfg);
            goalValue = itemView.findViewById(R.id.et_goal_value_cfg);
            bt_remove = itemView.findViewById(R.id.ibt_remove_cfg);
//            goalUnit = itemView.findViewById(R.id.goal_unit);
//            goalStartDate = itemView.findViewById(R.id.goal_start_date);
//            goalEndDate = itemView.findViewById(R.id.goal_end_date);
        }
    }
}
