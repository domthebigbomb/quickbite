package com.cmsc436.quickbite;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import com.cmsc436.quickbite.dataModel.UserVars;
import com.cmsc436.quickbite.fragments.BitesFragment_UP;
import com.cmsc436.quickbite.fragments.CheckInsFragment_UP;
import com.cmsc436.quickbite.fragments.InfoFragment_UP;
import java.lang.Thread;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener; //Firebase classes
import com.firebase.client.FirebaseError;
import com.firebase.client.DataSnapshot;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import 	android.app.FragmentManager;
import android.app.FragmentTransaction;

public class UserProfileActivity extends AppCompatActivity {

    public static String username = ""; // set by LoginActivity or RegisterActivity
    public static UserVars userVars = null;
    private Toolbar toolbar;
    private TextView usernameTextview = null;
    public static ToggleButton checkIns = null, bites = null, tb = null;
    public Bundle _savedInstanceState = null;
    public static Fragment currLowerFragment = null;
    public static int currLowerFragmentIndex = 0;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _savedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        toolbar = (Toolbar) findViewById(R.id.tool_bar_userprofile);
        setSupportActionBar(toolbar); // Setting toolbar as the ActionBar with setSupportActionBar() call
        getSupportActionBar().setTitle("");
        // Gets username
        user = ((MyApplication) this.getApplication()).getCurrentUser();
        if (user != null) {
            UserProfileActivity.username = user.fullName();
        }

        // obtain user data
        if (UserProfileActivity.username == null || UserProfileActivity.username.equals("")) {
            // cannot load user data
            Log.i("username_unknown", "username unknown! cannot load user data");
            UserProfileActivity.username="Username";
        } else {
            Log.i("username_known", "username loaded correctly");
        }

        usernameTextview = (TextView) findViewById(R.id.username_userprofile);
        usernameTextview.setText(UserProfileActivity.username);

        // start thread to create interface
        Thread buildUI = new Thread(new BuildUserProfile());
        buildUI.start();
    }

    public boolean populateLowerScreen(int index) {
        if (index == 1) {
            if (currLowerFragmentIndex != 1) {
                Log.i("view_1", "fetching info");

                setSupportActionBar(toolbar); // Setting toolbar as the ActionBar with setSupportActionBar() call
                usernameTextview.setText(UserProfileActivity.username);

                // add Info fragment
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                InfoFragment_UP fragment = new InfoFragment_UP();
                if (currLowerFragment != null) {
                    fragmentTransaction.remove(currLowerFragment);
                }
                fragmentTransaction.replace(R.id.profile_lowerfragment, fragment);
                fragmentTransaction.commit();

                currLowerFragment = fragment;
            }
            else {
                Log.i("FragmentExists", "Lower fragment already set to Info");
            }
            currLowerFragmentIndex = index;

        } else if (index == 2) {
            if (currLowerFragmentIndex != 2) {
                Log.i("view_2", "fetching check-ins");

                // add CheckIns fragment
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                CheckInsFragment_UP fragment = new CheckInsFragment_UP();
                if (currLowerFragment != null) {
                    fragmentTransaction.remove(currLowerFragment);
                }

                fragmentTransaction.replace(R.id.profile_lowerfragment, fragment);
                fragmentTransaction.commit();
                currLowerFragment = fragment;
            }
            else {
                Log.i("FragmentExists", "Lower fragment already set to CheckIn");
            }
            currLowerFragmentIndex = index;

        } else if (index == 3) {
            if (currLowerFragmentIndex != 3) {
                Log.i("view_3", "fetching bites");

                // add Bites fragment
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                BitesFragment_UP fragment = new BitesFragment_UP();
                if (currLowerFragment != null) {
                    fragmentTransaction.remove(currLowerFragment);
                }

                fragmentTransaction.replace(R.id.profile_lowerfragment, fragment);
                fragmentTransaction.commit();

                currLowerFragment = fragment;
            } else {
                Log.i("FragmentExists", "Lower fragment already set to Bites");
            }
            currLowerFragmentIndex = index;

        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    public void populateUpper() {
        // setup ToggleButtons for switching sub-views
        tb.setText("Info");
        tb.setTextOn("Info");
        tb.setTextOff("Info");

        checkIns.setText("Check-Ins");
        checkIns.setTextOn("Check-Ins");
        checkIns.setTextOff("Check-Ins");

        bites.setText("Bites");
        bites.setTextOn("Bites");
        bites.setTextOff("Bites");

        populateLowerScreen(1);

        // if any of the ToggleButtons are clicked, toggle the other ones to off
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIns.setChecked(false);
                bites.setChecked(false);
                populateLowerScreen(1);
            }
        });
        checkIns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tb.setChecked(false);
                bites.setChecked(false);
                populateLowerScreen(2);
            }
        });
        bites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tb.setChecked(false);
                checkIns.setChecked(false);
                populateLowerScreen(3);
            }
        });
    }

    public class BuildUserProfile implements Runnable {

        @Override
        public void run() {
            tb = (ToggleButton) findViewById(R.id.userprofile_info);
            checkIns = (ToggleButton) findViewById(R.id.userprofile_checkins);
            bites = (ToggleButton) findViewById(R.id.userprofile_bites);
            populateUpper();
            populateLowerScreen(1); //default to info fragment
        }
    }
}