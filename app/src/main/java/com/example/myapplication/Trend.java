package com.example.myapplication;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Trend extends Fragment {

    private View root;
    private RequestQueue requestQueue;

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




        /*




  var chart = anychart.line();

  chart.title("Extra Axes Units Comparison Sample");

  // setting xScale settings
  chart.xScale(anychart.scales.linear());

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



  // adding and adjusting extra Y axis
  var extraYAxis = chart.yAxis(1);
  extraYAxis.orientation("right");
  extraYAxis.scale(extraYScale);

  // setting yAxes titles
  chart.yAxis(0).title("Debt");
  extraYAxis.title("GDP");

  // yAxes labels text adjusting
  chart.yAxis(0).labels().format("${%value}{scale:(1000000000000)|(T)}");
  chart.yAxis(1).labels().format("{%value}%");

  // disable x axis title
  var xAxis = chart.xAxis();
  xAxis.title(false);

  // enabling x axis stagger mode
  xAxis.staggerMode(true);

  // settings for labels of x axis
  xAxis.labels().width(50);
  xAxis.labels().hAlign("center");



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
});

function getData2(){
    return [
    [1940, 45500000000],
    [1941, 56500000000],
    [1942, 100000000000],
    [1943, 170000000000],
    [1944, 216500000000],
    [1945, 263535000000],
    [1946, 262600000000],
    [1947, 257900000000],
    [1948, 252563000000],
    [1949, 257037000000],
    [1950, 257357352351],
    [1951, 255221976815],
    [1952, 259105178785],
    [1953, 275168120129],
    [1954, 278749814391],
    [1955, 280768553189],
    [1956, 276627527996],
    [1957, 274897784291],
    [1958, 282922423583],
    [1959, 290797771718],
    [1960, 310000000000],
    [1961, 296168761214],
    [1962, 303470080489],
    [1963, 309346845059],
    [1964, 317940472718],
    [1965, 320904110042],
    [1966, 329319249366],
    [1967, 344663009745],
    [1968, 358028625002],
    [1969, 368225581254],
    [1970, 389158403690],
    [1971, 424130961959],
    [1972, 449298066119],
    [1973, 469898039554],
    [1974, 492665000000],
    [1975, 576649000000],
    [1976, 653544000000],
    [1977, 718943000000],
    [1978, 789207000000],
    [1979, 845116000000],
    [1980, 930210000000],
    [1981, 1028729000000],
    [1982, 1197073000000],
    [1983, 1410702000000],
    [1984, 1662966000000],
    [1985, 1945941616459],
    [1986, 2125302616658],
    [1987, 2350276890953],
    [1988, 2602337712041],
    [1989, 2857430960187],
    [1990, 3233313451777],
    [1991, 3665303351697],
    [1992, 4064620655521],
    [1993, 4411488883139],
    [1994, 4692749910013],
    [1995, 4973982900709],
    [1996, 5224810939135],
    [1997, 5413146011397],
    [1998, 5526193008897],
    [1999, 5605523901615],
    [2000, 5674178209887],
    [2001, 5807463412200],
    [2002, 6228235965597],
    [2003, 6783231062743],
    [2004, 7379052696330],
    [2005, 7932709661723],
    [2006, 8506973899215],
    { x: 2007, value: 9350102000000, fill: "#FFF", hoverFill: "#FFF", stroke: "#000", hoverStroke: "#000"},
    { x: 2008, value: 9948640000000, fill: "#FFF", hoverFill: "#FFF", stroke: "#000", hoverStroke: "#000"},
    { x: 2009, value: 10543521000000, fill: "#FFF", hoverFill: "#FFF", stroke: "#000", hoverStroke: "#000"},
    { x: 2010, value: 11137297000000, fill: "#FFF", hoverFill: "#FFF", stroke: "#000", hoverStroke: "#000"}
  ];
}

         */

        loadVitalValues(9);

        AnyChartView anyChartView = root.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(root.findViewById(R.id.progress_bar));


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

        List<DataEntry> seriesData = new ArrayList<>();
        seriesData.add(new CustomDataEntry("1986", 3.6, 2.3, 2.8));
        seriesData.add(new CustomDataEntry("1987", 7.1, 4.0, 4.1));
        seriesData.add(new CustomDataEntry("1988", 8.5, 6.2, 5.1));
        seriesData.add(new CustomDataEntry("1989", 9.2, 11.8, 6.5));
        seriesData.add(new CustomDataEntry("1990", 10.1, 13.0, 12.5));
        seriesData.add(new CustomDataEntry("1991", 11.6, 13.9, 18.0));
        seriesData.add(new CustomDataEntry("1992", 16.4, 18.0, 21.0));
        seriesData.add(new CustomDataEntry("1993", 18.0, 23.3, 20.3));
        seriesData.add(new CustomDataEntry("1994", 13.2, 24.7, 19.2));
        seriesData.add(new CustomDataEntry("1995", 12.0, 18.0, 14.4));
        seriesData.add(new CustomDataEntry("1996", 3.2, 15.1, 9.2));
        seriesData.add(new CustomDataEntry("1997", 4.1, 11.3, 5.9));
        seriesData.add(new CustomDataEntry("1998", 6.3, 14.2, 5.2));
        seriesData.add(new CustomDataEntry("1999", 9.4, 13.7, 4.7));
        seriesData.add(new CustomDataEntry("2000", 11.5, 9.9, 4.2));
        seriesData.add(new CustomDataEntry("2001", 13.5, 12.1, 1.2));
        seriesData.add(new CustomDataEntry("2002", 14.8, 13.5, 5.4));
        seriesData.add(new CustomDataEntry("2003", 16.6, 15.1, 6.3));
        seriesData.add(new CustomDataEntry("2004", 18.1, 17.9, 8.9));
        seriesData.add(new CustomDataEntry("2005", 17.0, 18.9, 10.1));
        seriesData.add(new CustomDataEntry("2006", 16.6, 20.3, 11.5));
        seriesData.add(new CustomDataEntry("2007", 14.1, 20.7, 12.2));
        seriesData.add(new CustomDataEntry("2008", 15.7, 21.6, 10));
        seriesData.add(new CustomDataEntry("2009", 12.0, 22.5, 8.9));


        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");

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
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

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

        anyChartView.setChart(chart);
    }

    private List<DataEntry> getData1() {
        return Arrays.asList(
                _f(0, 0),
                _f(0, 0),
                _f(0, 0),
                _f(0, 0),
                _f(0, 0),
                _f(0, 0),
                _f(0, 0)
        );
    }

    private DataEntry _f(int x, int y) {
        return new ValueDataEntry(x,y);
    }


    public void loadVitalValues(int numberOfDataPackages) {

        final JSONArray data = new JSONArray();
        final StatusCode mStatusCode = new StatusCode();

        // Request a string response from the provided URL.
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, "http://" + G.hostHealthCare + ":" + G.portHealthCare
                + "/api/GesundheitsDaten/" + G.benutzer + "/" + numberOfDataPackages, data, //
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            Log.i("DEBUG", "LoadData: STATUS  " + mStatusCode.get() + " " + response.toString(3));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("DEBUG", "Error onResponse" + e.getMessage());
                        }
                    }
                }, //
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String text = error.getMessage();
                        Log.i("DEBUG", text == null ? "" : text);
                        Toast.makeText(getActivity(), "Server unreachable", Toast.LENGTH_LONG).show();
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

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value, Number value2, Number value3) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
        }

    }
}
