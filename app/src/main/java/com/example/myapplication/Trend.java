package com.example.myapplication;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.axes.Linear;
import com.anychart.core.cartesian.series.Line;
import com.anychart.core.ui.Crosshair;
import com.anychart.core.ui.Legend;
import com.anychart.core.ui.Tooltip;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.Orientation;
import com.anychart.enums.ScaleTypes;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.anychart.graphics.vector.text.HAlign;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Trend extends Fragment {

    private static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
    private View root;
    private RequestQueue requestQueue;


    //"2020-05-20T11:03:56.4614718Z"

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.tab_2, container, false);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadVitalValues(9);
    }

    private DataEntry _f(int x, int y) {
        return new ValueDataEntry(x, y);
    }

    public void loadVitalValues(int numberOfDays) {
        /*

        [
            {
                "benutzer": "marie",
                "praxis": "praxis",
                "gewicht": 55.0,
                "puls": 80.0,
                "blutdruckSys": 180.0,
                "blutdruckDia": 93.0,
                "atemfrequenz": 85.0,
                "timeStamp": "2020-05-20T11:03:56.4614718Z"  //format?
            },
            //....
        ]

         */

        Calendar calendar = Calendar.getInstance(); // this would default to now
        calendar.add(Calendar.DAY_OF_MONTH, -numberOfDays);
        Log.e(Trend.class.getSimpleName() + ".class", "minimumDate " + calendar.getTime());
        requestX(numberOfDays, 0, calendar, new ArrayList<>());
    }

    private void requestX(final int x, final int lastN, final Calendar minimumDate, final List<CustomDataEntry> filteredResult) {

        Log.e(Trend.class.getSimpleName() + ".class", "requestX call  " + x + " " + lastN + " " + filteredResult.size());

        request(x,  //
                (JSONArray response) -> {

                    final int n = response.length();
                    boolean lessThenMinimumDate = false;

                    Log.e(Trend.class.getSimpleName() + ".class", "body:  JSONArray.length() = " + n );

                    for (int i = lastN; i < n && !lessThenMinimumDate; i++) {
                        CustomDataEntry entry = CustomDataEntry.createFrom(_safeGet(response, i), i);
                        if (entry != null) {
                            Log.e(Trend.class.getSimpleName() + ".class", "entry " + i + " is " + entry.date.getTime());

                            if (entry.date.before(minimumDate)) { /*date < minimumDate*/
                                lessThenMinimumDate = true;
                            } else {
                                filteredResult.add(entry);
                            }
                        } else {
                            Log.e(Trend.class.getSimpleName() + ".class", "entry " + i + " is null");
                        }
                    }

                    if (lessThenMinimumDate || n <= lastN) {
                        showData(filteredResult);
                        Toast.makeText(getActivity(), "server: " + n + " chart: " + filteredResult.size(), Toast.LENGTH_LONG).show();
                    } else {
                        requestX(x + 10, n, minimumDate, filteredResult);
                    }

                },  //
                (Integer status) -> {
                    Toast.makeText(getActivity(), "Load Vital-Values Status " + status, Toast.LENGTH_LONG).show();
                }, //
                (VolleyError error) -> {
                    String text = error.getMessage();
                    Log.e("DEBUG", text == null ? "" : text);
                    Toast.makeText(getActivity(), "Server unreachable: Could not load Vital-Values", Toast.LENGTH_LONG).show();
                });

    }

    private JSONObject _safeGet(JSONArray response, int i) {
        try {
            return (JSONObject) response.get(i);
        } catch (Exception e) {
            Log.e(Trend.class.getSimpleName() + ".class", "ParseError: " + e.getMessage());
            return null;
        }
    }

    private void request(int x, Response.Listener<JSONArray> onStatusOK, Response.Listener<Integer> onStatusNotOk, Response.ErrorListener onError) {
        final JSONArray data = new JSONArray();
        final StatusCode mStatusCode = new StatusCode();

        final String url = "http://" + Globals.hostHealthCare + ":" + Globals.portHealthCare + "/api/GesundheitsDaten/" + Globals.benutzer + "/" + x;
        Log.e("Trend.class", "URL: " + url);

        // Request a string response from the provided URL.
        JsonArrayRequest stringRequest = new JsonArrayRequest( //
                Request.Method.GET, //
                url, //
                data, //
                (JSONArray response) -> {
                    if (mStatusCode.get() == 200) {
                        onStatusOK.onResponse(response);
                    } else {
                        onStatusNotOk.onResponse(mStatusCode.get());
                    }
                },//
                onError) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                if (response != null) {
                    mStatusCode.set(response.statusCode);
                }
                return super.parseNetworkResponse(response);
            }
        };

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest); //TODO

    }


    private Cartesian newChart() {


        Cartesian chart = AnyChart.line();

        chart.animation(true);
        chart.padding(10d, 20d, 5d, 20d);
        chart.title("Trend Vital Parameter");

        Crosshair crosshair = chart.crosshair();
        crosshair.enabled(true);
        crosshair.yLabel(true);
        crosshair.yStroke((Stroke) null, null, null, (String) null, (String) null);

        Legend legend = chart.legend();
        legend.enabled(true);
        legend.fontSize(13d);
        legend.padding(0d, 0d, 10d, 0d);

        Linear xAxis = chart.xAxis(0);
        xAxis.title("Datum");
        xAxis.labels().width(50);
        xAxis.labels().hAlign(HAlign.CENTER);
        xAxis.staggerMode(true);

        Linear yAxis0 = chart.yAxis(0);
        yAxis0.title(false);
        yAxis0.orientation(Orientation.LEFT);

        Linear yAxis1 = chart.yAxis(1);
        yAxis1.title(false);
        yAxis1.orientation(Orientation.RIGHT);

      /*

  tooltip.unionFormat(function() {
    var gdp = "GDP - " + this.points[0].value + "%";
    var debt = (this.points[1].value / 1000000000).toFixed(0);
    if (debt > 999)
      debt = ("" + debt/1000).replace(".", " ");
    return gdp + "\nDebt - $" + debt + " bil." ;
  });

       */

        Tooltip tooltip = chart.tooltip();
        tooltip.positionMode(TooltipPositionMode.POINT);
        tooltip.titleFormat( //
                "function() {" +
                        "    return \"Day: \" + this.points[0].x;" +
                        "  }");

        return chart;
    }

    private void showData(List<CustomDataEntry> list) {


        //set Data

        Cartesian chart1 = newChart();

        Set set = Set.instantiate();
        set.data(new ArrayList<>(list));

        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value5' }");


        Line series1 = chart1.line(series1Mapping);
        series1.name("Gewicht");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);
        series1.yScale(ScaleTypes.LINEAR);


        Line series2 = chart1.line(series2Mapping);
        series2.name("Puls");
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        //series2.color(new SolidFill("#FF0000", 1.0));
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);


        Line series3 = chart1.line(series3Mapping);
        series3.name("Atemfrequenz");
        series3.hovered().markers().enabled(true);
        series3.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series3.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);
        series3.yScale(ScaleTypes.LINEAR);

        //draw
        AnyChartView anyChartView1 = root.findViewById(R.id.any_chart_view1);
        anyChartView1.setChart(chart1);


    }

    private static class CustomDataEntry extends ValueDataEntry {


        public final Calendar date;

        public CustomDataEntry(String x, Number value1, Number value2, Number value3, Number value4, Number value5, Calendar date) {
            super(x, value1);
            this.date = date;
            setValue("value2", value2);
            setValue("value3", value3);
            setValue("value4", value4);
            setValue("value5", value5);
            setValue("date", date.toString());


        }

        public static CustomDataEntry createFrom(JSONObject o, int index) {
            if (o == null) {
                return null;
            }
            try {
                double dGewicht = Double.valueOf(o.getString("gewicht"));
                double dPuls = Double.valueOf(o.getString("puls"));
                double dBlutSys = Double.valueOf(o.getString("blutdruckSys"));
                double dBlutDias = Double.valueOf(o.getString("blutdruckDia"));
                double dAtemFreq = Double.valueOf(o.getString("atemfrequenz"));


                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateFormater.parse(o.getString("timeStamp")));

                String xValue =
                        (calendar.get(Calendar.MONTH) + 1) + "/" +
                                calendar.get(Calendar.DAY_OF_MONTH);

                if (!Globals.filterTrendsPerDay) {
                    xValue += "_" + index;
                }
                return new CustomDataEntry(xValue, dGewicht, dPuls, dBlutSys, dBlutDias, dAtemFreq, calendar);
            } catch (Exception exc) {
                Log.e(CustomDataEntry.class.getSimpleName() + ".class", "ParseError: " + exc.getMessage());
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
}
