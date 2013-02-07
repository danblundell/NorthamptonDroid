package uk.gov.northampton.droid.fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.gov.northampton.droid.Confirmation;
import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.ReportProblem;
import uk.gov.northampton.droid.lib.ConfirmationRetriever;
import uk.gov.northampton.droid.lib.PhotoChooserDialogFragment;
import uk.gov.northampton.droid.lib.ReportHttpSender;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.Window;

public class ReportSubmitMenu extends SherlockFragmentActivity implements PhotoChooserDialogFragment.PhotoChooserDialogListener {

	private static final int CAMERA_REQUEST = 1987;
	private static final String JPEG_FILE_PREFIX = "npton_";
	private static final String JPEG_FILE_SUFFIX = ".jpeg";
	private static final String REPORT_PHOTO_LOCATION = "REPORT_PHOTO_LOCATION";
	
	private View mView;
	private TextView jobType;
	private EditText jobDesc;
	private ImageButton addPhoto;
	private ImageView jobPhoto;
	private TextView jobEmail;
	private TextView jobPhone;
	private ToggleButton jobEmailToggle;
	private ToggleButton jobPhoneToggle;
	private Button jobSubmit;
	
	
	private Uri photoUri;
	private File storageDir;
	private File imageFile;
	private String currentPhotoPath;
	private ReportProblem rp;
	private Bitmap pThumbnailImage;
	private String pLat;
	private String pLng;
	private String pNumber;
	private String pType;
	private String pDesc;
	private String pEmail;
	private String pPhone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.report_3_submit);
		setSupportProgressBarIndeterminateVisibility(false);

		ActionBar ab = getSupportActionBar();
		ab.setTitle(getString(R.string.report_type_title));
		
		findAllViewsById();
		
		//Set initial UI
		jobPhoto.setVisibility(View.GONE);
		addPhoto.setOnClickListener(AddPhotoListener);
		jobPhoto.setOnClickListener(FullScreenImageListener);
		jobSubmit.setOnClickListener(ReportButtonListener);

		updateUI();
		
		if(savedInstanceState != null){
			currentPhotoPath = savedInstanceState.getString(REPORT_PHOTO_LOCATION);
			if(currentPhotoPath != null){
				Log.i("SAVED IMAGE LOC",currentPhotoPath);
				getImageThumbnail();
			}
		}

		storageDir = getAlbumName();
		
	}
	
	@Override
	protected void onResume() {
		if(!jobSubmit.isEnabled()){
			jobSubmit.setEnabled(true);
		}
		super.onResume();
	}

	private void findAllViewsById(){
		jobType = (TextView) findViewById(R.id.report_desc_title_TextView);
		jobDesc = (EditText) findViewById(R.id.reportDescriptionEditText);
		addPhoto = (ImageButton) findViewById(R.id.reportImageButton);
		jobPhoto = (ImageView) findViewById(R.id.reportImagePreview);
		jobSubmit = (Button) findViewById(R.id.reportSubmitButton);
		jobEmail = (TextView) findViewById(R.id.report_notifications_email_TextView);
		jobPhone = (TextView) findViewById(R.id.report_notifications_sms_TextView);
		jobEmailToggle = (ToggleButton) findViewById(R.id.report_notifications_email_ToggleButton);
		jobPhoneToggle = (ToggleButton) findViewById(R.id.report_notifications_sms_ToggleButton);
	}
	
	private void updateFromIntent(){
		Intent intent = getIntent();
		
		//get location
		double lat = intent.getDoubleExtra("lat", 0);
		double lng = intent.getDoubleExtra("lng", 0);
		Log.i("LAT", "" + lat);
		Log.i("LNG", "" + lng);
		pLat = Double.toString(lat);
		pLng = Double.toString(lng);

		//get problem type
		rp = (ReportProblem) intent.getSerializableExtra("type");
		pType = rp.getpDesc();
		pNumber = String.valueOf(rp.getpNum());
	}

	private void updateUI(){
		updateFromIntent();
		updateFromPreferences();
		
		jobType.setText(pType);
		jobEmail.setText(pEmail);
		jobPhone.setText(pPhone);
		
	}
	
	private void updateFromPreferences(){
		//get email and phone number from preferences
		Context context = getApplicationContext();
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		pEmail = sharedPrefs.getString(Settings.NBC_EMAIL, null);
		pPhone = sharedPrefs.getString(Settings.NBC_TEL, null);
	}
	
	private OnClickListener AddPhotoListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			showPhotoOptions();
		}
	};
	
	private OnClickListener FullScreenImageListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			Intent removePhotoIntent = new Intent(getApplicationContext(),ReportImageFullScreen.class);
			removePhotoIntent.putExtra("photo", currentPhotoPath);
			startActivity(removePhotoIntent);
		}
	};
	
	private File getAlbumName() {
		File dir = new File(
			    Environment.getExternalStoragePublicDirectory(
			        Environment.DIRECTORY_PICTURES
			    ), 
			    getString(R.string.photo_album_name)
			);
		Log.d("STORAGE DIR RETURNING", dir.getAbsolutePath());
		if(!dir.exists()){
			dir.mkdirs();
		}
        return dir;
	}
	
	public void showPhotoOptions() {
	    DialogFragment newFragment = new PhotoChooserDialogFragment();
	    newFragment.show(getSupportFragmentManager(), "photoChooser");
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
		Log.d("Activity Result", "Code: " + requestCode + " | resultCode: " + resultCode);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {  
           getImageThumbnail(); 
        }  
    }
	
	private void getImageThumbnail(){
		ProcessImageThumbnailTask imageThumb = new ProcessImageThumbnailTask();
        imageThumb.execute(currentPhotoPath);
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
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_" + JPEG_FILE_SUFFIX;
	    File image = new File(storageDir,imageFileName);
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
	  
	    int scaleFactor = 10;
	    // Determine how much to scale down the image
	    if(targetW > 0 && targetH >0){
	    	scaleFactor = Math.min(photoW/targetW, photoH/targetH) | 10;
	    }
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
			int viewW = addPhoto.getWidth();
			Log.d("Width","Factor: " + viewW);
			int viewH = addPhoto.getHeight();
			Log.d("Height","Factor: " + viewH);
			//BitmapFactory.Options bmThumbFactory = new BitmapFactory.Options();
			//bmThumbFactory.inJustDecodeBounds = false;
			//bmThumbFactory.inSampleSize = 12; //TODO query sample size for thumbnail based on imageView size
			Bitmap mBitmap = scalePic(viewW,viewH,currentPhotoPath); //BitmapFactory.decodeFile(currentPhotoPath, bmThumbFactory); //TODO add compression parameters
			Log.d("BITMAP SCALED","NEW HEIGHT: " + mBitmap.getHeight());
			return 	mBitmap; 
		}

		@Override
		protected void onPostExecute(final Bitmap image){
			pThumbnailImage = image;
			showThumbnail(pThumbnailImage);
		}

	}
	
	private void showThumbnail(Bitmap image){
		if(image.getHeight() > 0){
			addPhoto.setVisibility(View.GONE);
			jobPhoto.setVisibility(View.VISIBLE);
			jobPhoto.setImageBitmap(image);
		}else{
			//show error
		}
	}
	
	private OnClickListener ReportButtonListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			setBusy(true);
			if(v.isEnabled()){
				v.setEnabled(false);
			}
			Boolean image = false;
			if(pThumbnailImage != null){
				image = true;
			}
			pDesc = jobDesc.getText().toString();
			ReportSubmitTask rst = new ReportSubmitTask();
			rst.execute(
					pDesc,
					pEmail,
					"",
					image.toString(),
					pLat,
					pLng,
					"Problem Location",
					pPhone,
					pNumber
			);
		}
		
	};
	
	private void setBusy(Boolean b){
		setSupportProgressBarIndeterminateVisibility(b);
	}
	
	private class ReportSubmitTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			
			Log.d("SUBMITTING REPORT","DO IN BG");
			//set up the http request
			ReportHttpSender rs = new ReportHttpSender();
			rs.setDataSource(getString(R.string.data_source));
			rs.setDesc(params[0]);
			rs.setDeviceID("12345");
			rs.setEmail(params[1]);
			rs.setImage(params[2]);
			rs.setIncludesImage(params[3]);
			rs.setLat(params[4]);
			rs.setLng(params[5]);
			rs.setLocation(params[6]);
			rs.setPhone(params[7]);
			rs.setProblemNumber(params[8]);
			
			//check 'image' parameter
			Boolean image = Boolean.parseBoolean(params[2]);
			
			//if image is true and there is a thumbnail image to send, compress the image and attach to the request
			if(pThumbnailImage != null && image){
				//make http request
				ByteArrayOutputStream baos = new ByteArrayOutputStream();  
				pThumbnailImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object   
				byte[] b = baos.toByteArray();
				String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
				Log.i("ENCODED IMAGE", encodedImage);
				rs.setImageData(b);
			}
			//make the http request
			String result = rs.send(getString(R.string.mycouncil_url) + getString(R.string.report_url));
			return result;
		}
		
		@Override
		protected void onPostExecute(final String result){
			Log.d("SUBMITTING REPORT","OPE");
			setBusy(false);
			if(result != null){
				Log.d("REPORT PE",result);
				ConfirmationRetriever cr = new ConfirmationRetriever();
				Confirmation conf = cr.retrieveConfirmation(result);
				Intent confIntent = new Intent(getApplicationContext(),ReportConfirmation.class);
				confIntent.putExtra("result", conf);
				startActivity(confIntent);
			}
			else{
				Toast.makeText(getApplicationContext(), "There was an error sending this case", Toast.LENGTH_LONG).show();
			}
		}
			
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.i("SAVING INSTANCE", "BUNDLE SAVING");
		if(currentPhotoPath != null){
			Log.i("SAVING INSTANCE", currentPhotoPath);
			outState.putString(REPORT_PHOTO_LOCATION, currentPhotoPath);
		}
		super.onSaveInstanceState(outState);
	}
	
	
}
