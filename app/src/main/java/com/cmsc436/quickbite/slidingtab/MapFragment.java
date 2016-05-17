package com.cmsc436.quickbite.slidingtab;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cmsc436.quickbite.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import retrofit.Call;
import retrofit.Response;

//import com.jacobroeland.tabbedview.R;




/**
 * Created by jacobroeland on 4/7/16.
 */
public class MapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private GoogleMap googleMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    double longitudeGPS, latitudeGPS;
    LocationManager locationManager;
    Location userLocation = new Location("");
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    String consumerKey = "0YxBV-Axpu7Z0XD2pp91jg";
    String consumerSecret = "6GRuyklo0qJcODC9vuUjYo2uvVg";
    String token = "QLWLjZ0_7ltM0TuumUk8U2m6rcsNYXQy";
    String tokenSecret = "gE6sgS-8x5C3K1ttW8LsQTvbdQ0";
    YelpAPIFactory apiFactory = new YelpAPIFactory(consumerKey, consumerSecret, token, tokenSecret);
    YelpAPI yelpAPI = apiFactory.createAPI();
    Map<String, String> params = new HashMap<>();
    ArrayList<Business> nearbyLocations = new ArrayList<Business>();
    Firebase fb;



    private class GetYelpData extends AsyncTask<Call<SearchResponse>, Void, ArrayList<Business>> {

        @Override
        protected ArrayList<Business> doInBackground(Call<SearchResponse>... params) {

            try {
                Response<SearchResponse> response = params[0].execute();
                return response.body().businesses();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maps, container,
                false);


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        return v;
    }



    public void setUpMap() {

        SupportMapFragment mSupportMapFragment;
        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);


        if (mSupportMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.maps, mSupportMapFragment).commit();
        }

        if (mSupportMapFragment != null) {
            mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap googleMap) {
                    if (googleMap != null) {

                        googleMap.getUiSettings().setAllGesturesEnabled(true);
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    LOCATION_PERMISSION_REQUEST_CODE);

                        }
                        googleMap.setMyLocationEnabled(true);
                        Toast.makeText(getContext(), userLocation.toString(), Toast.LENGTH_SHORT).show();
                        LatLng cur = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
                        //the fix is to set the lat and long in the emulator
                        CameraPosition position = CameraPosition.builder().target(cur).zoom(13f).build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(position);
                        googleMap.moveCamera(cameraUpdate);
                        // general params
                        //search food, return 3 results
                        //params.put("term", "food");
                        params.put("term", "restaurants");
                        //params.put("term", "bar");
                        //params.put("term", "Fast Food");
                        params.put("sort", "1");
                        params.put("radius_filter", "3000");
                        //params.put("limit", "50");

                        //params.put("location", "College Park, MD 20740");
                        CoordinateOptions coordinate = CoordinateOptions.builder()
                                .latitude(cur.latitude)
                                .longitude(cur.longitude).build();
                        Call<SearchResponse> call = yelpAPI.search(coordinate, params);
                        try {
                            nearbyLocations = (new GetYelpData()).execute(call).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < nearbyLocations.size(); i++) {
                            final LatLng loc= new LatLng(nearbyLocations.get(i).location().coordinate().latitude(), nearbyLocations.get(i).location().coordinate().longitude());
                            final String locName = nearbyLocations.get(i).name();
                            fb = new Firebase("https://quick-bite.firebaseio.com/").child(nearbyLocations.get(i).id()).child("waitTime");
                            fb.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() == null) {
                                        googleMap.addMarker(new MarkerOptions().position(loc).title(locName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                    } else if ((long) dataSnapshot.getValue() < 600) {
                                        googleMap.addMarker(new MarkerOptions().position(loc).title(locName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                    } else if ((long) dataSnapshot.getValue() > 1800) {
                                        googleMap.addMarker(new MarkerOptions().position(loc).title(locName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                    } else {
                                        googleMap.addMarker(new MarkerOptions().position(loc).title(locName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                                    }
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {
                                    Toast.makeText(getContext(), "Can't read your data!", Toast.LENGTH_SHORT).show();

                                }
                            });
                            //Log.d("WaitcheckHUE", locMap.get(nearbyLocations.get(i).id()));
                            //googleMap.addMarker(new MarkerOptions().position(new LatLng(nearbyLocations.get(i).location().coordinate().latitude(), nearbyLocations.get(i).location().coordinate().longitude())).title(nearbyLocations.get(i).name()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                        }
                        /*for (int i = 1; i < 15; i++) {
                            Log.d("In the proper place!", (nearbyLocations.get(i).name()) + nearbyLocations.get(i).location().coordinate().latitude() + nearbyLocations.get(i).location().coordinate().longitude());

                        }*/
                    }

                }
            });
        }

    }


    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(getContext(), "onConnected", Toast.LENGTH_SHORT).show();


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);

        }
        userLocation = LocationServices
                .FusedLocationApi
                .getLastLocation(mGoogleApiClient);
        if (userLocation == null) {

            Toast.makeText(getContext(), "location is null", Toast.LENGTH_SHORT).show();
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            //Log.d("locationtesting", "accuracy: " + userLocation.getAccuracy() + " lat: " + userLocation.getLatitude() + " lon: " + userLocation.getLongitude());
            if (userLocation == null) {
                Toast.makeText(getContext(), "You need to allow location on your device", Toast.LENGTH_SHORT).show();

            }


        } else {
            Toast.makeText(getContext(), userLocation.toString(), Toast.LENGTH_SHORT).show();
            setUpMap();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getContext(), "connection suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getContext(), "connection failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {


        Toast.makeText(getContext(), "Your location has changed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }




}


