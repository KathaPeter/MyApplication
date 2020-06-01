package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.data.PatientDto;
import com.example.myapplication.service.FirestorePatientService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;

import static com.example.myapplication.service.FirestorePatientService.getPatientData;

public class WelcomeActivity extends AppCompatActivity {

    private final PagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment;

            switch (i) {
                default:
                case 0:
                    fragment = new Input_VitalParameter();
                    break;

                case 1:
                    fragment = new Trend();
                    break;

                case 2:
                    fragment = new ContactPerson();
                    break;
            }
            return fragment;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Task<DocumentSnapshot> getPatientDataTask = getPatientData(this.getIntent().getStringExtra("user_uid"));
        getPatientDataTask.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                PatientDto patientDto = documentSnapshot.toObject(PatientDto.class);
                Log.d(FirestorePatientService.class.toString(), patientDto.getName() + " was loaded");
                getIntent().putExtra("patient_name", patientDto.getName());
                getIntent().putExtra("patient_vorname", patientDto.getVorname());
                setWelcomeAlert();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(WelcomeActivity.class.toString(), "getPatientDataTask:failure", e);
            }
        });


        setContentView(R.layout.activity_welcome__page);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        for (int i = 0; i < tabs.getTabCount(); i++) {
            tabs.getTabAt(i).setIcon(R.mipmap.ic_launcher);  //TODO set Icon
        }
    }

    void setWelcomeAlert() {
        Alert_WelcomePage alert = new Alert_WelcomePage();
        alert.show(getSupportFragmentManager(), "Welcome_Alert");
    }

}