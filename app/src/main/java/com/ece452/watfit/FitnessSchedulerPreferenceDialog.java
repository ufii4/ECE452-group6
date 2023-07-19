package com.ece452.watfit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class FitnessSchedulerPreferenceDialog extends AppCompatDialogFragment {
    private Spinner edit_exercise_type_spinner;
    private Spinner edit_intensity_spinner;
    private FitnessSchedulerPreferenceDialogListener fitnessSchedulerPreferenceDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fitness_schedule_preference_layout_dialog, null);

        builder.setView(view)
                .setTitle("Fitness Scheduler Preference Dialog")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    String exercise_type = "indoor";
                    String intensity = "low";
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(!edit_exercise_type_spinner.getSelectedItem().toString().equalsIgnoreCase("Select an exercise type")){
                            exercise_type = edit_exercise_type_spinner.getSelectedItem().toString();
                        }

                        if(!edit_intensity_spinner.getSelectedItem().toString().equalsIgnoreCase("Select an intensity level")){
                            intensity = edit_intensity_spinner.getSelectedItem().toString();
                        }

                        fitnessSchedulerPreferenceDialogListener.applyTexts(exercise_type,intensity);
                    }
                });

        edit_exercise_type_spinner = view.findViewById(R.id.edit_exercise_type);
        ArrayAdapter<String> adapter_exercise_type = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.exercise_type_list));
        adapter_exercise_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_exercise_type_spinner.setAdapter(adapter_exercise_type);

        edit_intensity_spinner = view.findViewById(R.id.edit_intensity);
        ArrayAdapter<String> adapter_intensity = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.intensity_list));
        adapter_intensity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_intensity_spinner.setAdapter(adapter_intensity);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            fitnessSchedulerPreferenceDialogListener = (FitnessSchedulerPreferenceDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+" must implement FitnessSchedulerPreferenceDialogListener");
        }
    }

    public interface FitnessSchedulerPreferenceDialogListener{
        void applyTexts(String exercise_type, String intensity);
    }
}
