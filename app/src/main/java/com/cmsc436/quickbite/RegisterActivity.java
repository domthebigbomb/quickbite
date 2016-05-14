package com.cmsc436.quickbite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_register);

        final Firebase fb = new Firebase("https://quick-bite.firebaseio.com/");

        final Button regButton = (Button) findViewById(R.id.bRegister);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);

        final TextView loginLink = (TextView) findViewById(R.id.etLoginHere);

        // Sets up login link
        if (loginLink != null) {
            loginLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent registerIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    RegisterActivity.this.startActivity(registerIntent);
                }
            });
        }

        // Sets up register button
        if (regButton != null) {
            regButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Checks user/pass bounds
                    String username = etUsername.getText().toString();
                    String password = etPassword.getText().toString();
                    if(username.length() > 0 && password.length() > 0) {
                        // Creates a new user
                        fb.createUser(username, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                            @Override
                            public void onSuccess(Map<String, Object> result) {
                                System.out.println("Successfully created user account with uid: " + result.get("uid"));
                                // Returns to login page
                                Intent registerIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                RegisterActivity.this.startActivity(registerIntent);
                            }
                            @Override
                            public void onError(FirebaseError firebaseError) {
                                // there was an error
                            }
                        });
                    } else {
                        // Have something fade in&out that explains user/pass bounds
                    }
                }
            });
        }
    }
}
