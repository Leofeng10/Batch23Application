package com.example.GoogleCalendar;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.GoogleCalendar.R;
import com.example.GoogleCalendar.navigation.Fragment.Month;
import com.example.GoogleCalendar.navigation.Fragment.Today;
import com.example.GoogleCalendar.navigation.Fragment.ViewPagerAdapter;
import com.example.GoogleCalendar.navigation.Fragment.Week;
import com.google.android.material.tabs.TabLayout;

public class NavigationActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_fragment);

        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.viewPager);

        getTab();



    }

    public void getTab(){
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                viewPagerAdapter.addFragment(Today.getInstance(), "Today");
                viewPagerAdapter.addFragment(Week.getInstance(), "Week");
                viewPagerAdapter.addFragment(Month.getInstance(), "Month");

                viewPager.setAdapter(viewPagerAdapter);

                tabLayout.setupWithViewPager(viewPager);
            }
        });


    }
}