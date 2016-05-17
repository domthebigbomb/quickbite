package com.cmsc436.quickbite.tabbedview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmsc436.quickbite.DrawerActivity;
import com.cmsc436.quickbite.LoginActivity;
import com.cmsc436.quickbite.MyApplication;
import com.cmsc436.quickbite.R;
import com.cmsc436.quickbite.TimerActivity;
import com.cmsc436.quickbite.UserProfileActivity;
import com.cmsc436.quickbite.slidingtab.slider.SlidingTabLayout;
import com.firebase.client.Firebase;

import java.util.ArrayList;

public class MainActivity extends DrawerActivity {
    //final Firebase fb = new Firebase("https://quick-bite.firebaseio.com/");

    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    private CharSequence Titles[] = {"List", "Map"};
    int numOfTabs = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        dUserName = (TextView) findViewById(R.id.userName);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        configureDrawer();
        getSupportActionBar().setElevation(0);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles,
                numOfTabs);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setViewPager(pager);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
}