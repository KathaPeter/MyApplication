package com.example.myapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Alert_WelcomePage extends AppCompatDialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            String patientName = getActivity().getIntent().getStringExtra("patient_name");
            String patientVorname = getActivity().getIntent().getStringExtra("patient_vorname");
            String message = String.format("Herzlich Willkommen %s %s,\n\nwähle Dein gewünschtes Ziel.", patientVorname, patientName);
            builder.setMessage(message)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

            return builder.create();
        }
    }
