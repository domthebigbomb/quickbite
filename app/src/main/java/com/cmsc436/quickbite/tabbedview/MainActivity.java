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

import com.cmsc436.quickbite.LoginActivity;
import com.cmsc436.quickbite.MyApplication;
import com.cmsc436.quickbite.R;
import com.cmsc436.quickbite.TimerActivity;
import com.cmsc436.quickbite.UserProfileActivity;
import com.cmsc436.quickbite.slidingtab.slider.SlidingTabLayout;
import com.firebase.client.Firebase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //final Firebase fb = new Firebase("https://quick-bite.firebaseio.com/");

    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    private CharSequence Titles[] = {"List", "Map"};
    int numOfTabs = 2;

    // Drawer parameters
    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    TextView dUserName;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hamburger icon support
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Drawer items
        mNavItems.add(new NavItem("Check In", R.drawable.ic_action_place));
        mNavItems.add(new NavItem("Favorites", R.drawable.ic_action_important));
        mNavItems.add(new NavItem("Nearby", R.drawable.ic_action_location_found));
        mNavItems.add(new NavItem("Search", R.drawable.ic_action_search));
        mNavItems.add(new NavItem("My Profile", R.drawable.ic_action_person));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Populate the Navigation Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter drawer_adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(drawer_adapter);

        // Updates drawer username
        String curr_user = ((MyApplication) this.getApplication()).getUsername();
        dUserName = (TextView) findViewById(R.id.userName);
        if(curr_user != null) {
            dUserName.setText(curr_user);
        }

        // Implements nav-bar open/close listener
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                invalidateOptionsMenu();
            }
        };
        // Adds the listener
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        // Removes the ListView dividers
        mDrawerList.setDivider(null);

        // Drawer Item click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Handles My Profile
                if(position==4) {
                    Intent profileIntent = new Intent(MainActivity.this, UserProfileActivity.class);
                    MainActivity.this.startActivity(profileIntent);
                }
            }
        });

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    /*
    // Called when invalidateOptionsMenu() is invoked
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
    */

    class NavItem {
        String mTitle;
        int mIcon;

        public NavItem(String title, int icon) {
            mTitle = title;
            mIcon = icon;
        }
    }

    class DrawerListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mNavItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.drawer_item, null);
            }
            else {
                view = convertView;
            }

            TextView titleView = (TextView) view.findViewById(R.id.title);
            ImageView iconView = (ImageView) view.findViewById(R.id.icon);

            titleView.setText( mNavItems.get(position).mTitle );
            iconView.setImageResource(mNavItems.get(position).mIcon);

            return view;
        }
    }
}