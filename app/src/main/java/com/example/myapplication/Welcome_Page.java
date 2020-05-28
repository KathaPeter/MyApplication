package com.example.myapplication;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class Welcome_Page extends AppCompatActivity  {


    private final PagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment;

            switch(i){
                default:
                case 0: fragment = new Input_VitalParameter();
                    break;

                case 1: fragment = new Trend();
                    break;

                case 2: fragment = new ContactPerson();
                    break;
            }
            return fragment;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setWelcomeAlert();

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