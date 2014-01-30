package uk.gov.northampton.droid.fragments;

import uk.gov.northampton.droid.R;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.analytics.tracking.android.EasyTracker;

public class ReportImageFullScreen extends SherlockFragmentActivity {

	//private Button removePhoto;
	private ImageView fullPhoto;
	private View fullPhotoContainer;
	private String currentPhotoPath;
	OnPhotoRemovedListener photoRemovedListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_4_full_photo);

		//removePhoto = (Button) findViewById(R.id.reportImageRemoveButton);
		fullPhoto = (ImageView) findViewById(R.id.reportImageFullScreen);
		fullPhotoContainer = (View) findViewById(R.id.reportImageFullLayout);
		
		Intent intent = getIntent();
		currentPhotoPath = intent.getStringExtra("photo");
		if(currentPhotoPath != null){
			ProcessImageFullScreenTask imageFS = new ProcessImageFullScreenTask();
			imageFS.execute(currentPhotoPath);
		}
		
		
	}
	// Container Activity must implement this interface
    public interface OnPhotoRemovedListener {
        public void onPhotoRemoved(String photoPath);
    }
    
    

	private Bitmap scalePic(int targetW, int targetH, String photoPath) {
	  
	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//	    bmOptions.inJustDecodeBounds = true;
//	    BitmapFactory.decodeFile(photoPath, bmOptions);
//	    int photoW = bmOptions.outWidth;
//	    int photoH = bmOptions.outHeight;
	  
	    // Determine how much to scale down the image
	    int scaleFactor = 12; //Math.min(photoW/targetW, photoH/targetH);
	  
	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;
	  
	    return BitmapFactory.decodeFile(photoPath, bmOptions);
	    //fullPhoto.setImageBitmap(bitmap);
	}
	
	private class ProcessImageFullScreenTask extends AsyncTask<String, Void, Bitmap>{

		@Override
		protected Bitmap doInBackground(String... params) {
			String imageUri = params[0];
			int targetW = 1;//TODO get image view width
			int targetH = 1;//TODO get image view height
			Bitmap mBitmap = scalePic(targetW, targetH, imageUri);
			return 	mBitmap; 
		}

		@Override
		protected void onPostExecute(final Bitmap image){
				fullPhoto.setImageBitmap(image);
		}

	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}
}

