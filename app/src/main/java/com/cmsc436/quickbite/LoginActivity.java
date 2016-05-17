package com.cmsc436.quickbite;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cmsc436.quickbite.tabbedview.MainActivity;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    static String emailKey = "email";
    final Firebase fb = new Firebase("https://quick-bite.firebaseio.com/");

    private Button loginButton;
    private Button showRegisterButton;

    private EditText etUsername;
    private EditText etPassword;

    private AlertDialog.Builder builder;

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    FrameLayout progressBarHolder;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        loginButton = (Button) findViewById(R.id.bLogin);
        showRegisterButton = (Button) findViewById(R.id.bShowRegister);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        builder = new AlertDialog.Builder(LoginActivity.this, android.R.style.Theme_Material_Dialog_Alert);
    }

    private void enableLoginViews(boolean enabled) {
        loginButton.setEnabled(enabled);
        showRegisterButton.setEnabled(enabled);
        etUsername.setEnabled(enabled);
        etPassword.setEnabled(enabled);
    }

    private void showProgressBar(boolean isShowingProgress) {
        if (isShowingProgress) {
            enableLoginViews(false);
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        } else {
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            enableLoginViews(true);
        }
    }

    public void login(View view) {
        final String username = etUsername.getText().toString();
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

        // Updates the current user
        ((MyApplication) this.getApplication()).setUsername(username);

        // Create a handler to handle the result of the authentication
        final Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                // Authenticated successfully with payload authData
                // Sends the user to the MainActivity
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("logged-in","true");
                editor.apply();
                editor.putString("curr-user",username);
                editor.apply();
                showProgressBar(false);
                finish();
                //Intent loggedInIntent = new Intent(LoginActivity.this, MainActivity.class);
                //LoginActivity.this.startActivity(loggedInIntent);
            }
            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // Authenticated failed with error firebaseError
                // Shows the user an error message
                builder.setTitle("Login Error");
                builder.setMessage(firebaseError.getMessage())
                        .setNegativeButton("Got it", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                showProgressBar(false);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };

        showProgressBar(true);
        fb.authWithPassword(username, password, authResultHandler);
    }

    public void showRegistration(View view) {
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        LoginActivity.this.startActivityForResult(registerIntent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String newEmail =data.getStringExtra(LoginActivity.emailKey);
                etUsername.setText(newEmail);
            }
        }
    }
}
