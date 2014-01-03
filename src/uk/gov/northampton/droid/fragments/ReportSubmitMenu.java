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
import uk.gov.northampton.droid.lib.EditTextDialogFragment.EditTextDialogListener;
import uk.gov.northampton.droid.lib.EditTextDialogFragment;
import uk.gov.northampton.droid.lib.PhotoChooserDialogFragment;
import uk.gov.northampton.droid.lib.ReportHttpSender;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;

public class ReportSubmitMenu extends SherlockFragmentActivity implements PhotoChooserDialogFragment.PhotoChooserDialogListener, ReportImageFullScreen.OnPhotoRemovedListener {

	private static final int CAMERA_REQUEST = 1987;
	private static final int GALLERY_REQUEST = 1986;
	private static final int CAMERA = 0;
	private static final int GALLERY = 1;
	private static final String JPEG_FILE_PREFIX = "npton_";
	private static final String JPEG_FILE_SUFFIX = ".jpeg";
	private static final String REPORT_PHOTO_LOCATION = "REPORT_PHOTO_LOCATION";
	
	// Views
	private View mView;
	private TextView jobType;
	private EditText jobDesc;
	private ImageButton addPhoto;
	private ImageView jobPhoto;
	private TextView jobEmail;
	private TextView jobPhone;
	private CheckBox jobEmailCb;
	private CheckBox jobPhoneCb;
	private Button jobSubmit;
	
	// Data
	private Uri photoUri;
	private File storageDir;
	private File imageFile;
	private String currentPhotoPath;
	private ReportProblem rp;
	private Bitmap pThumbnailImage;
	private String pType;
	private String pDesc;
	private String pEmail;
	private String pPhone;
	private boolean emailNotification = false;
	private boolean phoneNotification = false;
	
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
		updateUI();
		setEventListeners();
		
