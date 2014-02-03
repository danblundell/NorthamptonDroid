package uk.gov.northampton.droid.fragments;

import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.ReportProblem;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.*;

public class ReportLocation extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

	private GoogleMap mView;
	private LatLng mapCenter;
	private Marker mMarker = null;

	private ReportProblem rp;
	private LocationClient mLocationClient;
	private Location mCurrentLocation; 
	private Button selectLocation;
	private static final String LAST_KNOWN_LOCATION = "LAST_KNOWN_LOCATION";

	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_2_map);

		mLocationClient = new LocationClient(this, this, this);

		// get the report problem from the intent
		Intent intent = getIntent();
		rp = (ReportProblem) intent.getExtras().getSerializable("problem");

		// if a lat/lng has been saved, use that
		if(savedInstanceState != null && savedInstanceState.containsKey(LAST_KNOWN_LOCATION)) {
			mapCenter = savedInstanceState.getParcelable(LAST_KNOWN_LOCATION);
		}
		else {
			// set up a temporary center point for the map
			double lat = 51.751724;
			double lng = -1.255285;
			mapCenter = new LatLng(lat,lng);
			
			// if google services are available, connect to them to get a location
			if(servicesAvailable()) {
				mLocationClient.connect(); // this calls the 'onConnected' listener
			}
			
		}

		findViewsById();
		setMapUI(mView);
		setMapCenter(mView, mapCenter);

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

		addListeners();
	}
	
	private void getLastKnownLocation() {
		
		mCurrentLocation = mLocationClient.getLastLocation();

		if(mCurrentLocation != null) {
			LatLng newLocation = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
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
		}
	}

	private void findViewsById() {
		// find the ui components
		mView = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapview)).getMap();
		selectLocation = (Button) findViewById(R.id.googlemaps_select_location);
	}

	private void setMapUI(GoogleMap map) {
		// Update the map ui
		UiSettings mapUi = map.getUiSettings();
		mapUi.setZoomControlsEnabled(false);
		mapUi.setMyLocationButtonEnabled(true);
	}

	private void setMapCenter(GoogleMap map, LatLng center) {
		// update the camera view of the map
		CameraUpdate update = CameraUpdateFactory.newLatLng(center);
		map.animateCamera(update, 250, null);
	}


	private void addListeners() {
		// add the listeners
		mView.setOnMapClickListener(mcl);
		mView.setOnMarkerDragListener(mdl);
		selectLocation.setOnClickListener(selectLocationListener);
	}

	private OnMarkerDragListener mdl = new OnMarkerDragListener() {

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
	private OnMapClickListener mcl = new OnMapClickListener() {

		@Override
		public void onMapClick(LatLng newPosition) {
			mMarker.setPosition(newPosition);
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(newPosition, 16);
			mView.animateCamera(update, 250, null);
		}

	};

	// set a click listener to save the location data and move to the next view
	private OnClickListener selectLocationListener = new OnClickListener() {

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

	};

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
		if(mLocationClient.isConnected()) {
			mLocationClient.disconnect();
		}
		EasyTracker.getInstance(this).activityStop(this);
		super.onStop();
	}

	// Define a DialogFragment that displays the error dialog
	public static class ErrorDialogFragment extends DialogFragment {
		// Global field to contain the error dialog
		private Dialog mDialog;

		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}
		// Return a Dialog to the DialogFragment.
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	/*
	 * Handle results returned to the FragmentActivity
	 * by Google Play services
	 */
	@Override
	protected void onActivityResult(
			int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {

		case CONNECTION_FAILURE_RESOLUTION_REQUEST :
			/*
			 * If the result code is Activity.RESULT_OK, try
			 * to connect again
			 */
			switch (resultCode) {
			case Activity.RESULT_OK :


				break;
			}
		}
	}

	private boolean servicesAvailable() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			return true;

			// Google Play services was not available for some reason
		} else {

			// Get the error dialog from Google Play services
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
					resultCode,
					this,
					CONNECTION_FAILURE_RESOLUTION_REQUEST);

			// If Google Play services can provide an error dialog
			if (errorDialog != null) {
				// Create a new DialogFragment for the error dialog
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				// Set the dialog in the DialogFragment
				errorFragment.setDialog(errorDialog);
				// Show the error dialog in the DialogFragment
				errorFragment.show(getSupportFragmentManager(),"Location Updates");
			}
			return false;
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects.
		 * If the error has a resolution, try sending an Intent to
		 * start a Google Play services activity that can resolve
		 * error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(
						this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			/*
			 * If no resolution is available, display a dialog to the
			 * user with the error.
			 */
			showErrorDialog(connectionResult.getErrorCode());
		}

	}

	/**
	 * Show a dialog returned by Google Play services for the
	 * connection error code
	 *
	 * @param errorCode An error code returned from onConnectionFailed
	 */
	private void showErrorDialog(int errorCode) {

		// Get the error dialog from Google Play services
		Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
				errorCode,
				this,
				CONNECTION_FAILURE_RESOLUTION_REQUEST);

		// If Google Play services can provide an error dialog
		if (errorDialog != null) {

			// Create a new DialogFragment in which to show the error dialog
			ErrorDialogFragment errorFragment = new ErrorDialogFragment();

			// Set the dialog in the DialogFragment
			errorFragment.setDialog(errorDialog);

			// Show the error dialog in the DialogFragment
			errorFragment.show(getSupportFragmentManager(), "Connection Error");
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		getLastKnownLocation();
	}

	@Override
	public void onDisconnected() {
		if(!mLocationClient.isConnecting()){
			mLocationClient.connect();
		}
	}

}
