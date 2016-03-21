package com.example.alecksjohanssen.newyorktimes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Calendar;

public class ChooseDialog extends DialogFragment {

    public interface SettingsListener {
        public void onSettingsSelected(String selection);
    }
    String selection1;
    final String[] items = {"Newest", "Oldest"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @NonNull

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Choose").setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                selection1 = (String) items[arg1];
                Log.d("DEBUG", "User selected: " + selection1);
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Pass selection back to parent.
                SettingsListener settingsActivity = (SettingsListener) getActivity();
                settingsActivity.onSettingsSelected(selection1);
                dialog.dismiss();
            }

        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return alertDialogBuilder.create();
    }
}




