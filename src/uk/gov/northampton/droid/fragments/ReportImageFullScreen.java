package uk.gov.northampton.droid.fragments;

import uk.gov.northampton.droid.R;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class ReportImageFullScreen extends SherlockFragmentActivity {

	private static final int CAMERA_REQUEST = 1987;
	private static final int THUMBNAIL_SIZE = 0;
	private Button removePhoto;
	private ImageView fullPhoto;
	private View fullPhotoContainer;
	private String currentPhotoPath;
	private Bitmap photoBitmap;
	private int imageViewW;
	private int imageViewH;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.report_4_full_photo);
		
		ActionBar actionBar = getSupportActionBar();

		removePhoto = (Button) findViewById(R.id.reportImageRemoveButton);
		fullPhoto = (ImageView) findViewById(R.id.reportImageFullScreen);
		fullPhotoContainer = (View) findViewById(R.id.reportImageFullLayout);
		
		Intent intent = getIntent();
		currentPhotoPath = intent.getStringExtra("photo");
		if(currentPhotoPath != null){
			ProcessImageFullScreenTask imageFS = new ProcessImageFullScreenTask();
			imageFS.execute(currentPhotoPath);
		}else{
			Log.d("ImageFullScreen","File path empty");
			Toast.makeText(this,"File path empty", Toast.LENGTH_LONG).show();
		}
		
		removePhoto.setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View v) {
				Log.d("Image Remove Button","Clicked!");
				//remove photo and go back
			}
		});	
	}

	private Bitmap scalePic(int targetW, int targetH, String photoPath) {
	    // Get the dimensions of the View
	    //int targetW = fullPhoto.getWidth();
	    //int targetH = fullPhoto.getHeight();
	  
	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(photoPath, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;
	  
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
				Toast.makeText(getBaseContext(),"Image height: " + image.getHeight(),Toast.LENGTH_LONG).show();
				fullPhoto.setImageBitmap(image);
		}

	}
}

