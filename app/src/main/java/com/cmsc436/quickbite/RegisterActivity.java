package com.cmsc436.quickbite;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    final Firebase fb = new Firebase("https://quick-bite.firebaseio.com/");

    private EditText etUsername;
    private EditText etPassword;
    private EditText etPasswordConfirm;

    private TextView loginLink;

    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordConfirm = (EditText) findViewById(R.id.etPasswordConfirm);

        loginLink = (TextView) findViewById(R.id.etLoginHere);

        builder = new AlertDialog.Builder(RegisterActivity.this, android.R.style.Theme_Material_Dialog_Alert);

        // Sets up login link
        if (loginLink != null) {
            loginLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    public void register(View view) {
        // Checks user/pass bounds
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String passwordConfirm = etPasswordConfirm.getText().toString();
        if(username.length() > 0 && password.length() > 0 && passwordConfirm.length() > 0) {

            if(!password.equals(passwordConfirm)) {
                // Shows the user an error message
                // Uses an Animation to fade in/out
                builder.setTitle("Registration Error");
                builder.setMessage("Passwords do not match")
                        .setNegativeButton("Got it", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                return;
            }

            // Creates a new user
            fb.createUser(username, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> result) {
                    System.out.println("Successfully created user account with uid: " + result.get("uid"));
                    // Returns to login page
                    finish();
                }
                @Override
                public void onError(FirebaseError firebaseError) {
                    // there was an error
                    builder.setTitle("Registration Error");
                    builder.setMessage(firebaseError.getMessage())
                            .setNegativeButton("Got it", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        } else {
            builder.setTitle("Registration Error");
            builder.setMessage("Please fill out all fields")
                    .setNegativeButton("Got it", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
