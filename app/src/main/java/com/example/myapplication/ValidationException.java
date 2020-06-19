package com.example.myapplication;

import android.widget.EditText;

public class ValidationException extends Exception {

    private EditText field;

    public ValidationException(String message, EditText field) {
        super(message);
        this.field = field;
    }

    public EditText getField() {
        return field;
    }
}
