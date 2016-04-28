package com.cmsc436.quickbite;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.cmsc436.quickbite.dataModel.UserVars;
import com.firebase.client.ValueEventListener; //Firebase classes
import com.firebase.client.FirebaseError;
import com.firebase.client.DataSnapshot;

public class UserProfileActivity extends AppCompatActivity {

    public static String username = ""; // set by LoginActivity or RegisterActivity
    public UserVars userVars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

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
        if (username == "") {
            // cannot load user data
            System.out.println("username unknown! cannot load user data");
            return;
        }

        // we can associate a username (key) with a data object
        LoginActivity.myFirebaseRef.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userVars = (UserVars)snapshot.getValue(); // set data reference
            }
            @Override public void onCancelled(FirebaseError error) { }
        });

        // example of setting object later on after updates to any fields
        //  LoginActivity.myFirebaseRef.child(username).setValue(userVars);
    }
}
