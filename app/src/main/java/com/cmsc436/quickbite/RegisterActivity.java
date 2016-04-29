package com.cmsc436.quickbite;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.content.Intent;

import com.cmsc436.quickbite.dataModel.UserVars;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open Login screen on button click (just for debuggin)
                Intent startLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                RegisterActivity.this.startActivity(startLogin);
            }
        });



        // ^ Above code responds to existing button. Completely arbitrary.

        // ** change to set username variable using user input (text box)
        UserProfileActivity.username = "Jeremy";

        // initialize variables for Firebase interaction (if done here, will not be handled in LoginActivity)
        Firebase.setAndroidContext(this);
        LoginActivity.myFirebaseRef = new Firebase("https://quick-bite.firebaseio.com/");

        // we can associate a username (key) with a data object
        LoginActivity.myFirebaseRef.child(UserProfileActivity.username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // access user data using static field in UserProfileActivity
                UserProfileActivity.userVars = snapshot.getValue(UserVars.class); // set data reference

                // info log to display messages in logcat
                Log.i("username_tag", UserProfileActivity.userVars.username);
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });

        // simulate setting data value for userVars. rank starts at 0, as well as points.
        UserVars vars = new UserVars(UserProfileActivity.username, 0, 0);
        // trigger onDataChange event. handler implemented above
        LoginActivity.myFirebaseRef.child(UserProfileActivity.username).setValue(vars);
    }
}
