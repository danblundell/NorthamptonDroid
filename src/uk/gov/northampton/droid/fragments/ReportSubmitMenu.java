package uk.gov.northampton.droid.fragments;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.ReportProblem;
import uk.gov.northampton.droid.lib.PhotoChooserDialogFragment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class ReportSubmitMenu extends SherlockFragmentActivity implements PhotoChooserDialogFragment.PhotoChooserDialogListener {

	private static final int CAMERA_REQUEST = 1987;
	private static final String JPEG_FILE_PREFIX = "npton_";
	private static final String JPEG_FILE_SUFFIX = ".jpeg";
	private ImageButton addPhoto;
	private ImageView jobPhoto;
	private View mView;
	private Uri photoUri;
	private File storageDir;
	private File imageFile;
	private String currentPhotoPath;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.report_job_submit);
		getAlbumName();
		ActionBar actionBar = getSupportActionBar();
		mView = (View) findViewById(R.id.reportTypeLayout);
		TextView jobType = (TextView) findViewById(R.id.reportType);
		EditText jobDescription = (EditText) findViewById(R.id.reportDescriptionEditText);
		addPhoto = (ImageButton) findViewById(R.id.reportImageButton);
		jobPhoto = (ImageView) findViewById(R.id.reportImagePreview);
		Button jobSubmit = (Button) findViewById(R.id.reportSubmitButton);
		
		Intent intent = getIntent();
		double lat = intent.getDoubleExtra("lat", 0);
		double lng = intent.getDoubleExtra("lng", 0);
		ReportProblem rp = (ReportProblem) intent.getExtras().getSerializable("type");
		jobType.setText(rp.getpDesc());
		jobPhoto.setVisibility(View.GONE);
		
		File storageDir = new File(
			    Environment.getExternalStoragePublicDirectory(
			        Environment.DIRECTORY_PICTURES
			    ), 
			    getAlbumName()
			);
		
		Log.d("File Directory", storageDir.getAbsolutePath());

		addPhoto.setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View v) {
				Log.d("Image Button","Clicked!");
				//load dialog
				showPhotoOptions();
			}
		});
		
		jobPhoto.setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View v) {
				Log.d("Image View","Clicked!");
				Intent removePhotoIntent = new Intent(mView.getContext(),ReportImageFullScreen.class);
				removePhotoIntent.putExtra("photo", currentPhotoPath);
				startActivity(removePhotoIntent);
			}
		});
		
		jobSubmit.setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View v) {
				Log.d("Submit Button","Clicked!");
				//load dialog
			}
		});
		
		//Toast.makeText(this, "Lat: " + lat + " / Lng: " + lng, Toast.LENGTH_LONG).show();
		
	}
	
	private String getAlbumName() {
		Log.d("Album Name", getString(R.string.photo_album_name));
        return getString(R.string.photo_album_name);
	}
	
	public void showPhotoOptions() {
	    DialogFragment newFragment = new PhotoChooserDialogFragment();
	    newFragment.show(getSupportFragmentManager(), "photoChooser");
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
		Log.d("Activity Result", "Code: " + requestCode + " | resultCode: " + resultCode);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {  
            ProcessImageThumbnailTask imageThumb = new ProcessImageThumbnailTask();
            imageThumb.execute(currentPhotoPath);
        }  
    }

	@Override
	public void onFinishPhotoChooserDialog(int option) {
		Log.d("Finished Dialog","Option: " + option);
		if(option == 0){
			dispatchTakePictureIntent(CAMERA_REQUEST);
		}
	}
	
	private void dispatchTakePictureIntent(int actionCode) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try{
		imageFile = createImageFile();
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
		}catch(IOException e){
			e.printStackTrace();
		}
	    
		startActivityForResult(takePictureIntent, actionCode);
	}
	
	private void dispatchChooseFromGalleryIntent(int actionCode){
		//TODO go get an image from the gallery - run it through the same thumbnail compression and store the uri
	}
	
	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = 
	        new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
	    File image = File.createTempFile(
	        imageFileName, 
	        JPEG_FILE_SUFFIX, 
	        storageDir
	    );
	    
	    Log.d("Path: ", image.getAbsolutePath());
	    
	    currentPhotoPath = image.getAbsolutePath();
	    return image;
	}
	
	private Bitmap scalePic(int targetW, int targetH, String photoPath) {
	  
	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(photoPath, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;
	  
	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH) | 12;
	    Log.d("SCALE","Factor: " + scaleFactor);
	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;
	  
	    return BitmapFactory.decodeFile(photoPath, bmOptions);
	}
	
	private class ProcessImageThumbnailTask extends AsyncTask<String, Void, Bitmap>{

		@Override
		protected Bitmap doInBackground(String... params) {
			String imageUri = params[0];
			int viewW = addPhoto.getWidth();
			Log.d("Width","Factor: " + viewW);
			int viewH = addPhoto.getHeight();
			Log.d("Height","Factor: " + viewH);
			//BitmapFactory.Options bmThumbFactory = new BitmapFactory.Options();
			//bmThumbFactory.inJustDecodeBounds = false;
			//bmThumbFactory.inSampleSize = 12; //TODO query sample size for thumbnail based on imageView size
			Bitmap mBitmap = scalePic(viewW,viewH,currentPhotoPath); //BitmapFactory.decodeFile(currentPhotoPath, bmThumbFactory); //TODO add compression parameters
			return 	mBitmap; 
		}

		@Override
		protected void onPostExecute(final Bitmap image){

				if(image.getHeight() > 0){
					addPhoto.setVisibility(View.GONE);
					jobPhoto.setVisibility(View.VISIBLE);
					jobPhoto.setImageBitmap(image);
				}
				
		}

	}
}
