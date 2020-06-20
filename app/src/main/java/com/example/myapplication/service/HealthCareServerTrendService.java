package com.example.myapplication.service;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class HealthCareServerTrendService {

    public static void request(RequestQueue queue, String url, Response.Listener<JSONArray> onStatusOK, Response.Listener<Integer> onStatusNotOk, Response.ErrorListener onError) {
        final JSONArray data = new JSONArray();
        final StatusCode mStatusCode = new StatusCode();


        Log.e("TrendService.class", url);

        // Request a response from the provided URL.
        JsonArrayRequest myRequest = new JsonArrayRequest( //
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
        };

        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(myRequest);
    }

    public static void request(RequestQueue queue, String url, JSONObject data, Response.Listener<JSONObject> onStatusOK, Response.Listener<Integer> onStatusNotOk, Response.ErrorListener onError) {

        final StatusCode mStatusCode = new StatusCode();


        Log.e("TrendService.class", url);

        // Request a response from the provided URL.
        JsonObjectRequest myRequest = new JsonObjectRequest( //
                Request.Method.POST, //
                url, //
                data, //
                (JSONObject response) -> {
                    if (mStatusCode.get() == 200) {
                        onStatusOK.onResponse(response);
                    } else {
                        onStatusNotOk.onResponse(mStatusCode.get());
                    }
                },//
                onError) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                if (response != null) {
                    mStatusCode.set(response.statusCode);
                    if (response.data.length == 0) {
                        return Response.success(new JSONObject(), HttpHeaderParser.parseCacheHeaders(response));
                    }
                }
                return super.parseNetworkResponse(response);
            }
        };

        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(myRequest);
    }
}
