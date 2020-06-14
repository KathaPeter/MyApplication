package com.example.myapplication;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentActivity;


public class DialogYesNo extends AppCompatDialogFragment {

    private final String question;
    private final IEvent handlerYes;
    private final IEvent handlerNo;

    public DialogYesNo(FragmentActivity parent, String question, IEvent handlerYes, @Nullable IEvent handlerNo) {
        this.question = question;
        this.handlerYes = handlerYes;
        this.handlerNo = handlerNo == null ? () -> { } : handlerNo;

        show(parent.getSupportFragmentManager(), null);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(question) //
                .setPositiveButton("Ja", (a, b) -> handlerYes.execute())
                .setNegativeButton("Nein", (a, b) -> handlerNo.execute());

        return builder.create();
    }


}