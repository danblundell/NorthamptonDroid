package uk.gov.northampton.droid.fragments;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import uk.gov.northampton.droid.ContactReason;
import uk.gov.northampton.droid.ContactReasonsAdapter;
import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.lib.ContactReasonsRetriever;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class Contact extends SherlockActivity {
	
	private ContactReasonsRetriever crr = new ContactReasonsRetriever();
	private Spinner spinner;
	private ContactReason selectedSubject;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_1_reason);
		
		ActionBar ab = getSupportActionBar();
		ab.setTitle(getString(R.string.contact_type));
		
		
		spinner = (Spinner) findViewById(R.id.contact_reasons1_spinner);
		
		ImageView step1 = (ImageView) findViewById(R.id.contact_step_1);
		step1.setImageResource(R.drawable.progress_line_current);

		TextView title = (TextView) findViewById(R.id.contact_reasons_title_desc_TextView);
		title.setText(getString(R.string.contact_subject_title));
		
		Button nextBtn = (Button) findViewById(R.id.contact_reasons1_button);
		
		RetrieveContactReasonsTask sf = new RetrieveContactReasonsTask();
		sf.execute();
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	
		    	selectedSubject = (ContactReason) spinner.getItemAtPosition(position);
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }

		});
		
		nextBtn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Contact Activity","Next Button Clicked!");
				if(selectedSubject != null){
					//start next activity
					Intent contactIntent = new Intent(v.getContext(),Contact2.class);
					contactIntent.putExtra("contactSubject", selectedSubject);
					startActivity(contactIntent);
				}
				else{
					//show error
				}
			}
			
		});
	}
	
	private class RetrieveContactReasonsTask extends AsyncTask<InputStream, Void, ArrayList<ContactReason>>{

		@Override
		protected ArrayList<ContactReason> doInBackground(InputStream... params) {
			//get xml file and process to array list of objects
			Resources res = getResources();
			InputStream is = res.openRawResource(R.raw.contacts);
			return 	crr.retrieveContactReasons(is);
		}
		
		@Override
		protected void onPostExecute(final ArrayList<ContactReason> result){
			
			//sort results alphabetically
			Collections.sort(result);
			
			//populate contents of the arraylist to the spinner
			SpinnerAdapter mySpinnerAdapter = new ContactReasonsAdapter(result, Contact.this);
			spinner.setAdapter(mySpinnerAdapter);
		}
	}
}
