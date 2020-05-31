package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Input_VitalParameter extends Fragment {

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
        root = inflater.inflate(R.layout.tab_1, container, false);

        Button button = (Button) root.findViewById(R.id.bt_send);
        button.setOnClickListener((View v) -> {
            new Dialog_YesNo(getActivity(), G.qSendVitalParam, Input_VitalParameter.this::sendVitalParam, null);
        });

        return root;
    }

    private void sendVitalParam() {

        /*
        {
            "benutzer": "marie",
            "praxis": "praxis",
            "gewicht": 85.0,
            "puls": 140.0,
            "blutdruckSys": 200.0,
            "blutdruckDia": 70.0,
            "atemfrequenz": 80.0,
            "timeStamp": "0001-01-01T00:00:00"
        }
         */

        JSONObject data = new JSONObject();
        try {

            Helper.viewToJSONDecimal(root, data, R.id.gewicht, "gewicht");
            Helper.viewToJSONDecimal(root, data, R.id.puls, "puls");
            Helper.viewToJSONDecimal(root, data, R.id.blutdruck_systolisch, "blutdruckSys");
            Helper.viewToJSONDecimal(root, data, R.id.blutdruck_diastolisch, "blutdruckDia");
            Helper.viewToJSONDecimal(root, data, R.id.atemfrequenz, "atemfrequenz");

            data.put("timeStamp", "0001-01-01T00:00:00");
            data.put("benutzer", G.benutzer);
            data.put("praxis", G.praxis);
            //data.put("gewicht", R.id.gewicht);
            //data.put("puls", R.id.puls);
            //data.put("blutdruckSys", R.id.blutdruck_systolisch);
            //data.put("blutdruckDias", R.id.blutdruck_diastolisch);
            //data.put("blutdruckDias", R.id.atemfrequenz);

            //TemporalStorage for HTTP StatusCode
            final StatusCode mStatusCode = new StatusCode();

            // Request a string response from the provided URL.
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://" + G.hostHealthCare + ":" + G.portHealthCare + "/api/GesundheitsDaten", data, //
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("DEBUG", "SendVitalParam Status: ["+mStatusCode.get()+"]");
                        }
                    }, //
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String text = error.getMessage();
                            Log.i("DEBUG", text == null ? "<null>" : text );
                            Toast.makeText(getActivity(), "Server unreachable", Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    if (response != null) {
                         mStatusCode.set(response.statusCode);
                    }
                    return super.parseNetworkResponse(response);
                }

            };

            // Add the request to the RequestQueue.
            requestQueue.add(stringRequest);

        } catch (JSONException e) {
            //SHOULD NEVER HAPPEN checked
            e.printStackTrace();
        } catch (ValidationException e) {
            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}





