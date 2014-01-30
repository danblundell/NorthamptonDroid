package uk.gov.northampton.droid.fragments;

import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.ReportProblem;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.*;

public class ReportLocation extends Activity {

	private GoogleMap mView;
	private LatLng mapCenter;
	private Marker mMarker = null;
	private LocationManager locationManager;
	private LocationListener bestProviderListener;
	private LocationListener bestAvailableProviderListener;
	private OnMarkerDragListener mdl;
	private OnMapClickListener mcl;
	private ReportProblem rp;
	private static final String LAST_KNOWN_LOCATION = "LAST_KNOWN_LOCATION";

	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_2_map);

		// if a lat/lng has been saved, use that
		if(savedInstanceState != null) {
			if (savedInstanceState.containsKey(LAST_KNOWN_LOCATION)){
				mapCenter = savedInstanceState.getParcelable(LAST_KNOWN_LOCATION);
			}

		}
		else {
			// set up the center point for the map
			double lat = 51.751724;
			double lng = -1.255285;
			mapCenter = new LatLng(lat,lng);
		}
		
		
		// get the report problem from the intent
		Intent intent = getIntent();
		rp = (ReportProblem) intent.getExtras().getSerializable("problem");

		// find the ui components
		mView = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapview)).getMap();
		Button selectLocation = (Button) findViewById(R.id.googlemaps_select_location);

		// Update the map ui
		UiSettings mapUi = mView.getUiSettings();
		mapUi.setZoomControlsEnabled(false);
		mapUi.setMyLocationButtonEnabled(true);

		// update the camera view of the map
		CameraUpdate update = CameraUpdateFactory.newLatLng(mapCenter);
		mView.animateCamera(update, 250, null);

		// when the marker is dragged, do stuff
		mdl = new OnMarkerDragListener() {

			@Override
			public void onMarkerDrag(Marker m) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onMarkerDragEnd(Marker m) {
				CameraUpdate update = CameraUpdateFactory.newLatLng(m.getPosition());
				mView.animateCamera(update, 250, null);
			}

			@Override
			public void onMarkerDragStart(Marker m) {
				// TODO Auto-generated method stub
			}

		};

		// when the map is clicked, do stuff
		mcl = new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng newPosition) {
				mMarker.setPosition(newPosition);
				CameraUpdate update = CameraUpdateFactory.newLatLngZoom(newPosition, 16);
				mView.animateCamera(update, 250, null);
			}

		};

		// add the listeners
		mView.setOnMapClickListener(mcl);
		mView.setOnMarkerDragListener(mdl);
		
		// create the marker and set its position
		if(mMarker == null){
			mMarker = mView.addMarker(new MarkerOptions()
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
			.position(mapCenter)
			.flat(true).draggable(true));
			mdl.onMarkerDragStart(mMarker);
		}
		else {
			mMarker.setPosition(mapCenter);
		}

		// get the location
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		bestProviderListener = new LocationListener(){

			@Override
			public void onLocationChanged(Location l) {

				if(l != null){

					// get the new location
					double lat = l.getLatitude();
					double lng = l.getLongitude();
					LatLng newLocation = new LatLng(lat,lng);

					if(mMarker == null){
						// create the marker if there isn't one
						mMarker = mView.addMarker(new MarkerOptions()
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
						.position(newLocation)
						.flat(true).draggable(true));
						mdl.onMarkerDragStart(mMarker);

					}
					else {
						mMarker.setPosition(newLocation);
					}

					// update the map view to center the new location
					mView.animateCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 16));

					// stop any further location updates
					unregisterAllListeners();
				}
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
			}

		};

		// only get a location update if there hasn't been one set already
		if(savedInstanceState == null) {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, bestProviderListener);
		}
		


		// set a click listener to save the location data and move to the next view
		selectLocation.setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				// May return null if a EasyTracker has not yet been initialized with a
				// property ID.
				EasyTracker easyTracker = EasyTracker.getInstance(getApplicationContext());

				if(easyTracker != null) {
					easyTracker.send(MapBuilder
							.createEvent(getString(R.string.ga_event_category_report),     // Event category (required)
									getString(R.string.ga_event_transaction),  // Event action (required)
									getString(R.string.ga_event_report_step2),   // Event label
									null)            // Event value
									.build()
							); 
				}
				
				Intent submitMenuIntent = new Intent(getApplicationContext(),ReportSubmitMenu.class);

				if(mMarker != null){
					rp.setpLat(mMarker.getPosition().latitude);
					rp.setpLng(mMarker.getPosition().longitude);
				}

				submitMenuIntent.putExtra("problem", rp);
				startActivity(submitMenuIntent);
			}

		});


	}

	public void onPause() {
		super.onPause();
		unregisterAllListeners();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// Save the marker position so that it can be reinstated
		outState.putParcelable(LAST_KNOWN_LOCATION, mMarker.getPosition());
		super.onSaveInstanceState(outState);
	}

	public void onResume() {
		super.onResume();
	}

	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	public void onStop() {
		super.onStop();
		unregisterAllListeners();
		EasyTracker.getInstance(this).activityStop(this);
	}

	public void unregisterAllListeners(){
		if(bestAvailableProviderListener != null){
			locationManager.removeUpdates(bestAvailableProviderListener);
		}
		if(bestProviderListener != null){
			locationManager.removeUpdates(bestProviderListener);
		}
	}

}
