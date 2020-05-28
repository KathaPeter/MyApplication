package com.example.myapplication;

import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class Helper {

    public static void jsonToView(View root, JSONObject data, int fieldId, String jsonAttrib) throws JSONException {
        //findViewById
        EditText field = (EditText) root.findViewById(fieldId);
        field.setText(data.getString(jsonAttrib));
    }

    public static void viewToJSON(View root, JSONObject data, int fieldId, String jsonAttrib) throws JSONException, ValidationException {

        EditText field = (EditText) root.findViewById(fieldId);
        String text = field.getText().toString();

        if ( text.trim().length() == 0 ) {
            throw new ValidationException("Empty field");
        }

        data.put(jsonAttrib, text);
    }
}
