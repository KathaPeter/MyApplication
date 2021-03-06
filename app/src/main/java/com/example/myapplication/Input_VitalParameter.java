package com.example.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.service.FirestoreFormularService;
import com.example.myapplication.service.HealthCareServerTrendService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Input_VitalParameter extends Fragment {

    private View root;
    private RequestQueue requestQueue;
    private ArrayList<Helper.ParseInfo> parseInfos;
    private Drawable errorBackgground = null;
    private Drawable inputBackgground = null;
    private WelcomeActivity welcomeActivity = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        errorBackgground = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.background_error);
        inputBackgground = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.background_input);

        //Volley: Ein Werkzeug um ein RESTful-System zwischen einem Client und einem Remote-Backend zu entwickeln.
        requestQueue = Volley.newRequestQueue(getActivity());

        parseInfos = new ArrayList<Helper.ParseInfo>();
        parseInfos.add(new Helper.ParseInfo(R.id.gewicht, "gewicht", "Gewicht", 10, 180));
        parseInfos.add(new Helper.ParseInfo(R.id.puls, "puls", "Puls", 5, 140));
        parseInfos.add(new Helper.ParseInfo(R.id.blutdruck_systolisch, "blutdruckSys", "Blutdruck (sys)", 30, 190));
        parseInfos.add(new Helper.ParseInfo(R.id.blutdruck_diastolisch, "blutdruckDia", "Blutdruck (dias)", 30, 230));
        parseInfos.add(new Helper.ParseInfo(R.id.atemfrequenz, "atemfrequenz", "Atemfrequenz", 10, 180));

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.tab_1, container, false);

        Button button = (Button) root.findViewById(R.id.bt_send);
        button.setOnClickListener((View v) -> {
            new DialogYesNo(getActivity(), Globals.qSendVitalParam, Input_VitalParameter.this::sendVitalParam, null);
        });

        Button buttonClear = (Button) root.findViewById(R.id.bt_clear);
        buttonClear.setOnClickListener((View v) -> {
            new DialogYesNo(getActivity(), Globals.qClearVitalParam, Input_VitalParameter.this::emptyVitalParams, null);
        });

        emptyVitalParams();
        testDisableInputs();
        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof WelcomeActivity) {
            welcomeActivity = (WelcomeActivity) context;
        }
    }

    private void emptyVitalParams() {
        clear(R.id.blutdruck_diastolisch);
        clear(R.id.blutdruck_systolisch);
        clear(R.id.atemfrequenz);
        clear(R.id.puls);
        clear(R.id.gewicht);
    }

    private void clear(int fieldId) {
        EditText view = (EditText) root.findViewById(fieldId);
        view.setBackground(inputBackgground);
        view.setText("");
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

        final JSONObject data = new JSONObject();
        final List<String> listOutOfLimits = new ArrayList<>();
        final Bundle extras = getActivity().getIntent().getExtras();
        final List<ValidationException> errors = new ArrayList<>();


        for (Helper.ParseInfo info : parseInfos) {
            try {
                //Rotes Feld wieder Schwarz markieren
                root.findViewById(info.fieldId).setBackground(inputBackgground);
                Helper.viewToJSONDecimal(root, data, info, extras, listOutOfLimits);
            } catch (ValidationException e) {
                e.getField().setBackground(errorBackgground);
                Log.d("Input_VitalParameter.class", "ValidationError " + e.getMessage());
                errors.add(e);
            }
        }

        if (errors.isEmpty()) {
            try {
                data.put("timeStamp", "0001-01-01T00:00:00");
                data.put("benutzer", HealthCareServerTrendService._bucket(getActivity().getIntent().getExtras()));
                data.put("praxis", "praxis"); //TODO
            } catch (JSONException exc) {
                //should never HAPPEN
                exc.printStackTrace();
            }

            String url = "http://" + Globals.hostHealthCare + ":" + Globals.portHealthCare + "/api/GesundheitsDaten";

            HealthCareServerTrendService.request(requestQueue, url, data,
                    (JSONObject response) -> {
                        Toast.makeText(getActivity(), "Erfolgreich gesendet", Toast.LENGTH_LONG).show();
                        FirestoreFormularService.updateFormularDate(extras.getString("user_uid"));


                        if ( extras.containsKey("atemfrequenzMIN") == false) {
                            welcomeActivity.requestAlarm();
                        }

                        Log.d(Input_VitalParameter.class.getSimpleName() + ".class", "OutOfLimits: " + listOutOfLimits);
                        if (listOutOfLimits.size() > 0) {
                            sendLimitContactPerson(listOutOfLimits);
                        }

                        //inform Trends
                        if (welcomeActivity != null) {
                            welcomeActivity.onDataSendToServer();
                        }
                    }, //
                    (Integer status) -> Toast.makeText(getActivity(), "Send returned " + status, Toast.LENGTH_LONG).show(),
                    (VolleyError error) -> {
                        String text = error.getMessage();
                        Log.e(Input_VitalParameter.class.getSimpleName() + ".class", text == null ? "<null>" : text);
                        Toast.makeText(getActivity(), "Send returned with error", Toast.LENGTH_LONG).show();

                    });

        } else {
            Toast.makeText(getActivity(), errors.get(0).getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private void sendLimitContactPerson(final List<String> listOutOfLimits) {

        final String url = "http://" + Globals.hostHealthCare + ":" + Globals.port2HealthCare;

        Bundle extras = getActivity().getIntent().getExtras();
        final String vorname = extras.getString("patient_vorname");
        final String nachname = extras.getString("patient_name");
        final String kontaktmail = extras.getString("contact_email");


        for (String type : listOutOfLimits) {

            /*
            {
	            "Vorname": "vorame",
                "Name": "name",
	            "Grenzwert": "typ",
	            "Kontaktmail": "email"
            }
            */

            JSONObject data = new JSONObject();
            try {
                data.put("Vorname", vorname);
                data.put("Name", nachname);
                data.put("Grenzwert", type);
                data.put("Kontaktmail", kontaktmail);
            } catch (JSONException e) {
                Log.e(Input_VitalParameter.class.getSimpleName() + ".class", "Exception: " + e.getMessage());
            }

            //
            HealthCareServerTrendService.request(requestQueue, url, data, (JSONObject response) -> {
                    }, //
                    (Integer status) -> Toast.makeText(getActivity(), "Send ValueOutOfLimit returned " + status, Toast.LENGTH_LONG).show(),
                    (VolleyError error) -> {
                        String text = error.getMessage();
                        Log.e(Input_VitalParameter.class.getSimpleName() + ".class", text == null ? "<null>" : text);
                        Toast.makeText(getActivity(), "Send ValueOutOfLimit returned with error", Toast.LENGTH_LONG).show();

                    });

        }
    }

    public void testDisableInputs() {
        String contactEmail = getActivity().getIntent().getStringExtra("contact_email");
        boolean disableInput = (contactEmail == null || contactEmail.trim().length() == 0);

        Log.d("Input_VitalParameter.class", "testDisable: " + disableInput + ",  contact_email is <" + contactEmail + ">");
        root.findViewById(R.id.txtInfo).setVisibility(disableInput ? View.VISIBLE : View.GONE);

        disable(R.id.blutdruck_diastolisch, disableInput);
        disable(R.id.blutdruck_systolisch, disableInput);
        disable(R.id.atemfrequenz, disableInput);
        disable(R.id.puls, disableInput);
        disable(R.id.gewicht, disableInput);
    }

    private void disable(int fieldId, boolean disableInput) {
        root.findViewById(fieldId).setEnabled(!disableInput);
    }
}





