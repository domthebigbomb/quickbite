package com.cmsc436.quickbite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmsc436.quickbite.tabbedview.MainActivity;

import java.util.ArrayList;

/**
 * Created by dominic on 5/16/16.
 */
public class DrawerActivity extends AppCompatActivity {
    // Drawer parameters
    public ListView mDrawerList;
    public RelativeLayout mDrawerPane;
    public TextView dUserName;
    public ActionBarDrawerToggle mDrawerToggle;
    public DrawerLayout mDrawerLayout;
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    MyApplication app;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceBundle);
        app = (MyApplication) this.getApplication();
    }

    public void updateDrawerUser() {
        String firstName = prefs.getString(User.firstNameKey, "first");
        String lastName = prefs.getString(User.lastNameKey, "last");
        if (firstName != null && lastName != null) {
            dUserName.setText(firstName + " " + lastName);
        }
    }

    protected void configureDrawer() {
        // Hamburger icon support
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Drawer items
        mNavItems.add(new NavItem("Check In", R.drawable.ic_action_place));
        mNavItems.add(new NavItem("Favorites", R.drawable.ic_action_important));
        mNavItems.add(new NavItem("Nearby", R.drawable.ic_action_location_found));
        mNavItems.add(new NavItem("Search", R.drawable.ic_action_search));
        mNavItems.add(new NavItem("My Profile", R.drawable.ic_action_person));
        mNavItems.add(new NavItem("Logout", R.drawable.ic_power_off));

        DrawerListAdapter drawer_adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(drawer_adapter);

        // Updates drawer username
        updateDrawerUser();

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
                if (position == 4) {
                    Intent profileIntent = new Intent(DrawerActivity.this, UserProfileActivity.class);
                    DrawerActivity.this.startActivity(profileIntent);
                } else if (position == 5) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("logged-in","false");
                    editor.apply();
                    editor.remove(User.uidKey);
                    editor.remove(User.firstNameKey);
                    editor.remove(User.lastNameKey);
                    editor.apply();

                    app.setUser(null);

                    Intent logoutIntent = new Intent(DrawerActivity.this, LoginActivity.class);
                    DrawerActivity.this.startActivity(logoutIntent);
                    mDrawerLayout.closeDrawers();
                }
            }
        });
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
            } else {
                view = convertView;
            }

            TextView titleView = (TextView) view.findViewById(R.id.title);
            ImageView iconView = (ImageView) view.findViewById(R.id.icon);

            titleView.setText(mNavItems.get(position).mTitle);
            iconView.setImageResource(mNavItems.get(position).mIcon);

            return view;
        }
    }
}
