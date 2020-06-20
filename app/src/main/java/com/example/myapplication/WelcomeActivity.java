package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.anychart.chart.common.dataentry.DataEntry;
import com.example.myapplication.activities.ContactPersonFragment;
import com.example.myapplication.activities.PatientPersonFragment;
import com.example.myapplication.data.KontaktDto;
import com.example.myapplication.service.FirestoreKontaktService;
import com.example.myapplication.service.HealthCareServerTrendService;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    private Trend trends = null;
    private Input_VitalParameter input = null;

    private final FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment;

            switch (i) {
                default:

                case 0:
                    input = new Input_VitalParameter();
                    fragment = input;
                    break;

                case 1:
                    trends = new Trend();
                    fragment = trends;
                    break;

                case 2:
                    fragment = new ContactPersonFragment();
                    break;

                case 3:
                    fragment = new PatientPersonFragment();
                    break;
            }
            return fragment;
        }
    };

    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().subscribeToTopic("user_"+ this.getIntent().getStringExtra("user_uid"));
        requestQueue = Volley.newRequestQueue(this);

        setWelcomeAlert();
        loadLimitValues();

        setContentView(R.layout.activity_welcome__page);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        Button btLogout = findViewById(R.id.btLogout);
        btLogout.setOnClickListener((View v) -> {
            setResult(0, new Intent());
            finish();
        });

        tabs.getTabAt(0).setIcon(R.mipmap.ic_vital_parameter);
        tabs.getTabAt(1).setIcon(R.mipmap.ic_trend);
        tabs.getTabAt(2).setIcon(R.mipmap.ic_kontakt);
        tabs.getTabAt(3).setIcon(R.mipmap.ic_user);

        tabs.invalidate();
    }

    void setWelcomeAlert() {
        AlertWelcome alert = new AlertWelcome();
        alert.show(getSupportFragmentManager(), "Welcome_Alert");
    }

    void loadLimitValues() {

        final String url = "http://" + Globals.hostHealthCare + ":" + Globals.portHealthCare + "/api/Alarm/" + HealthCareServerTrendService._bucket(getIntent().getExtras());

        HealthCareServerTrendService.request(//
                requestQueue, //
                url,  //
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        final int n = response.length();

                        List<DataEntry> list = new ArrayList<>(n);

                        for (int i = 0; i < n; i++) {

                            String check = null;
                            double min = -1;
                            double max = -1;
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                check = jsonObject.getString("check");
                                min = jsonObject.getDouble("min");
                                max = jsonObject.getDouble("max");
                            } catch (JSONException exc) {

                            }

                            WelcomeActivity.this.getIntent().putExtra(check + "MIN", min);
                            WelcomeActivity.this.getIntent().putExtra(check + "MAX", max);

                        }

                        if (trends != null) {
                            trends.loadLimitValues();
                        }

                    }
                },  //
               (Integer status) -> {
                        Toast.makeText(WelcomeActivity.this, "Load Limit-Values Status " + status, Toast.LENGTH_LONG).show();
                }, //
                (VolleyError error) -> {
                    error.fillInStackTrace().printStackTrace();
                    error.printStackTrace();
                    String text = error.getMessage();
                    Log.e(WelcomeActivity.class.getSimpleName()+".class", text == null ? "<?>" : text);
             //       Toast.makeText(this, "Server unreachable: Could not load Limit-Values", Toast.LENGTH_LONG).show();
                });
    }


    public void onContactEmailChanged() {
        if (input != null) {
            input.testDisableInputs();
        }
    }


    public void onDataSendToServer() {
        if (trends != null) {
            trends.refresh();
        }
    }

}