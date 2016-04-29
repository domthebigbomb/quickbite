package com.cmsc436.quickbite;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import com.cmsc436.quickbite.dataModel.UserVars;
import java.lang.Thread;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener; //Firebase classes
import com.firebase.client.FirebaseError;
import com.firebase.client.DataSnapshot;

public class UserProfileActivity extends AppCompatActivity {

    public static String username = ""; // set by LoginActivity or RegisterActivity
    public static UserVars userVars = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);
        // start thread to create interface
        Thread buildUI = new Thread(new BuildProfile());
        buildUI.start();
    }

    public class BuildProfile implements Runnable {
        @Override
        public void run() {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            // obtain user data
            if (UserProfileActivity.username == "") {
                // cannot load user data
                Log.i("username_unknown", "username unknown! cannot load user data");
                return;
            }
            else {
                Log.i("username_known", "username loaded correctly");
            }


            // trigger onDataChange event. handler implemented in login/register
            //LoginActivity.myFirebaseRef.child(UserProfileActivity.username).setValue(userVars);
        }
    }
}
