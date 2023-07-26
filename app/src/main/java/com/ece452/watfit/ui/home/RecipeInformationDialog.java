package com.ece452.watfit.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.ece452.watfit.R;

public class RecipeInformationDialog extends AppCompatDialogFragment {

    String recipeInfo;
    String recipeTitle;
    RecipeInformationDialog (String recipeInfo, String recipeTitle){
        this.recipeTitle = recipeTitle;
        this.recipeInfo = recipeInfo;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.recipe_information_dialog, null);

        TextView recipeTitleText = view.findViewById(R.id.recipe_title);
        recipeTitleText.setText(recipeTitle);
        TextView recipeInfoText = view.findViewById(R.id.recipe_information_content);
        recipeInfoText.setText(recipeInfo);
//        recipeInfoText.setTypeface(null, Typeface.BOLD_ITALIC);


        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

}

