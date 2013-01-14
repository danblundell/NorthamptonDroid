package uk.gov.northampton.droid.fragments;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Window;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.ReportProblem;
import uk.gov.northampton.droid.lib.ReportLocationItemizedOverlay;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ZoomButtonsController;

public class ReportLocation extends MapActivity{
	
	private GeoPoint p;
	private MapController mController;
	private MapView mView;
	private MyLocationOverlay mOverlay = null;
	private GeoPoint mMarker = null;
	private ReportProblem rp;
	private LocationManager locationManager;
	private LocationListener bestProviderListener;
	private LocationListener bestAvailableProviderListener;

	public void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_2_map);
		
		Intent intent = getIntent();
		rp = (ReportProblem) intent.getExtras().getSerializable("type");
		
		double lat = 51.751724;
		double lng = -1.255285;
		p = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));

		mView = (MapView) findViewById(R.id.mapview);
		Button selectLocation = (Button) findViewById(R.id.googlemaps_select_location);
		mView.setBuiltInZoomControls(true);
		mController = mView.getController();
		mController.setCenter(p);
		mController.setZoom(14);

		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		bestProviderListener = new LocationListener(){

			@Override
			public void onLocationChanged(Location l) {
				// TODO Auto-generated method stub
				Log.d("Location Changed","Lat: " + l.getLatitude());
				if(l != null){
					double lat = l.getLatitude();
					double lng = l.getLongitude();
					p = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
					mController.animateTo(p);
					mController.setZoom(18);
					Drawable marker = getResources().getDrawable(R.drawable.pin);
					marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
					mView.getOverlays().clear();
					mView.getOverlays().add(new SitesOverlay(marker));
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
		
		selectLocation.setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View v) {
				//Toast.makeText(ReportLocation.this, "Lat: " + (mMarker.getLatitudeE6() / 1E6) + " / " + "Lng: " + (mMarker.getLongitudeE6() / 1E6),Toast.LENGTH_LONG).show();
				Intent submitMenuIntent = new Intent(mView.getContext(),ReportSubmitMenu.class);
				if(mMarker != null){
					submitMenuIntent.putExtra("lat", mMarker.getLatitudeE6() / 1E6);
					submitMenuIntent.putExtra("lng", mMarker.getLongitudeE6() / 1E6);
				}
				submitMenuIntent.putExtra("type", rp);
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
	};

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private GeoPoint getPoint(double lat, double lon) {
	    return(new GeoPoint((int)(lat*1E6),(int)(lon*1E6)));
	 }
	
	private class SitesOverlay extends ItemizedOverlay<OverlayItem>{
		private List<OverlayItem> items = new ArrayList<OverlayItem>();
		private Drawable marker = null;
		private OverlayItem inDrag = null;
		private ImageView dragImage = null;
		private int xDragImageOffset = 0;
		private int yDragImageOffset = 0;
		private int xDragTouchOffset = 0;
		private int yDragTouchOffset = 0;
		
		public SitesOverlay(Drawable marker){
			super(marker);
			this.marker = marker;
			
			dragImage = (ImageView) findViewById(R.id.drag);
			xDragImageOffset = dragImage.getDrawable().getIntrinsicWidth() / 2;
			yDragImageOffset = dragImage.getDrawable().getIntrinsicHeight();
			
			items.add(new OverlayItem(p,"Location","You are here."));
			mMarker = p;
			
			populate();	
		}
		
		@Override
		protected OverlayItem createItem(int i){
			return(items.get(i));
		}
		
		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow){
			
			super.draw(canvas, mapView, shadow);
			boundCenterBottom(marker);	
		}
		
		@Override
		public int size(){
			return (items.size());
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event, MapView mapView){
			final int action = event.getAction();
			final int x = (int) event.getX();
			final int y = (int) event.getY();
			boolean result = false;
			
			if(action==MotionEvent.ACTION_DOWN){
				for(OverlayItem item : items){
					Point p = new Point(0,0);
					
					mView.getProjection().toPixels(item.getPoint(),p);
					
					if(hitTest(item, marker, x-p.x, y-p.y)){
						result = true;
						inDrag = item;
						items.remove(inDrag);
						populate();
						
						xDragTouchOffset =  0;
						yDragTouchOffset = 0;
						
						setDragImagePosition(p.x,p.y);
						dragImage.setVisibility(View.VISIBLE);
						
						xDragTouchOffset = x-p.x;
						yDragTouchOffset = y-p.y;
						
						break;
					}
				}
			}
			else if(action==MotionEvent.ACTION_MOVE && inDrag != null){
				setDragImagePosition(x,y);
				result = true;
			}
			else if(action==MotionEvent.ACTION_UP && inDrag != null){
				//dropping the marker
				dragImage.setVisibility(View.GONE);
				GeoPoint pt = mapView.getProjection().fromPixels(x-xDragTouchOffset, y-yDragTouchOffset);
				OverlayItem toDrop = new OverlayItem(pt, inDrag.getTitle(), inDrag.getSnippet());
				items.add(toDrop);
				populate();
				mMarker = pt;
				mapView.getController().animateTo(pt);
				inDrag = null;
				result = true;
			}
			
			return (result || super.onTouchEvent(event, mapView));
		}
		
		private void setDragImagePosition(int x, int y){
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) dragImage.getLayoutParams();
			lp.setMargins(x-xDragImageOffset-xDragTouchOffset, y-yDragImageOffset-yDragTouchOffset, 0, 0);
			dragImage.setLayoutParams(lp);
		}
	}
	
}
