package uk.gov.northampton.droid.fragments;

import java.lang.reflect.Method;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.Window;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import uk.gov.northampton.droid.Confirmation;
import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.ContactReason;
import uk.gov.northampton.droid.lib.ConfirmationRetriever;
import uk.gov.northampton.droid.lib.ContactHttpSender;
import uk.gov.northampton.droid.lib.EditTextDialogFragment;
import uk.gov.northampton.droid.lib.EditTextDialogFragment.EditTextDialogListener;
import uk.gov.northampton.droid.lib.PostCodeDialogFragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactMessage extends SherlockFragmentActivity implements  EditTextDialogListener, PostCodeDialogFragment.PostCodeDialogListener {

	private EditText name;
	private EditText email;
	private EditText phone;
	private EditText message;
	private Button sendBtn;
	private ContactReason selectedSubject;
	private ContactReason selectedReason;
	private ContactReason selectedType;
	private TextView contactDesc;
	private Context context;
	private SharedPreferences sharedPrefs;
	private String pDeviceId;
	private String pPostCode;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.contact_2_message);
		setSupportProgressBarIndeterminateVisibility(false);
		ActionBar ab = getSupportActionBar();
		ab.setTitle(getString(R.string.contact_type));
		context = this;
		ImageView step1 = (ImageView) findViewById(R.id.contact_step_1);
		step1.setImageResource(R.drawable.progress_line_done);
		ImageView step2 = (ImageView) findViewById(R.id.contact_step_2);
		step2.setImageResource(R.drawable.progress_line_done);
		ImageView step3 = (ImageView) findViewById(R.id.contact_step_3);
		step3.setImageResource(R.drawable.progress_line_done);
		ImageView step4 = (ImageView) findViewById(R.id.contact_step_4);
		step4.setImageResource(R.drawable.progress_line_current);

		//get extras from previous intent
		selectedSubject = (ContactReason) getIntent().getSerializableExtra("contactSubject");
		selectedReason = (ContactReason) getIntent().getSerializableExtra("contactReason");
		selectedType = (ContactReason) getIntent().getSerializableExtra("contactType");

		this.findAllViewsById();
		this.updateUIFromExtras();
		this.updateUIFromPreferences();
		this.setListeners();		
	}

	private OnClickListener sendMessageListener = new OnClickListener(){
		public void onClick(View v){
			// May return null if a EasyTracker has not yet been initialized with a
			// property ID.
			EasyTracker easyTracker = EasyTracker.getInstance(context);

			if(easyTracker != null) {
				easyTracker.send(MapBuilder
						.createEvent(getString(R.string.ga_event_category_contact),     // Event category (required)
								getString(R.string.ga_event_transaction),  // Event action (required)
								getString(R.string.ga_event_contact_step4),   // Event label
								null)            // Event value
								.build()
						); 
			}

			sendMessage();
		}
	};

	private OnMenuItemClickListener sendMessageMenuItemListener = new OnMenuItemClickListener(){

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			return sendMessage();
		}
	};

	private boolean sendMessage(){
		boolean send = validateForm();
		if(send){
			
			if(pDeviceId == null) {
				pDeviceId = email.getText().toString();
			}
			if(pPostCode != null) {
			setSupportProgressBarIndeterminateVisibility(true);
			SendMessageTask sm = new SendMessageTask();

			sm.execute(
					pDeviceId,
					email.getText().toString(),
					phone.getText().toString(),
					selectedSubject.getiDesc(),
					selectedReason.getiDesc(),
					selectedType.getiDesc(),
					pPostCode,
					name.getText().toString(),
					message.getText().toString()
					);
			return true;
			} else {
				// post code fragment
				PostCodeDialogFragment pcdf = new PostCodeDialogFragment();
				Bundle bundle = new Bundle();
				bundle.putString("callback", "sendMessage");
				pcdf.setArguments(bundle);
				pcdf.show(getSupportFragmentManager(), "pcdf");
			}
		}
		return false;
	}

	private OnClickListener updateUserDetailListener = new OnClickListener(){
		public void onClick(View v){
			EditText editTextView = (EditText) v.findViewById(v.getId());
			String title = editTextView.getHint().toString();
			//EditTextDialogFragment(Title for the dialog, prompt,view id to return to, edit text input type);
			EditTextDialogFragment editTextFragment = new EditTextDialogFragment();
			editTextFragment.setTitle(title);
			editTextFragment.setViewId(v.getId());
			editTextFragment.setInputType(editTextView.getInputType());
			editTextFragment.show(getSupportFragmentManager(), "editText");
		}
	};

	private void updateUIFromExtras(){
		String contentDescText = selectedType.geteDesc();
		contactDesc.setText(contentDescText);
	}

	private void setListeners(){
		name.setOnClickListener(updateUserDetailListener);
		phone.setOnClickListener(updateUserDetailListener);
		email.setOnClickListener(updateUserDetailListener);
		sendBtn.setOnClickListener(sendMessageListener);
	}

	private void findAllViewsById(){
		contactDesc = (TextView) findViewById(R.id.contact_details_title_desc_TextView);
		name = (EditText) findViewById(R.id.contactNameEditText);
		email = (EditText) findViewById(R.id.contactEmailEditText);
		phone = (EditText) findViewById(R.id.contactPhoneEditText);
		message = (EditText) findViewById(R.id.contactMessageEditText);
		sendBtn = (Button) findViewById(R.id.contactSubmitButton);
	}

	private void updateUIFromPreferences(){
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		String settingName  = sharedPrefs.getString(Settings.NBC_NAME, "");
		String settingEmail = sharedPrefs.getString(Settings.NBC_EMAIL, "");
		String settingPhone = sharedPrefs.getString(Settings.NBC_TEL, "");
		pDeviceId = sharedPrefs.getString(Settings.getDeviceIdKey(), null);
		pPostCode = sharedPrefs.getString(Settings.NBC_POST_CODE, null);

		if(settingName.compareTo(getString(R.string.settings_name_add)) != 0){
			name.setText(settingName);
		}
		if(settingEmail.compareTo(getString(R.string.settings_email_add)) != 0){
			email.setText(settingEmail);
		}
		if(settingPhone.compareTo(getString(R.string.settings_telephone_add)) != 0){
			phone.setText(settingPhone);
		}
	}

	private class SendMessageTask extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... params) {

			//disable button
			if(sendBtn.isClickable()){
				sendBtn.setClickable(false);
			}

			//get httpsender and send data
			ContactHttpSender chs = new ContactHttpSender(
					getString(R.string.data_source),
					params[0], // device id
					params[1], // email
					params[2], // phone
					params[3], //subject
					params[4],// reason
					params[5],// type
					params[6], // postcode
					params[7], // name
					params[8] // message
					);
			String result = chs.send(getString(R.string.mycouncil_url) + getString(R.string.contact_url));
			//String result = "Done";
			return 	result;
		}

		@Override
		protected void onPostExecute(String result){
			setSupportProgressBarIndeterminateVisibility(false);
			
			//if successful, forward to confirmation screen
			if(result != null){
				// May return null if a EasyTracker has not yet been initialized with a
				// property ID.
				EasyTracker easyTracker = EasyTracker.getInstance(context);

				if(easyTracker != null) {
					easyTracker.send(MapBuilder
							.createEvent(getString(R.string.ga_event_category_contact),     // Event category (required)
									getString(R.string.ga_event_transaction),  // Event action (required)
									getString(R.string.ga_event_contact_step5),   // Event label
									null)            // Event value
									.build()
							); 
				}
				ConfirmationRetriever cr = new ConfirmationRetriever();
				Confirmation conf = cr.retrieveConfirmation(result);
				Intent confIntent = new Intent(getApplicationContext(),ContactConfirmation.class);
				confIntent.putExtra("result", conf);
				startActivity(confIntent);
			}
			else {
				// May return null if a EasyTracker has not yet been initialized with a
				// property ID.
				EasyTracker easyTracker = EasyTracker.getInstance(context);

				if(easyTracker != null) {
					easyTracker.send(MapBuilder
							.createEvent(getString(R.string.ga_event_category_contact),     // Event category (required)
									getString(R.string.ga_event_transaction),  // Event action (required)
									getString(R.string.ga_event_contact_step5b),   // Event label
									null)            // Event value
									.build()
							); 
				}
				
				//otherwise, error.
				Toast.makeText(getApplicationContext(), getString(R.string.contact_send_error), Toast.LENGTH_SHORT).show();
				
				sendBtn.setText(R.string.contact_send_button_again);
				if(!sendBtn.isClickable()){
					
					sendBtn.setClickable(true);
				}
			}
			
			
		}
	}

	private boolean validateForm(){
		if(validateName() && validateEmail() && validateMessge()){
			return true;
		}
		return false;
	}

	private boolean validateMessge() {
		String m = message.getText().toString();
		if(m.length() > 0){
			return true;
		}
		Toast.makeText(getApplicationContext(), getString(R.string.contact_error_message), Toast.LENGTH_SHORT).show();
		return false;
	}

