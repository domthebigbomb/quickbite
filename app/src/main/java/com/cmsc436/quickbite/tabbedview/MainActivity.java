package com.cmsc436.quickbite.tabbedview;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.cmsc436.quickbite.R;
import com.cmsc436.quickbite.slidingtab.slider.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    private CharSequence Titles[] = {"List", "Map"};
    int numOfTabs = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().setElevation(0);
//        setSupportActionBar(toolbar);
//
//
//        getSupportActionBar().setTitle("Restaurants");


        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles,
                numOfTabs);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setViewPager(pager);


    }





}