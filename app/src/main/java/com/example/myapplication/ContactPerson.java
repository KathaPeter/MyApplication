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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactPerson extends Fragment {

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
        root = inflater.inflate(R.layout.tab_3, container, false);

        Button buttonSave = (Button) root.findViewById(R.id.bt_save);
        //  public Dialog_YesNo(FragmentActivity parent, String question, IEvent handlerYes, @Nullable IEvent handlerNo)
        buttonSave.setOnClickListener((v) ->
                new Dialog_YesNo(getActivity(), G.qSaveContactPerson, ContactPerson.this::updateContactPerson, null)
        );

        Button buttonCancel = (Button) root.findViewById(R.id.bt_cancel);
        buttonCancel.setOnClickListener((v) ->
                new Dialog_YesNo(getActivity(), G.qCancelContactPerson, ContactPerson.this::loadContactPerson, null)
        );


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        loadContactPerson();
    }

    private void updateContactPerson() {

        /*

        {
            "firstname":"Marie"
            "lastname":"johanna"
            "phonenumber": "0178/666666"
            "email": "pilz@oettinger.de"
            "street" : "Ehrlichweg"
            "housenumber" : "6a"  (String?)
            "postalcode" : "77652"
        }

      */

        JSONObject data = new JSONObject();
        try {

            Helper.viewToJSON(root, data, R.id.input_firstname, "firstname");
            Helper.viewToJSON(root, data, R.id.input_lastname, "lastname");
            Helper.viewToJSON(root, data, R.id.input_telephone, "phonenumber");
            Helper.viewToJSON(root, data, R.id.input_email, "email");
            Helper.viewToJSON(root, data, R.id.input_street, "street");
            Helper.viewToJSON(root, data, R.id.input_housenumber, "housenumber");
            Helper.viewToJSON(root, data, R.id.input_plz, "postalcode");


            // Request a string response from the provided URL.
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://" + G.hostFireBase + ":" + G.portFireBase + "/UpdateDataContactPerson", data, //
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Display the first 500 characters of the response string.
                            try {

                                String return_type = response.getString("return_type");

                                if (return_type.compareTo(G.RETURN_OK) == 0) {
                                    Toast.makeText(getActivity(), "Update ContactPerson succeed", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getActivity(), "Error: "+ response.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                Log.i("DEBUG", "JSONException in STATUS 200 "+ e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }, //
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("DEBUG", error.getMessage());
                            Toast.makeText(getActivity(), "Server unreachable", Toast.LENGTH_LONG).show();
                        }
                    });

            // Add the request to the RequestQueue.
            requestQueue.add(stringRequest);

        } catch (JSONException e) {
            //SHOULD NEVER HAPPEN checked
            e.printStackTrace();
        } catch (ValidationException e) {
            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


    private void loadContactPerson() {

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://" + G.hostFireBase + ":" + G.portFireBase + "/GetDataContactPerson", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Display the first 500 characters of the response string.
                try {

                    String return_type = response.getString("return_type");

                    if (return_type.compareTo(G.RETURN_OK) == 0) {

                        JSONObject data = response.getJSONObject("data");

                        Helper.jsonToView(root, data, R.id.input_firstname, "firstname");
                        Helper.jsonToView(root, data, R.id.input_lastname, "lastname");
                        Helper.jsonToView(root, data, R.id.input_telephone, "phonenumber");
                        Helper.jsonToView(root, data, R.id.input_email, "email");
                        Helper.jsonToView(root, data, R.id.input_housenumber, "housenumber");
                        Helper.jsonToView(root, data, R.id.input_street, "street");
                        Helper.jsonToView(root, data, R.id.input_plz, "postalcode");

                    } else {
                        Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Server unreachable", Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);

    }
}
