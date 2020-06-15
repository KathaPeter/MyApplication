package com.example.myapplication.data;

import android.icu.util.Calendar;
import android.util.Log;

import androidx.annotation.NonNull;

import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.example.myapplication.Globals;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AnyChartDataEntry extends ValueDataEntry {

    private static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'", Locale.US);

    public final Calendar date;

    public AnyChartDataEntry(String x, Number value1, Number value2, Number value3, Number value4, Number value5, Calendar date) {
        super(x, value1);
        this.date = date;
        setValue("value2", value2);
        setValue("value3", value3);
        setValue("value4", value4);
        setValue("value5", value5);
        setValue("date", date.toString());


    }

    public static AnyChartDataEntry createFrom(JSONObject o, int index) {
        if (o == null) {
            return null;
        }
        try {
            double dGewicht = Double.parseDouble(o.getString("gewicht"));
            double dPuls = Double.parseDouble(o.getString("puls"));
            double dBlutSys = Double.parseDouble(o.getString("blutdruckSys"));
            double dBlutDias = Double.parseDouble(o.getString("blutdruckDia"));
            double dAtemFreq = Double.parseDouble(o.getString("atemfrequenz"));


            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormater.parse(o.getString("timeStamp")));

            String xValue =
                    (calendar.get(Calendar.MONTH) + 1) + "/" +
                            calendar.get(Calendar.DAY_OF_MONTH);

            if (!Globals.filterTrendsPerDay) {
                xValue += "_" + (char) ('a'+index);
            }
            return new AnyChartDataEntry(xValue, dGewicht, dPuls, dBlutSys, dBlutDias, dAtemFreq, calendar);
        } catch (Exception exc) {
            Log.e(AnyChartDataEntry.class.getSimpleName() + ".class", "ParseError: " + exc.getMessage());
            return null;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "{" + //
                getValue("x") + ", " + //
                getValue("value") + ", " + //
                getValue("value2") + ", " + //
                getValue("value3") + ", " + //
                getValue("value4") + ", " + //
                getValue("value5") + " }\n\r";
    }
}