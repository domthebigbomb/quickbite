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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    final Firebase fb = new Firebase("https://quick-bite.firebaseio.com/");

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    FrameLayout progressBarHolder;

    Button registerButton;
    Button showLoginButton;

    private EditText etUsername;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    private EditText etFirstName;
    private EditText etLastName;

    private AlertDialog.Builder builder;
    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        registerButton = (Button) findViewById(R.id.bRegister);
        showLoginButton = (Button) findViewById(R.id.bShowLogin);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordConfirm = (EditText) findViewById(R.id.etPasswordConfirm);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);

        builder = new AlertDialog.Builder(RegisterActivity.this, android.R.style.Theme_Material_Dialog_Alert);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }

    private void enableRegisterViews(boolean enabled) {
        registerButton.setEnabled(enabled);
        showLoginButton.setEnabled(enabled);
        etUsername.setEnabled(enabled);
        etPassword.setEnabled(enabled);
        etPasswordConfirm.setEnabled(enabled);
    }

    private void showProgressBar(boolean isShowingProgress) {
        if (isShowingProgress) {
            enableRegisterViews(false);
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        } else {
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            enableRegisterViews(true);
        }
    }

    public void register(View view) {
        // Checks user/pass bounds
        final String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String passwordConfirm = etPasswordConfirm.getText().toString();
        final String firstName = etFirstName.getText().toString();
        final String lastName = etLastName.getText().toString();

        if(username.length() > 0 && password.length() > 0 && passwordConfirm.length() > 0
                && firstName.length() > 0 && lastName.length() > 0) {
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

            showProgressBar(true);
            // Creates a new user
            fb.createUser(username, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> result) {
                    String uid = result.get("uid").toString();
                    //System.out.println("Successfully created user account with uid: " + result.get("uid"));
                    // Returns to login page
                    fb.child("users").child(uid).setValue( new User(uid, firstName, lastName));
                    showProgressBar(false);
                    Intent intent = new Intent();
                    intent.putExtra(LoginActivity.emailKey, username);
                    setResult(RESULT_OK, intent);
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
                                    showProgressBar(false);
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

    public void showLogin(View view) {
        finish();
    }
}
