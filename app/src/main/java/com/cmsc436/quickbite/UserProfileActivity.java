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

public class UserProfileActivity extends AppCompatActivity {

    public static String username = ""; // set by LoginActivity or RegisterActivity
    public static UserVars userVars = null;
    private Toolbar toolbar;
    private TextView usernameTextview = null;
    public static ToggleButton checkIns = null, bites = null, tb = null;
    public Bundle _savedInstanceState = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _savedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        toolbar = (Toolbar) findViewById(R.id.tool_bar_userprofile);
        setSupportActionBar(toolbar); // Setting toolbar as the ActionBar with setSupportActionBar() call

        // obtain user data
        if (UserProfileActivity.username == "") {
            // cannot load user data
            Log.i("username_unknown", "username unknown! cannot load user data");
            return;
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
            Log.i("view_1", "fetching info");

            setSupportActionBar(toolbar); // Setting toolbar as the ActionBar with setSupportActionBar() call
            usernameTextview.setText(UserProfileActivity.username);

            populateUpper();
            //setContentView(R.layout.activity_userprofile_info);

        } else if (index == 2) {
            Log.i("view_2", "fetching check-ins");
            Thread buildUI = new Thread(new BuildUserProfile());
            buildUI.start();
            setContentView(R.layout.activity_userprofile_checkins);
        } else if (index == 3) {
            Log.i("view_3", "fetching bites");
            Thread buildUI = new Thread(new BuildUserProfile());
            buildUI.start();
            setContentView(R.layout.activity_userprofile_bites);
        } else {
            return false;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

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
        }
    }
}