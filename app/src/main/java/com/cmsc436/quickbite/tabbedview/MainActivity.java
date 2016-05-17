package com.cmsc436.quickbite.tabbedview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmsc436.quickbite.DrawerActivity;
import com.cmsc436.quickbite.LoginActivity;
import com.cmsc436.quickbite.MyApplication;
import com.cmsc436.quickbite.R;
import com.cmsc436.quickbite.User;
import com.cmsc436.quickbite.slidingtab.slider.SlidingTabLayout;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends DrawerActivity {
    private Firebase fb = new Firebase("https://quick-bite.firebaseio.com/");

    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    private CharSequence Titles[] = {"List", "Map"};
    int numOfTabs = 2;

    SharedPreferences prefs;

    private MyApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        app = (MyApplication) this.getApplication();

        // Checks if user is logged-in
        if(prefs.getString("logged-in","false").equals("false")) {
            Intent logInIntent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(logInIntent);
        } else {
            String uid = prefs.getString(User.uidKey, "uid");
            fb.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User existingUser = dataSnapshot.getValue(User.class);
                    app.setUser(existingUser);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Intent logInIntent = new Intent(MainActivity.this, LoginActivity.class);
                    MainActivity.this.startActivity(logInIntent);
                }
            });
        }

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
    protected void onResume() {
        super.onResume();
        updateDrawerUser();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
}