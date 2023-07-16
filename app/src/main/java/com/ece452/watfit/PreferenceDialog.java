package com.ece452.watfit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class PreferenceDialog extends AppCompatDialogFragment {
    private Spinner edit_calorie_spinner;
    private Spinner edit_protein_spinner;

    private Spinner edit_fat_spinner;

    private Spinner edit_carbohydrates_spinner;

    private PreferenceDialogListener preferenceDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layour_dialog, null);

        builder.setView(view)
                .setTitle("Preference Dialog")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    String calorie = "100~2000";
                    String protein = "1~120";
                    String fat = "1~120";
                    String carbohydrates = "1~120";
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!edit_calorie_spinner.getSelectedItem().toString().equalsIgnoreCase("Select a range...")){
                           String calorieLevel= edit_calorie_spinner.getSelectedItem().toString();
                           if(calorieLevel.equals("Low")){
                               calorie="480~860";
                           }else if(calorieLevel.equals("Medium")){
                               calorie="860~1620";
                           }else if(calorieLevel.equals("High")){
                               calorie="1620~2000";
                           }
                        }

                        if(!edit_protein_spinner.getSelectedItem().toString().equalsIgnoreCase("Select a range...")){
                            String proteinLevel = edit_protein_spinner.getSelectedItem().toString();
                            if(proteinLevel.equals("Low")){
                                protein="1~40";
                            }else if(proteinLevel.equals("Medium")){
                                protein="40~80";
                            }else if(proteinLevel.equals("High")){
                                protein="80~120";
                            }
                        }

                        if(!edit_fat_spinner.getSelectedItem().toString().equalsIgnoreCase("Select a range...")){
                            String fatLevel = edit_fat_spinner.getSelectedItem().toString();
                            if(fatLevel.equals("Low")){
                                fat="1~40";
                            }else if(fatLevel.equals("Medium")){
                                fat="40~80";
                            }else if(fatLevel.equals("High")){
                                fat="80~120";
                            }
                        }

                        if(!edit_carbohydrates_spinner.getSelectedItem().toString().equalsIgnoreCase("Select a range...")){
                            String carbohydratesLevel = edit_carbohydrates_spinner.getSelectedItem().toString();
                            if(carbohydratesLevel.equals("Low")){
                                carbohydrates="1~40";
                            }else if(carbohydratesLevel.equals("Medium")){
                                carbohydrates="40~80";
                            }else if(carbohydratesLevel.equals("High")){
                                carbohydrates="80~120";
                            }
                        }

                        preferenceDialogListener.applyTexts(calorie, protein, fat, carbohydrates);

                    }
                });


        edit_calorie_spinner = view.findViewById(R.id.edit_calorie_spinner);
        ArrayAdapter<String> adapter_calorie = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.calorie_list));
        adapter_calorie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_calorie_spinner.setAdapter(adapter_calorie);

        edit_protein_spinner = view.findViewById(R.id.edit_protein_spinner);
        ArrayAdapter<String> adapter_protein = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.protein_list));
        adapter_protein.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_protein_spinner.setAdapter(adapter_protein);

        edit_fat_spinner = view.findViewById(R.id.edit_fat_spinner);
        ArrayAdapter<String> adapter_fat = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.fat_list));
        adapter_fat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_fat_spinner.setAdapter(adapter_fat);



        edit_carbohydrates_spinner = view.findViewById(R.id.edit_carbohydrates_spinner);
        ArrayAdapter<String> adapter_carbohydrates = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.carbohydrates_list));
        adapter_carbohydrates.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_carbohydrates_spinner.setAdapter(adapter_carbohydrates);


        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            preferenceDialogListener = (PreferenceDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+" must implement PreferenceDialogListener");
        }
    }

    public interface PreferenceDialogListener{
        void applyTexts(String calorie, String carbohydrates, String fat, String protein);
    }
}
