package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.axes.Linear;
import com.anychart.core.axismarkers.Range;
import com.anychart.core.cartesian.series.Line;
import com.anychart.core.ui.Crosshair;
import com.anychart.core.ui.Legend;
import com.anychart.core.ui.Tooltip;
import com.anychart.core.utils.LegendItemSettings;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.Orientation;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.anychart.graphics.vector.text.HAlign;
import com.example.myapplication.data.AnyChartDataEntry;
import com.example.myapplication.service.HealthCareServerTrendService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Trend extends Fragment {

    private View root;
    private RequestQueue requestQueue;
    private List<DataEntry> list = null;
    private ArrayList<SpinnerItem> spinnerItems;
    private Set set;

    //"2020-05-20T11:03:56.4614718Z"

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.tab_2, container, false);

        //dGewicht, dPuls, dBlutSys, dBlutDias, dAtemFreq
        spinnerItems = new ArrayList<>();
        spinnerItems.add(new SpinnerItem("Gewicht", "{ x: 'x', value: 'value' }", "gewicht"));
        spinnerItems.add(new SpinnerItem("Puls", "{ x: 'x', value: 'value2' }", "puls"));
        spinnerItems.add(new SpinnerItem("Blutdruck (sys)", "{ x: 'x', value: 'value3' }", "blutdruckSys"));
        spinnerItems.add(new SpinnerItem("Blutdruck (dias)", "{ x: 'x', value: 'value4' }", "blutdruckDia"));
        spinnerItems.add(new SpinnerItem("Atemfrequenz", "{ x: 'x', value: 'value5' }", "atemfrequenz"));

        loadLimitValues();
        initAnyChart();

        Spinner spinner = root.findViewById(R.id.ddSeries);


        ArrayAdapter<SpinnerItem> arrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, spinnerItems);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem item = (SpinnerItem) parent.getItemAtPosition(position);

                showData(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        SeekBar seekbar = root.findViewById(R.id.barDays);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                loadVitalValues(seekBar.getProgress() * 5);
            }
        });

        return root;
    }

    private void loadLimitValues() {


        final String benutzer = Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras()).getString("patient_vorname");
        final String url = "http://" + Globals.hostHealthCare + ":" + Globals.portHealthCare + "/api/Alarm/" + benutzer;

        HealthCareServerTrendService.request(//
                requestQueue, //
                url,  //
                (JSONArray response) -> {

                    final int n = response.length();

                    List<DataEntry> list = new ArrayList<>(n);

                    for (int i = 0; i < n; i++) {

                        String check = null;
                        double min = -1;
                        double max = -1;
                        try {
                            JSONObject jsonObject = _safeGet(response, i);
                            check = jsonObject.getString("check");
                            min = jsonObject.getDouble("min");
                            max = jsonObject.getDouble("max");
                        } catch (JSONException exc) {

                        }

                        SpinnerItem itemFound = null;
                        for (SpinnerItem item : spinnerItems) {
                            if (item.getJsonAttributeName().compareTo(check) == 0) {
                                itemFound = item;
                                break;
                            }
                        }

                        if (itemFound == null) {
                            Log.e("Trend.class", "LoadLimit: unknown JSONObject check <" + check + ">");
                        } else {
                            itemFound.setRange(min, max);
                        }
                    }
                },  //
                (Integer status) -> Toast.makeText(getActivity(), "Load Vital-Values Status " + status, Toast.LENGTH_LONG).show(), //
                (VolleyError error) -> {
                    String text = error.getMessage();
                    Log.e("DEBUG", text == null ? "" : text);
                    Toast.makeText(getActivity(), "Server unreachable: Could not load Vital-Values", Toast.LENGTH_LONG).show();
                });

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

        final String benutzer = Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras()).getString("patient_vorname");
        final String url = "http://" + Globals.hostHealthCare + ":" + Globals.portHealthCare + "/api/GesundheitsDaten/" + benutzer + "/" + numberOfDays;

        HealthCareServerTrendService.request(//
                requestQueue, //
                url,  //
                (JSONArray response) -> {

                    final int n = response.length();

                    List<DataEntry> list = new ArrayList<>(n);

                    for (int i = 0; i < n; i++) {
                        AnyChartDataEntry entry = AnyChartDataEntry.createFrom(_safeGet(response, i), i);
                        if (entry != null) {
                            Log.e(Trend.class.getSimpleName() + ".class", "entry " + i + " is " + entry.date.getTime());
                            list.add(entry);
                        } else {
                            Log.e(Trend.class.getSimpleName() + ".class", "entry " + i + " is null");
                        }
                    }

                    Trend.this.list = list;
                    //Toast.makeText(getActivity(), "server: " + n + " chart: " + list.size(), Toast.LENGTH_LONG).show();

                    Spinner spinner = root.findViewById(R.id.ddSeries);
                    showData((SpinnerItem) spinner.getSelectedItem());

                },  //
                (Integer status) -> Toast.makeText(getActivity(), "Load Vital-Values Status " + status, Toast.LENGTH_LONG).show(), //
                (VolleyError error) -> {
                    String text = error.getMessage();
                    Log.e("DEBUG", text == null ? "" : text);
                    Toast.makeText(getActivity(), "Server unreachable: Could not load Vital-Values", Toast.LENGTH_LONG).show();
                });
    }

    private void initAnyChart() {

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


        Tooltip tooltip = chart.tooltip();
        tooltip.positionMode(TooltipPositionMode.POINT);
        tooltip.titleFormat( //
                "function() {" +
                        "    return \"Day: \" + this.points[0].x;" +
                        "  }");

        set = Set.instantiate();


        for (int i = 0; i < spinnerItems.size(); i++) {
            SpinnerItem item = spinnerItems.get(i);

            //TREND VITAL_PARAMETER
            Line series = chart.line(set.mapAs(item.getMapping()));
            series.name(item.toString());

            series.hovered().markers().enabled(true);
            series.hovered().markers()
                    .type(MarkerType.CIRCLE)
                    .size(4d);
            series.tooltip()
                    .position("right")
                    .anchor(Anchor.LEFT_CENTER)
                    .offsetX(5d)
                    .offsetY(5d);

            //Range
            Range rangeMarker = chart.rangeMarker(i);
            rangeMarker.axis(yAxis0);
            rangeMarker.fill("#009900 0.4");
            rangeMarker.enabled(false);

            item.setLine(series, rangeMarker);
        }

        //just once
        AnyChartView anyChartView1 = root.findViewById(R.id.any_chart_view1);
        anyChartView1.setChart(chart);

    }

    @Override
    public void onStart() {
        super.onStart();

        SeekBar bar = root.findViewById(R.id.barDays);
        loadVitalValues(bar.getProgress() * 5);
    }


    private void showData(SpinnerItem item) {

        if (list != null) {

            //set Data
            set.data(list);

            for (SpinnerItem i : spinnerItems) {
                i.getLine().enabled(false);
                i.getLegendItem().disabled();
                i.getLegendItem().iconEnabled(false);
                i.getLegendItem().text(".");
                i.getRange().enabled(false);
            }

            item.getLine().enabled(true);
            item.getLegendItem().enabled();
            item.getLegendItem().iconEnabled(true);
            item.getLegendItem().text(item.toString());
            item.getRange().enabled(true);

            //Toast.makeText(getContext(), item.toString(), Toast.LENGTH_LONG).show();

        }
    }

    private JSONObject _safeGet(JSONArray response, int i) {
        try {
            return (JSONObject) response.get(i);
        } catch (Exception e) {
            Log.e(Trend.class.getSimpleName() + ".class", "ParseError: " + e.getMessage());
            return null;
        }
    }


    static class SpinnerItem {

        private final String display;
        private final String mapping;
        private final String jsonAttributeName;
        private Line line = null;
        private LegendItemSettings legendItem;
        private Range range;
        private double min;
        private double max;

        public SpinnerItem(String display, String mapping, String jsonAttributeName) {
            this.display = Objects.requireNonNull(display);
            this.mapping = mapping;
            this.jsonAttributeName = jsonAttributeName;
        }

        public void setLine(Line line, Range rangeMarker) {
            this.line = line;
            this.range = rangeMarker;
            this.legendItem = line.legendItem();

            rangeMarker.from(min);
            rangeMarker.to(max);
        }

        public void setRange(double min, double max) {
            this.min = min;
            this.max = max;
        }

        public String getJsonAttributeName() {
            return jsonAttributeName;
        }

        public LegendItemSettings getLegendItem() {
            return legendItem;
        }

        public Line getLine() {
            return line;
        }

        public Range getRange() {
            return range;
        }

        public String getMapping() {
            return mapping;
        }

        @NonNull
        @Override
        public String toString() {
            return display;
        }
    }

}
