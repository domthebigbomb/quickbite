package com.cmsc436.quickbite;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import java.lang.Thread;

import com.cmsc436.quickbite.dataModel.UserVars;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    public static Firebase myFirebaseRef = null;
    public boolean newFirebase = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (myFirebaseRef == null) {
            Firebase.setAndroidContext(this);
            LoginActivity.myFirebaseRef = new Firebase("https://quick-bite.firebaseio.com/");
            newFirebase = true;
        }
        // start thread to create interface
        Thread buildUI = new Thread(new BuildLogin());
        buildUI.start();
    }

    public class BuildLogin implements Runnable {

        @Override
        public void run() {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // open UserProfile screen on button click (just for debuggin)
                    Intent startUserProfile = new Intent(LoginActivity.this, UserProfileActivity.class);
                    //myIntent.putExtra("key", value); //Optional parameters
                    LoginActivity.this.startActivity(startUserProfile);
                }
            });

            // ** change to set username variable using user input (text box)
            UserProfileActivity.username = "Jeremy";

            // initialize variables for Firebase interaction. handled here if user has not registered
            // during this session. (a returning user).
            if (newFirebase == true) {

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
                Log.i("correct_check", "login accomplished");
            }
            else {
                Log.i("correct_check", "login successfuly negated");
            }
        }
    }

}
