package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.firestore.DocumentSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class Helper {

    public static void jsonToView(View root, JSONObject data, int fieldId, String jsonAttrib) throws JSONException {
        //findViewById
        EditText field = (EditText) root.findViewById(fieldId);
        field.setText(data.getString(jsonAttrib));
    }

    public static void viewToJSON(View root, JSONObject data, int fieldId, String jsonAttrib) throws JSONException, ValidationException {

        EditText field = (EditText) root.findViewById(fieldId);
        String text = field.getText().toString();

        if (text.trim().length() == 0) {
            throw new ValidationException("Empty field");
        }

        data.put(jsonAttrib, text);
    }

    public static void viewToJSONDecimal(View root, JSONObject data, ParseInfo info, Bundle extras, List<String> listOutOfLimits) throws JSONException, ValidationException {

        EditText field = (EditText) root.findViewById(info.fieldId);

        //trim
        String text = field.getText().toString().trim();
        if (text.length() == 0) {
            Log.i("DEBUG", "Empty field");
            throw new ValidationException("Empty field");
        }

        //PARSE
        double number = 0.0;
        try {
            number = Double.parseDouble(text);
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }

        //test Bound
        if (number < info.dMinBound || number > info.dMaxBound) {
            throw new ValidationException("Value <" + number + "> Out of Range [" + info.dMinBound + ";" + info.dMaxBound + "]");
        }

        //test Limit
        double dLimitMax = extras.getDouble(info.jsonAttrib + "MAX");
        double dLimitMin = extras.getDouble(info.jsonAttrib + "MIN");

        if ( number < dLimitMin || number > dLimitMax ) {
            listOutOfLimits.add(info.displayName);
        }

        data.put(info.jsonAttrib, number);
    }

    public static void documentToView(View root, DocumentSnapshot data, int fieldId, String attributeName) {
        EditText field = (EditText) root.findViewById(fieldId);
        field.setText(data.getString(attributeName));
    }

    public static String validate(View root, int fieldId) throws ValidationException {

        EditText field = root.findViewById(fieldId);
        String text = field.getText().toString().trim();

        if (text.length() == 0) {
            throw new ValidationException(("Empty Field "));
        }

        return text;
    }

    public static class ParseInfo {

        public final int fieldId;
        public final String jsonAttrib;
        public final String displayName;
        public final double dMinBound;
        public final double dMaxBound;

        public ParseInfo(int fieldId, String jsonAttrib, String displayName, double dMinBound, double dMaxBound) {
            this.fieldId = fieldId;
            this.jsonAttrib = jsonAttrib;
            this.displayName = displayName;
            this.dMinBound = dMinBound;
            this.dMaxBound = dMaxBound;
        }
    }
}
