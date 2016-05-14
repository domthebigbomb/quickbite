package com.cmsc436.quickbite;

import android.graphics.Color;
import android.media.Image;
import android.os.SystemClock;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.client.Firebase;

public class ComposeBiteActivity extends AppCompatActivity {
    EditText reviewText;
    TextView charCountView;
    Button postButton;
    int sentimentIndex = 3;
    Firebase restaurantRef;
    String author = "Dom";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_bite);

        // Replace with restaurant id
        restaurantRef = new Firebase("https://quick-bite.firebaseio.com/");//.child("bites").child("1");
        reviewText = (EditText) findViewById(R.id.review_text);
        reviewText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateCharacterCount();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        charCountView = (TextView) findViewById(R.id.character_count);
        postButton = (Button) findViewById(R.id.post_button);

        updateSentiments();
    }

    protected void updateCharacterCount() {
        int charRemaining = 140 - reviewText.getText().length();
        String newText = charRemaining + "";
        if (charRemaining < 20) {
            charCountView.setTextColor(Color.RED);
        } else {
            charCountView.setTextColor(0xff9B9B9B);
        }
        setPostEnabled(charRemaining >= 0);
        charCountView.setText(newText);
    }

    protected void setPostEnabled(boolean isEnabled) {
        postButton.setEnabled(isEnabled);
        if (isEnabled) {
            postButton.setTextColor(Color.WHITE);
            postButton.setBackgroundColor(0xff00B9E6);
        } else {
            postButton.setTextColor(0xff9B9B9B);
            postButton.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    protected void updateSentiments() {
        int[] sentimentIDs = {R.id.rate_very_dissatisfied, R.id.rate_dissatisfied,
                R.id.rate_neutral, R.id.rate_satisfied, R.id.rate_very_satisfied};
        int[] graySentiments = {R.drawable.qb_gray_very_dissatisfied,
                R.drawable.qb_gray_dissatisfied, R.drawable.qb_gray_neutral,
                R.drawable.qb_gray_satisfied, R.drawable.qb_gray_very_satisfied};
        int[] coloredSentiments = {R.drawable.qb_very_dissastisfied,
                R.drawable.qb_dissatisfied, R.drawable.qb_neutral,
                R.drawable.qb_satisfied, R.drawable.qb_very_satisfied};
        String[] sentimentStrings = {"Very Dissatisfied", "Dissatisfied",
                "Neutral", "Satisfied", "Very Satisfied"};
        for (int i = 0; i < sentimentIDs.length; i++) {
            ImageButton button = (ImageButton) findViewById(sentimentIDs[i]);
            if (sentimentIndex == i) {
                TextView sentimentName = (TextView) findViewById(R.id.sentiment_name);
                sentimentName.setText(sentimentStrings[i]);
                button.setImageResource(coloredSentiments[i]);
            } else {
                button.setImageResource(graySentiments[i]);
            }
        }
    }

    protected void selectVeryDissatisfied(View view) {
        sentimentIndex = 0;
        updateSentiments();
    }

    protected void selectDissatisfied(View view) {
        sentimentIndex = 1;
        updateSentiments();
    }

    protected void selectNeutral(View view) {
        sentimentIndex = 2;
        updateSentiments();
    }

    protected void selectSatisfied(View view) {
        sentimentIndex = 3;
        updateSentiments();
    }

    protected void selectVerySatisfied(View view) {
        sentimentIndex = 4;
        updateSentiments();
    }
    protected void submitBite(View view) {
        Firebase biteRef = restaurantRef.push();
        Bite bite = new Bite(System.currentTimeMillis(), author, reviewText.getText().toString(), sentimentIndex + 1);
        biteRef.setValue("Test");
        Log.d("Dom", "Sent bite");

    }
}
