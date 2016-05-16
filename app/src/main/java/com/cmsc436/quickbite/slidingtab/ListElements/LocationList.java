package com.cmsc436.quickbite.slidingtab.ListElements;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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
//import com.cmsc436.quickbite.tabbedview.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import retrofit.Call;
import retrofit.Response;


public class LocationList extends ListFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
		com.google.android.gms.location.LocationListener{
    public static String restaurantIDKey = "restaurant_id";
	public static String restaurantNameKey = "restaurant_name";
    public static String waitKey = "wait_time";
	TypedArray icon_pics;
	List<RowItem> rowItems;
	Location userLocation = new Location("");
	private LocationRequest mLocationRequest;
	private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
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
	CustomAdapter adapter;

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
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.location, container, false);

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

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	public void setUp() {
		if (userLocation == null) { return; }
		//params.put("term", "food");
		params.put("term", "restaurants");
		//params.put("term", "bar");
		//params.put("term", "Fast Food");
		params.put("sort", "1");
		params.put("radius_filter", "3000");
		//final Map <String, Long> wait = new HashMap<String, Long>();

		//params.put("location", "College Park, MD 20740");
		Log.d("The users loc", userLocation.getLatitude() + "  " + userLocation.getLongitude());
		CoordinateOptions coordinate = CoordinateOptions.builder()
				.latitude(userLocation.getLatitude())
				.longitude(userLocation.getLongitude()).build();
		Call<SearchResponse> call = yelpAPI.search(coordinate, params);
		try {
			nearbyLocations = (new GetYelpData()).execute(call).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		rowItems = new ArrayList<RowItem>();
		icon_pics = getResources().obtainTypedArray(R.array.icon_pics);



		for (int i = 0; i < nearbyLocations.size(); i++) {
			Log.d("MY LOCATIONS", nearbyLocations.get(i).name());
			ArrayList<String> locAddress = nearbyLocations.get(i).location().address();
			StringBuilder address = new StringBuilder();
			for (int x = 0; x < locAddress.size(); x++)  {
				address.append(locAddress.get(x));
				address.append(" ");
			}
			long value = 0;

			fb = new Firebase("https://quick-bite.firebaseio.com/").child(nearbyLocations.get(i).id()).child("waitTime");
			fb.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					Business currentBusiness = null;
					StringBuilder addressIn = null;
					RowItem itemIn = null;
					int idx = -1;
					int resId = -1;
                    long wait = -1;
					String restaurantID = dataSnapshot.getRef().toString();
					restaurantID = restaurantID.substring("https://quick-bite.firebaseio.com/".length());
					restaurantID = restaurantID.substring(0, restaurantID.indexOf("/waitTime"));
					for (int j = 0; j < nearbyLocations.size(); j++) {
						if (nearbyLocations.get(j).id().toString().equals(restaurantID)) {
							currentBusiness = nearbyLocations.get(j);
							ArrayList<String> locAddress = currentBusiness.location().address();
							addressIn = new StringBuilder();
							for (int x = 0; x < locAddress.size(); x++)  {
								addressIn.append(locAddress.get(x));
								addressIn.append(" ");
							}
							idx = j;
							break;
						}

					}

					if (dataSnapshot.getValue() != null && currentBusiness!= null) {
						 wait = (long) dataSnapshot.getValue();

						if (wait > -1 && wait < 36000) {
							resId =0;

						}else if (wait > 36000 && wait < 108000) {
							resId = 1;

						} else if (wait > 108000) {
							resId =2;

						} else {

						}


					} else {
					}

					if (resId != -1) {

					//	for (int z = 0; z < rowItems.size(); z++) {
						//	if (rowItems.get(z).getLocation_id().equals(itemIn.getLocation_id())) {
								rowItems.get(idx).seticon_id(icon_pics.getResourceId(resId,-1));
                                rowItems.get(idx).setWaitTime(wait);

								//rowItems.set(idx, itemIn);
								adapter.notifyDataSetChanged();
							//	break;
							}
						//}
					//}
				}

				@Override
				public void onCancelled(FirebaseError firebaseError) {
					Toast.makeText(getContext(), "Can't read your data!", Toast.LENGTH_SHORT).show();

				}
			});




			RowItem item = new RowItem((nearbyLocations.get(i).name()),
					(icon_pics.getResourceId(0, -1)), (address.toString() + ", " + nearbyLocations.get(i).location().city()),
					((String.format("%.2g ", (nearbyLocations.get(i).distance() * 0.000621371))) +"mi"), nearbyLocations.get(i).id(),-1);


			rowItems.add(item);
			if (item.getLocation_id() != null) {
				Log.d("Rowitems", item.getLocation_id());
			} else {
				Log.d("Rowitems", "null");
			}
		}

		adapter = new CustomAdapter(getActivity(), rowItems);

		setListAdapter(adapter);
		ListView lv = getListView();
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				final RowItem item = (RowItem) parent.getItemAtPosition(position);
				//Toast.makeText(getContext(), item.getLocation_id(), Toast.LENGTH_LONG).show();
				Intent i = new Intent(getContext(), RestaurantProfile.class);
				i.putExtra(LocationList.restaurantIDKey, item.getLocation_id());
                i.putExtra(LocationList.restaurantNameKey, item.getlocation_name());
				i.putExtra(LocationList.waitKey, item.getWaitTime());
				startActivity(i);
			}

		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
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

		}

		setUp();
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
