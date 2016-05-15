package com.cmsc436.quickbite;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cmsc436.quickbite.tabbedview.MainActivity;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class LoginActivity extends AppCompatActivity {
    final Firebase fb = new Firebase("https://quick-bite.firebaseio.com/");
    private Button loginButton;

    private EditText etUsername;
    private EditText etPassword;

    private TextView registerLink;

    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.bLogin);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        registerLink = (TextView) findViewById(R.id.etSignUpHere);

        builder = new AlertDialog.Builder(LoginActivity.this, android.R.style.Theme_Material_Dialog_Alert);

        // Sets up register link
        if (registerLink != null) {
            registerLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                    LoginActivity.this.startActivity(registerIntent);
                }
            });
        }
    }

    public void login(View view) {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();


        if (username.length() == 0 || password.length() == 0) {
            builder.setTitle("Login Error");
            builder.setMessage("Please enter both Email and Password")
                    .setNegativeButton("Got it", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }

        // Create a handler to handle the result of the authentication
        final Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                // Authenticated successfully with payload authData
                // For now, sends the user to the TimerActivity
                Intent loggedInIntent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(loggedInIntent);
            }
            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // Authenticated failed with error firebaseError
                // Shows the user an error message
                builder.setTitle("Login Error");
                builder.setMessage("Invalid Email or Password")
                        .setNegativeButton("Got it", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };

        fb.authWithPassword(username, password, authResultHandler);
    }
}