//	private boolean validatePhone() {
//		String p  = phone.getText().toString();
//		if(p.length() == 0) {
//			return true;
//		}
//		Toast.makeText(getApplicationContext(), getString(R.string.contact_error_phone), Toast.LENGTH_SHORT).show();
//		return true;
//	}

	private boolean validateEmail() {
		String e = email.getText().toString();
		
		if(e.length() > 0 && android.util.Patterns.EMAIL_ADDRESS.matcher(e).matches()){
			return true;
		}
		Toast.makeText(getApplicationContext(), getString(R.string.contact_error_email), Toast.LENGTH_SHORT).show();
		return false;
	}

	private boolean validateName() {
		String n = name.getText().toString();
		if(n.length() > 0){
			return true;
		}
		Toast.makeText(getApplicationContext(), getString(R.string.contact_error_name), Toast.LENGTH_SHORT).show();
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu m){
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.contact_message_menu, m);
		MenuItem sendMessage = m.findItem(R.id.sendContactMessage);
		sendMessage.setOnMenuItemClickListener(this.sendMessageMenuItemListener);
		return true;
	}

	private void savePreferences() {
		String namePref = name.getText().toString();
		String emailPref = email.getText().toString();
		String phonePref = phone.getText().toString();

		Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		if(namePref.length() > 0) {
			editor.putString(Settings.NBC_NAME, namePref);
		}
		if(emailPref.length() > 0) {
			editor.putString(Settings.NBC_EMAIL, emailPref);
		}
		if(phonePref.length() > 0) {
			editor.putString(Settings.NBC_TEL, phonePref);
		}
		editor.commit();
	}

	@Override
	public void onFinishEditTextDialog(int opt, String string, int viewId) {
		if(opt == Activity.RESULT_OK){
			EditText vEditText = (EditText) this.findViewById(viewId);
			vEditText.setText(string);
			savePreferences();
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

	@Override
	public void onFinishPostCodeDialog(String postCode) {
		Settings.saveStringPreference(getApplicationContext(), Settings.NBC_POST_CODE, postCode);
		pPostCode = postCode;
		sendMessage();
	}



}
