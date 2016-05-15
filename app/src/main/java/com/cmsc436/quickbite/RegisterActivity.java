package com.cmsc436.quickbite;

import android.content.Intent;
import android.os.Handler;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_register);

        final Firebase fb = new Firebase("https://quick-bite.firebaseio.com/");

        final Button regButton = (Button) findViewById(R.id.bRegister);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etPasswordConfirm = (EditText) findViewById(R.id.etPasswordConfirm);

        final TextView loginLink = (TextView) findViewById(R.id.etLoginHere);
        final TextView tvIncError = (TextView) findViewById(R.id.tvIncError);

        // Sets up login link
        if (loginLink != null) {
            loginLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent registerIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    //RegisterActivity.this.startActivity(registerIntent);
                    finish();
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
                    String passwordConfirm = etPasswordConfirm.getText().toString();
                    if(username.length() > 0 && password.length() > 0 && passwordConfirm.length() > 0) {

                        if(!password.equals(passwordConfirm)) {
                            // Shows the user an error message
                            // Uses an Animation to fade in/out
                            fadeTextView(tvIncError, TextView.VISIBLE);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    fadeTextView(tvIncError, TextView.INVISIBLE);;
                                }
                            }, 4000);
                            return;
                        }

                        // Creates a new user
                        fb.createUser(username, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                            @Override
                            public void onSuccess(Map<String, Object> result) {
                                System.out.println("Successfully created user account with uid: " + result.get("uid"));
                                // Returns to login page
                                //Intent registerIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                //RegisterActivity.this.startActivity(registerIntent);
                                finish();
                            }
                            @Override
                            public void onError(FirebaseError firebaseError) {
                                // there was an error
                            }
                        });
                    } else {
                    }
                }
            });
        }
    }

    public void fadeTextView(final TextView tv, int fadeTypeIn) {
        final int fadeType = (fadeTypeIn==View.INVISIBLE) ? View.INVISIBLE : View.VISIBLE;
        Animation fadeAnimation = (fadeType==View.INVISIBLE) ? new AlphaAnimation(1f, 0f) : new AlphaAnimation(0f, 1f);
        fadeAnimation.setDuration(1300);
        fadeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                tv.setVisibility(fadeType);
            }
        });
        tv.startAnimation(fadeAnimation);
    }
}