		//Set initial UI
		jobPhoto.setVisibility(View.GONE);
		
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
		jobEmailCb = (CheckBox) findViewById(R.id.report_notifications_email_Checkbox);
		jobPhoneCb = (CheckBox) findViewById(R.id.report_notifications_sms_Checkbox);
	}
	
	private void setEventListeners() {
		addPhoto.setOnClickListener(AddPhotoListener);
		jobPhoto.setOnClickListener(FullScreenImageListener);
		jobSubmit.setOnClickListener(ReportButtonListener);
		jobEmailCb.setOnCheckedChangeListener(CheckBoxListener);
		jobPhoneCb.setOnCheckedChangeListener(CheckBoxListener);
	}
	
	/*
	 * Get the contents of the intent and update the report object properties
	 */
	private void updateFromIntent(){
		Intent intent = getIntent();
		
		//get location
//		double lat = intent.getDoubleExtra("lat", 0);
//		double lng = intent.getDoubleExtra("lng", 0);
//		pLat = Double.toString(lat);
//		pLng = Double.toString(lng);

		//get problem
		rp = (ReportProblem) intent.getSerializableExtra("problem");
		pType = rp.getpDesc();
		//pNumber = String.valueOf(rp.getpNum());
	}
	
	/*
	 * Get the app preferences and set the object properties
	 */
	private void updateFromPreferences(){
		// get email and phone number from preferences
		Context context = getApplicationContext();
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		pEmail = sharedPrefs.getString(Settings.NBC_EMAIL, getString(R.string.settings_email_add));
		pPhone = sharedPrefs.getString(Settings.NBC_TEL, getString(R.string.settings_telephone_add));
	}

	/*
	 * Update the UI based of the object and app properties
	 */
	private void updateUI(){
		updateFromIntent();
		updateFromPreferences();
		
		jobType.setText(pType);
		jobEmail.setText(pEmail);
		jobPhone.setText(pPhone);
	}
	
	
	/*
	 * Event Listeners
	 */
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
	
	private OnCheckedChangeListener CheckBoxListener = new OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(CompoundButton btn, boolean isChecked) {
			
			int id = btn.getId();
			
			if(isChecked) {
				if(id == R.id.report_notifications_email_Checkbox) {
					emailNotification = true;
				}
				if(id == R.id.report_notifications_sms_Checkbox) {
					phoneNotification = true;
				}
			}
			else {
				if(id == R.id.report_notifications_email_Checkbox) {
					emailNotification = false;
				}
				if(id == R.id.report_notifications_sms_Checkbox) {
					phoneNotification = false;
				}
			}

		}
	};
	
	private OnClickListener ReportButtonListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			setBusy(true);
			if(v.isEnabled()){
				v.setEnabled(false);
			}
			Boolean image = false;
			if(pThumbnailImage != null){
				Log.d("315", "There is an image");
				image = true;
			}
			rp.setpDetails(jobDesc.getText().toString());
			ReportSubmitTask rst = new ReportSubmitTask();
			rst.execute(
					String.valueOf(rp.getpNum()),
					String.valueOf(rp.getpLat()),
					String.valueOf(rp.getpLng()),
					rp.getpDesc(),
					rp.getpLocation(),
					pEmail,
					pPhone 
			);
		}
		
	};
	
	private File getAlbumName() {
		File dir = new File(
			    Environment.getExternalStoragePublicDirectory(
			        Environment.DIRECTORY_PICTURES
			    ), 
			    getString(R.string.photo_album_name)
			);
		
		if(!dir.exists()){
			dir.mkdirs();
		}
        return dir;
	}
	
	public void showPhotoOptions() {
	    DialogFragment newFragment = new PhotoChooserDialogFragment();
	    newFragment.show(getSupportFragmentManager(), "photoChooser");
	}
	
	private void getImageThumbnail(){
		ProcessImageThumbnailTask imageThumb = new ProcessImageThumbnailTask();
        imageThumb.execute(currentPhotoPath);
	}

	/*
	 * Dispatch a new intent based on the option from the dialog
	 * @see uk.gov.northampton.droid.lib.PhotoChooserDialogFragment.PhotoChooserDialogListener#onFinishPhotoChooserDialog(int)
	 */
	@Override
	public void onFinishPhotoChooserDialog(int option) {
		Log.d("Finished Dialog","Option: " + option);
		if(option == CAMERA) {
			dispatchTakePictureIntent(CAMERA_REQUEST);
		}
		if(option == GALLERY) {
			dispatchChooseFromGalleryIntent(GALLERY_REQUEST);
		}
	}
	
	/*
	 * Call to the device camera to take a photo
	 */
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
	
	/*
	 * Call to the device gallery to select a photo
	 */
	private void dispatchChooseFromGalleryIntent(int actionCode){
		//TODO go get an image from the gallery - run it through the same thumbnail compression and store the uri
		Intent getImageIntent = new Intent(Intent.ACTION_PICK);
		getImageIntent.setType("image/*");
		startActivityForResult(getImageIntent, actionCode);
	}
	
	/*
	 * Handle the response from the dispatched camera / gallery intents
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
		//Log.d("Activity Result", "Code: " + requestCode + " | resultCode: " + resultCode);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {  
           getImageThumbnail(); 
        }
        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {  
        	Uri selectedImage = data.getData();
        	currentPhotoPath = getPathForGalleryImage(selectedImage);
            getImageThumbnail(); 
         }
    }
	
	/*
	 * Gallery intents don't return absolute paths, they return content paths
	 * to get the absolute path we have to grab the data from the database
	 */
	private String getPathForGalleryImage(Uri imageUri) {
		
		String path;
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(imageUri,filePathColumn, null, null, null);
        
        if(cursor == null ) {
        	path = imageUri.getPath();
        }
        else {
        	cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            path = cursor.getString(columnIndex);
            cursor.close();	
        }
        
		return path;
	}
	
	/*
	 * Write the image file to the device
	 */
	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_" + JPEG_FILE_SUFFIX;
	    File image = new File(storageDir,imageFileName);
	    currentPhotoPath = image.getAbsolutePath();
	    return image;
	}
	
	/*
	 * Resize an image based on a target width or height
	 */
	private Bitmap scalePic(int targetW, int targetH, String photoPath) {
	  
	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(photoPath, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;
	    Log.d("Photo W", ""+photoW);
	    Log.d("Photo H", ""+photoH);
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
	
	/*
	 * Process the image off the UI thread for efficiency
	 */
	private class ProcessImageThumbnailTask extends AsyncTask<String, Void, Bitmap>{

		@Override
		protected Bitmap doInBackground(String... params) {
			int viewW = addPhoto.getWidth();
			Log.d("Width","Factor: " + viewW);
			int viewH = addPhoto.getHeight();
			Log.d("Height","Factor: " + viewH);
			
			Bitmap mBitmap = scalePic(viewW,viewH,currentPhotoPath); //BitmapFactory.decodeFile(currentPhotoPath, bmThumbFactory); //TODO add compression parameters
			return 	mBitmap; 
		}

		@Override
		protected void onPostExecute(final Bitmap image){
			pThumbnailImage = image;
			showThumbnail(pThumbnailImage);
		}

	}
	
	/*
	 * Show the thumbnail image rather than the placeholder
	 */
	private void showThumbnail(Bitmap image){
		if(image.getHeight() > 0){
			addPhoto.setVisibility(View.GONE);
			jobPhoto.setVisibility(View.VISIBLE);
			jobPhoto.setImageBitmap(image);
		}
	}
	

	/*
	 * Helper method to switch the progress bar on or off
	 */
	private void setBusy(Boolean b){
		setSupportProgressBarIndeterminateVisibility(b);
	}
	
	/*
	 * Submits the problem to an HTTP Sender
	 * off the UI thread for efficiency
	 */
	private class ReportSubmitTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			
			Log.d("SUBMITTING REPORT","DO IN BG");
			//set up the http request and set the attributes of the report object
			ReportHttpSender rs = new ReportHttpSender();
			rs.setDataSource(getString(R.string.data_source));
			rs.setDeviceID("12345");
			rs.setProblemNumber(params[0]);
			rs.setLat(params[1]);
			rs.setLng(params[2]);
			rs.setDesc(params[3]);
			rs.setLocation(params[4]);
			rs.setEmail(params[5]);
			rs.setPhone(params[6]); 
			
			//check 'image' parameter
			//Boolean image = Boolean.parseBoolean(params[2]);
			
			//if image is true and there is a thumbnail image to send, compress the image and attach to the request
			if(pThumbnailImage != null){
				ByteArrayOutputStream baos = new ByteArrayOutputStream(); // set up an output object  
				pThumbnailImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); // compress the image   
				byte[] b = baos.toByteArray(); // convert the output stream
				rs.setImageData(b); // add the byte array to the request object
			}
			
			//make the http request
			String result = rs.send(getString(R.string.mycouncil_url) + getString(R.string.report_url));
			return result;
		}
		
		@Override
		protected void onPostExecute(final String result){
			// the problem has been send to the server
			setBusy(false); // stop the spinner 
			if(result != null){ // if the result is successful
				ConfirmationRetriever cr = new ConfirmationRetriever(); // set up an object to process the confirmation
				Confirmation conf = cr.retrieveConfirmation(result); // process the XML result
				Intent confIntent = new Intent(getApplicationContext(),ReportConfirmation.class); // set the new intent to the confirmation view
				confIntent.putExtra("result", conf); // push the confirmation through to the new intent
				startActivity(confIntent); // start the intent
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

	@Override
	public void onPhotoRemoved(String photoPath) {
		// TODO Auto-generated method stub
		Log.d("Photo Removed","PHOTO REMOVED");
	}
	
	
	
}
