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

    public static void viewToJSONDecimal(View root, JSONObject data, ParseInfo info, Bundle extras, List<String> listOutOfLimits) throws ValidationException {

        EditText field = (EditText) root.findViewById(info.fieldId);

        //trim
        String text = field.getText().toString().trim();
        if (text.length() == 0) {
            Log.i("DEBUG", "Empty field");
            throw new ValidationException("Leeres Feld vorhanden.", field);
        }

        //PARSE
        double number = 0.0;
        try {
            number = Double.parseDouble(text);
        } catch (Exception e) {
            throw new ValidationException("Konvertierungsfehler. Text -> Zahl", field);
        }

        //test Bound
        if (number < info.dMinBound || number > info.dMaxBound) {
            throw new ValidationException("Wert <" + number + "> unrealistisch.", field);
        }

        //test Limit
        double dLimitMax = extras.getDouble(info.jsonAttrib + "MAX");
        double dLimitMin = extras.getDouble(info.jsonAttrib + "MIN");

        if ( number < dLimitMin || number > dLimitMax ) {
            listOutOfLimits.add(info.displayName);
        }

        try {
            data.put(info.jsonAttrib, number);
        }catch(JSONException exc) {
            //Sollte nie passieren!
            throw new ValidationException("Interner Fehler", field);
        }
    }

    public static String validate(View root, int fieldId) throws ValidationException {

        EditText field = root.findViewById(fieldId);
        String text = field.getText().toString().trim();

        if (text.length() == 0) {
            throw new ValidationException("Leeres Feld vorhanden.", field);
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
