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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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

	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_2_map);
		
		// get the report problem from the intent
		Intent intent = getIntent();
		rp = (ReportProblem) intent.getExtras().getSerializable("problem");
		
		// set up the center point for the map
		double lat = 51.751724;
		double lng = -1.255285;
		mapCenter = new LatLng(lat,lng);
		
		// find the ui components
		mView = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapview)).getMap();
		Button selectLocation = (Button) findViewById(R.id.googlemaps_select_location);
		
		// Update the map ui
		UiSettings mapUi = mView.getUiSettings();
		mapUi.setZoomControlsEnabled(false);
		mapUi.setMyLocationButtonEnabled(true);
		
		
		CameraUpdate update = CameraUpdateFactory.newLatLng(mapCenter);
		mView.animateCamera(update, 250, null);

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
		
		mcl = new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng newPosition) {
				mMarker.setPosition(newPosition);
				CameraUpdate update = CameraUpdateFactory.newLatLngZoom(newPosition, 16);
				mView.animateCamera(update, 250, null);
			}
			
		};
		
		mView.setOnMapClickListener(mcl);
		mView.setOnMarkerDragListener(mdl);
		
		// get the location
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		bestProviderListener = new LocationListener(){

			@Override
			public void onLocationChanged(Location l) {
				Log.d("Location Changed","Lat: " + l.getLatitude());
				
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
				Log.d("Location Listener Status Changed","Status: " + status + ", Provider: " + provider);
			}
			
		};
		
		
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, bestProviderListener);
		
		
		// set a click listener to save the location data and move to the next view
		selectLocation.setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View v) {
				//Toast.makeText(ReportLocation.this, "Lat: " + (mMarker.getLatitudeE6() / 1E6) + " / " + "Lng: " + (mMarker.getLongitudeE6() / 1E6),Toast.LENGTH_LONG).show();
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
		Log.d("Map Activity", "Paused!!");
	}

	public void onResume() {
		super.onResume();
		Log.d("Map Activity", "Resumed!!");
	}

	public void onStart() {
		super.onStart();
		Log.d("Map Activity", "Started!!");
	}

	public void onStop() {
		super.onStop();
		unregisterAllListeners();
		Log.d("Map Activity", "Stopped!!");
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
