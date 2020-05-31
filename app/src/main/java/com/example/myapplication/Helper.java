package com.example.myapplication;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.internal.ContextUtils;

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

    public static void viewToJSONDecimal(View root, JSONObject data, int fieldId, String jsonAttrib) throws JSONException, ValidationException {

        EditText field = (EditText) root.findViewById(fieldId);
        double number = Double.parseDouble(field.getText().toString());;

        String checkInput = Double.toString(number);
        if ( checkInput.length() == 0 ) {
            Log.i("DEBUG", "Empty field");
            throw new ValidationException("Empty field");
        }

        data.put(jsonAttrib, number);
    }

}
