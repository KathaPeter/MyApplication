package com.example.myapplication.service;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

public class HealthCareServerTrendService {

    public static void request(RequestQueue queue, String url, Response.Listener<JSONArray> onStatusOK, Response.Listener<Integer> onStatusNotOk, Response.ErrorListener onError) {
        final JSONArray data = new JSONArray();
        final StatusCode mStatusCode = new StatusCode();


        // Request a response from the provided URL.
        queue.add(new JsonArrayRequest( //
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
                    if (response.data.length == 0) {
                        return Response.success(new JSONArray(), HttpHeaderParser.parseCacheHeaders(response));
                    }
                }
                return super.parseNetworkResponse(response);
            }
        });
    }
}
