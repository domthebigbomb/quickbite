package com.cmsc436.quickbite.slidingtab.ListElements;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmsc436.quickbite.Bite;
import com.cmsc436.quickbite.R;
import com.cmsc436.quickbite.TimerActivity;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Response;

//import com.cmsc436.quickbite.tabbedview.R;

public class RestaurantProfile extends AppCompatActivity {

    String consumerKey = "0YxBV-Axpu7Z0XD2pp91jg";
    String consumerSecret = "6GRuyklo0qJcODC9vuUjYo2uvVg";
    String token = "QLWLjZ0_7ltM0TuumUk8U2m6rcsNYXQy";
    String tokenSecret = "gE6sgS-8x5C3K1ttW8LsQTvbdQ0";
    YelpAPIFactory apiFactory = new YelpAPIFactory(consumerKey, consumerSecret, token, tokenSecret);
    YelpAPI yelpAPI = apiFactory.createAPI();
    Response<Business> response;
    private String restaurantID;
    private String restaurantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_profile);

        Bundle bundle = getIntent().getExtras();
        restaurantID = bundle.getString(LocationList.restaurantIDKey);
        restaurantName = bundle.getString(LocationList.restaurantNameKey);

        getSupportActionBar().setTitle(restaurantName);
        TextView name = (TextView)findViewById(R.id.name);
        name.setText(restaurantName);

        /*Get average service rating from intent here. Hardcoding it for now*/
        int serviceRating = 3;
        TextView service = (TextView)findViewById(R.id.service);
        if(serviceRating == 1){
            service.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.qb_gray_very_dissatisfied), null, null, null);
            service.setText("Service is terrible");
        }else if(serviceRating == 2){
            service.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.qb_gray_dissatisfied), null, null, null);
            service.setText("Service is bad");
        }else if(serviceRating == 3){
            service.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.qb_gray_neutral), null, null, null);
            service.setText("Service is ok");
        }else if(serviceRating == 4){
            service.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.qb_gray_satisfied), null, null, null);
            service.setText("Service is good");
        }else if(serviceRating == 5){
            service.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.qb_gray_satisfied), null, null, null);
            service.setText("Service is awesome!");
        }else{
            service.setText("No service ratings yet!");
        }

        /*Get wait time from intent here. Hardcoding for now.*/
        int waitTime = 8;
        TextView txt = (TextView)findViewById(R.id.waitTime);
        if(waitTime <= 5){
            txt.setText("Wait time is very short");
        }else if(waitTime <= 10){
            txt.setText("Wait time is short");
        }else if(waitTime <= 15){
            txt.setText("Wait time is ok");
        }else if(waitTime <= 20){
            txt.setText("Wait time is long");
        }else if(waitTime <= 25){
            txt.setText("Wait time is very long");
        }else{
            txt.setText("Wait time not reported");
        }

        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        //CardView card1 = (CardView) findViewById(R.id.card1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(layoutManager);
        Bite b1 = new Bite(30000, "Liana Alvarez", "This place sucks", 3);
        Bite b2 = new Bite(294190, "David Greene", "I like chipotle", 5);
        ArrayList<Bite> biteData = new ArrayList<Bite>();

        biteData.add(b1);
        biteData.add(b2);

        RVAdapter adapter = new RVAdapter(biteData);
        rv.setAdapter(adapter);

        //needs to be in an asycn task to avoid NetworkOnMainThreadException
        new GetBusinessData().execute(restaurantID);


    }

    private class GetBusinessData extends AsyncTask<String, Void, Response<Business>> {

        @Override
        protected Response<Business> doInBackground(String... params) {
            Call<Business> call = yelpAPI.getBusiness(params[0]);
            try {
                response = call.execute();
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
        protected void onPostExecute(Response<Business> result) {
            TextView address = (TextView)findViewById(R.id.address);
            List a = result.body().location().displayAddress();
            address.setText((String)a.get(0));
            //address.append((String) a.get(1));
            TextView phone = (TextView)findViewById(R.id.phone);
            phone.setText(result.body().displayPhone());
            String imageURL = result.body().imageUrl();
            //changing image from "ms.jpg" to "l.jpg" so that we get a higher resolution image & dont have to crop/zoom in as much.
            String largeimageURL = imageURL.substring(0, imageURL.lastIndexOf("ms.jpg")) + "l.jpg";
            new getImage().execute(largeimageURL);
        }
    }


    private class getImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
            ImageView imageView = (ImageView)findViewById(R.id.image);
            imageView.setImageBitmap(result);
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }
    }

    public void checkIn(View view) {
        Intent checkInIntent = new Intent(this, TimerActivity.class);
        checkInIntent.putExtra(LocationList.restaurantIDKey, restaurantID);
        checkInIntent.putExtra(LocationList.restaurantNameKey, restaurantName);
        startActivity(checkInIntent);
    }
}
