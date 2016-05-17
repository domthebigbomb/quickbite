package com.cmsc436.quickbite;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.cmsc436.quickbite.slidingtab.ListElements.LocationList;
import com.cmsc436.quickbite.slidingtab.ListElements.RestaurantProfile;
import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class TimerActivity extends AppCompatActivity {
    private Firebase fb = new Firebase("https://quick-bite.firebaseio.com/");
    private boolean isTimerRunning = false;
    private int elapsedTime = 0;
    private Timer timer;
    private EditText waitText;
    private Firebase waitRef;
    private String restaurantID;
    private String restaurantName;
    private String address;

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            updateTimerView();
        }
    };

    protected void updateTimerView() {
        waitText.setText(formatTime(elapsedTime)); //this is the textview
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.print("Timer");
        setContentView(R.layout.activity_timer);
        ImageButton button = (ImageButton) this.findViewById(R.id.resetButton);
        button.setColorFilter(Color.argb(255, 255, 0, 104));

        waitText = (EditText) findViewById(R.id.waitingTime);
        waitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        updateTimerView();

        Bundle bundle = getIntent().getExtras();
        restaurantID = bundle.getString(LocationList.restaurantIDKey);
        restaurantName = bundle.getString(LocationList.restaurantNameKey);
        address = bundle.getString(RestaurantProfile.addressKey);

        waitRef = new Firebase("https://quick-bite.firebaseio.com/").child(restaurantID).child("waitTime");
    }

    protected int[] timeComponents(int seconds) {
        int hours = elapsedTime / 3600;
        int remainder = elapsedTime - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;
        return new int[]{hours, mins, secs};
    }

    protected String formatTime(int seconds) {
        Date d = new Date(seconds * 1000L);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss"); // HH for 0-23
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(d);
    }

    // Reset the textfield back to 0
    public void resetTimer(View view) {
        elapsedTime = 0;
        updateTimerView();
    }

    // Start/stop the timer
    public void toggleTimer(View view) {
        if (isTimerRunning) {
            // Stop
            waitText.setEnabled(true);
            waitText.setFocusable(true);
            timer.cancel();
        } else {
            // Start
            waitText.setEnabled(false);
            waitText.setFocusable(false);

            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    elapsedTime += 1; //increase every sec
                    mHandler.obtainMessage(1).sendToTarget();

                }
            }, 0, 1000);
        }
        isTimerRunning = !isTimerRunning;
    }

    public void showTimePicker() {
        int[] timeComponents = timeComponents(elapsedTime);
        int hour = timeComponents[0];
        int minute = timeComponents[1];
        TimePickerDialog timePicker = new TimePickerDialog(TimerActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                elapsedTime = selectedHour * 3600 + selectedMinute * 60;
                updateTimerView();
            }
        }, hour, minute, true);//Yes 24 hour time
        timePicker.setTitle("Select Time");
        timePicker.show();
    }

    // Submit current time and show next activity
    public void submitTime(View view) {
        waitRef.setValue(elapsedTime);
        User currUser = ((MyApplication) this.getApplication()).getCurrentUser();
        Firebase userRef = fb.child("checkIns").child(currUser.getUid()).push();
        userRef.setValue(new CheckIn(elapsedTime, restaurantName, address));

        Intent biteIntent = new Intent(this, ComposeBiteActivity.class);
        biteIntent.putExtra(LocationList.restaurantIDKey, restaurantID);
        biteIntent.putExtra(LocationList.restaurantNameKey, restaurantName);
        finish();
        startActivity(biteIntent);
    }
}
