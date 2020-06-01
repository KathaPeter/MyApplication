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
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
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
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.tab_2, container, false);

        return root;
    }


    @Override
    public void onStart() {
        super.onStart();



        AnyChartView anyChartView = root.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(root.findViewById(R.id.progress_bar));



        loadVitalValues(9);
    }

    private DataEntry _f(int x, int y) {
        return new ValueDataEntry(x, y);
    }


    public void loadVitalValues(int numberOfDataPackages) {


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

        final JSONArray data = new JSONArray();
        final StatusCode mStatusCode = new StatusCode();

        // Request a string response from the provided URL.
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, "http://" + Globals.hostHealthCare + ":" + Globals.portHealthCare
                + "/api/GesundheitsDaten/" + Globals.benutzer + "/" + numberOfDataPackages, data, //
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {



                        if (mStatusCode.get() == 200) {

                            int n = response.length();
                            List<DataEntry> list = new ArrayList<>();


                            try {

                                Log.d(Trend.class.toString(), ""+response.length());
                                Log.d(Trend.class.toString(), ""+response.toString(3));

                                for (int i = 0; i < n; i++) {

                                    JSONObject o = null;

                                    o = (JSONObject) response.get(i);


                                    double sGewicht = Double.valueOf(o.getString("gewicht"));
                                    double sPuls = Double.valueOf(o.getString("puls"));
                                    double sBlutSys = Double.valueOf(o.getString("blutdruckSys"));
                                    double dBlutDias = Double.valueOf(o.getString("blutdruckDia"));
                                    double dAtemFreq = Double.valueOf(o.getString("atemfrequenz"));


                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(dateFormater.parse(o.getString("timeStamp")));

                                    String x = i +"";/*
                                            calendar.get(Calendar.MONTH) + "/" +
                                                    calendar.get(Calendar.DAY_OF_MONTH);*/

                                    list.add(new CustomDataEntry(x, sGewicht, sPuls, sBlutSys, dBlutDias, dAtemFreq));
                                }

                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }

                            //showData TODO
                            showData(list);

                        } else {
                            Toast.makeText(getActivity(), "Load Vital-Values Status " + mStatusCode.get(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, //
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String text = error.getMessage();
                        Log.i("DEBUG", text == null ? "" : text);
                        Toast.makeText(getActivity(), "Server unreachable: Could not load Vital-Values", Toast.LENGTH_LONG).show();
                    }
                }) {

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

    private void showData(List<DataEntry> list) {



      /*
           INIT AnyChart
         */
        Cartesian chart = AnyChart.line();

        chart.animation(true);

        chart.padding(10d, 20d, 5d, 20d);

        chart.crosshair().enabled(true);
        chart.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        chart.tooltip().positionMode(TooltipPositionMode.POINT);

        chart.title("Historical Trend Vital Values");

        chart.xScale(ScaleTypes.LINEAR);
        chart.yScale(ScaleTypes.LINEAR);

/*

        var xScale = chart.xScale();
        xScale.minimum(1940);
        xScale.maximum(2010);
        xScale.ticks().interval(5);

        // setting yScale settings
        var yScale = chart.yScale();
        yScale.minimum(0);
        yScale.maximum(12000000000000);
        yScale.ticks().interval(2000000000000);

       // adding and adjusting extra Y scale
       var extraYScale = anychart.scales.linear();
       extraYScale.minimum(0);
       extraYScale.maximum(140);
       extraYScale.ticks().interval(20);

*/


        Linear yAxis0 = chart.yAxis(0);
        yAxis0.title(false);
        yAxis0.orientation(Orientation.LEFT);
        yAxis0.scale(ScaleTypes.LINEAR);
        // yAxis0.labels().format("${%value}{scale:(1000000000000)|(T)}");

        Linear yAxis1 = chart.yAxis(1);
        yAxis1.title(false);


        yAxis1.orientation(Orientation.RIGHT);
        yAxis1.scale(ScaleTypes.LINEAR);
        // yAxis1.labels().format("{%value}%");


        chart.xAxis(0).title("Datum");
        chart.xAxis(0).labels().width(50);
        chart.xAxis(0).labels().hAlign(HAlign.CENTER);
        chart.xAxis(0).staggerMode(true);


        /*

  // adjusting data visualisation
  var lineSeries = chart.line(getData1());
  lineSeries.stroke("#00F");
  lineSeries.hovered().stroke("#00F");
  lineSeries.yScale(extraYScale);

  var columnSeries = chart.column(getData2());
  columnSeries.fill(["#FF0000", "#880000"], -90);
  columnSeries.stroke("#000", 1);
  columnSeries.hovered().stroke("#FFF", 1);
  columnSeries.hovered().fill(["#FF0000", "#880000"], -90);



         */


        // chart.yAxis(0).title(" Datum ");
        // chart.yAxis(0).title(" Herzfrequenz: Anzahl Herzschläge pro Sekunde ");
        // chart.xAxis(0).labels().padding(5d, 5d, 5d, 5d);



      /*


      var tooltip = chart.tooltip();
  tooltip.titleFormat(function() {
    return "Year: " + this.points[0].x;
  });
  tooltip.unionFormat(function() {
    var gdp = "GDP - " + this.points[0].value + "%";
    var debt = (this.points[1].value / 1000000000).toFixed(0);
    if (debt > 999)
      debt = ("" + debt/1000).replace(".", " ");
    return gdp + "\nDebt - $" + debt + " bil." ;
  });

  chart.container("container");
  chart.draw();

       */

        Tooltip tooltip = chart.tooltip();
        tooltip.titleFormat( //
                "function() {" +
                        "    return \"YearXXX: \" + this.points[0].x;" +
                        "  }");

        chart.legend().enabled(true);
        chart.legend().fontSize(13d);
        chart.legend().padding(0d, 0d, 10d, 0d);


       //set Data

        Set set = Set.instantiate();
        set.data(list);

        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");

        Line series1 = chart.line(series1Mapping);

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


        Line series2 = chart.line(series2Mapping);
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

        //draw
        AnyChartView anyChartView = root.findViewById(R.id.any_chart_view);

        anyChartView.setChart(chart);



    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value1, Number value2, Number value3, Number value4, Number value5) {
            super(x, value1);
            setValue("value2", value2);
            setValue("value3", value3);
            setValue("value4", value4);
            setValue("value5", value5);
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
