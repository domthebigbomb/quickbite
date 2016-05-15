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

import com.cmsc436.quickbite.tabbedview.MainActivity;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Firebase fb = new Firebase("https://quick-bite.firebaseio.com/");

        final Button loginButton = (Button) findViewById(R.id.bLogin);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);

        final TextView registerLink = (TextView) findViewById(R.id.etSignUpHere);
        final TextView tvIncError = (TextView) findViewById(R.id.tvIncError);

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
                // Uses an Animation to fade in/out
                fadeTextView(tvIncError, TextView.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fadeTextView(tvIncError, TextView.INVISIBLE);;
                    }
                }, 4000);
            }
        };

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

        // Sets up register button
        if (loginButton != null) {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Authenticates the user
                    String username = etUsername.getText().toString();
                    String password = etPassword.getText().toString();
                    fb.authWithPassword(username, password, authResultHandler);
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
