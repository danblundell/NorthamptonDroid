package uk.gov.northampton.droid.fragments;

import java.util.ArrayList;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import uk.gov.northampton.droid.Confirmation;
import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.ContactReason;
import uk.gov.northampton.droid.lib.ConfirmationRetriever;
import uk.gov.northampton.droid.lib.ContactHttpSender;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ContactMessage extends SherlockActivity {
	
	private EditText name;
	private EditText email;
	private EditText phone;
	private EditText message;
	private Button sendBtn;
	private ContactReason selectedSubject;
	private ContactReason selectedReason;
	private ContactReason selectedType;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_2_message);
		ActionBar ab = getSupportActionBar();
		ab.setTitle(getString(R.string.contact_type));
		
		ImageView step1 = (ImageView) findViewById(R.id.contactProgress1ImageView);
		step1.setImageResource(R.drawable.progress_step_done);
		ImageView step2 = (ImageView) findViewById(R.id.contactProgress2ImageView);
		step2.setImageResource(R.drawable.progress_step_done);
		ImageView step3 = (ImageView) findViewById(R.id.contactProgress3ImageView);
		step3.setImageResource(R.drawable.progress_step_done);
		ImageView step4 = (ImageView) findViewById(R.id.contactProgress4ImageView);
		step4.setImageResource(R.drawable.progress_step_done);
		
		//get extras from previous intent
		selectedSubject = (ContactReason) getIntent().getSerializableExtra("contactSubject");
		selectedReason = (ContactReason) getIntent().getSerializableExtra("contactReason");
		selectedType = (ContactReason) getIntent().getSerializableExtra("contactType");
		
		this.findAllViewsById();
		this.updateUIFromPreferences();
		sendBtn.setOnClickListener(SendMessageListener);
		
	}
	
	private OnClickListener SendMessageListener = new OnClickListener(){
		public void onClick(View v){
			// TODO Auto-generated method stub
			Log.d("CONTACT","Sending Message!");
			boolean send = validateForm();
			if(send){
				SendMessageTask sm = new SendMessageTask();

				sm.execute(
						selectedSubject.getiDesc(),
						selectedReason.getiDesc(),
						selectedType.getiDesc(),
						name.getText().toString(),
						message.getText().toString(),
						email.getText().toString(),
						phone.getText().toString()
				);
			}
		}
	};
	
	private void findAllViewsById(){
		name = (EditText) findViewById(R.id.contactNameEditText);
		email = (EditText) findViewById(R.id.contactEmailEditText);
		phone = (EditText) findViewById(R.id.contactPhoneEditText);
		message = (EditText) findViewById(R.id.contactMessageEditText);
		sendBtn = (Button) findViewById(R.id.contactSubmitButton);
	}
	
	private void updateUIFromPreferences(){
		Context context = getApplicationContext();
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		name.setText(sharedPrefs.getString(Settings.NBC_NAME, getString(R.string.contact_name)));
		email.setText(sharedPrefs.getString(Settings.NBC_EMAIL, getString(R.string.contact_email)));
		phone.setText(sharedPrefs.getString(Settings.NBC_TEL, getString(R.string.contact_phone)));
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
					"12345",
					params[5],
					params[6],
					params[0],
					params[1],
					params[2],
					"NN1 1DE",
					params[3],
					params[4]
			);
			String result = chs.send(getString(R.string.mycouncil_url) + getString(R.string.contact_url));
			//String result = "Done";
			return 	result;
		}
		
		protected void onProgressUpdate(Integer... progress) {
	         //do action on progress
	     }
		
		@Override
		protected void onPostExecute(String result){
			
			//if successful, forward to confirmation screen
			if(result != null){
				Log.d("CONTACT PE",result);
				ConfirmationRetriever cr = new ConfirmationRetriever();
				Confirmation conf = cr.retrieveConfirmation(result);
				Intent confIntent = new Intent(getApplicationContext(),ConfirmationActivity.class);
				confIntent.putExtra("result", conf);
				startActivity(confIntent);
				//Toast.makeText(getApplicationContext(), "Case Ref: " + conf.getCallNumber(), Toast.LENGTH_LONG).show();
			}
			//otherwise, error.
			if(!sendBtn.isClickable()){
				sendBtn.setClickable(true);
			}
		}
	}
	
	private boolean validateForm(){
		if(validateName() && validateEmail() && validatePhone() && validateMessge()){
			return true;
		}
		return false;
	}

	private boolean validateMessge() {
		String m = message.getText().toString();
		if(!m.isEmpty()){
			return true;
		}
		return false;
	}

	private boolean validatePhone() {
		String p  = phone.getText().toString();
		if(!p.isEmpty()){
			return true;
		}
		return false;
	}

	private boolean validateEmail() {
		String e = email.getText().toString();
		if(!e.isEmpty()){
			return true;
		}
		return false;
	}

	private boolean validateName() {
		String n = name.getText().toString();
		if(!n.isEmpty()){
			return true;
		}
		return false;
	}
	
}
