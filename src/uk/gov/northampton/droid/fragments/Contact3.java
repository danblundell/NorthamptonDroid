package uk.gov.northampton.droid.fragments;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.ContactReason;
import uk.gov.northampton.droid.ContactReasonsAdapter;
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

public class Contact3 extends SherlockActivity {
	
	private Spinner spinner;
	private ContactReason selectedSubject;
	private ContactReason selectedReason;
	private ContactReason selectedType;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_1_reason);
		ActionBar ab = getSupportActionBar();
		ab.setTitle(getString(R.string.contact_type));
		
		selectedSubject = (ContactReason) getIntent().getSerializableExtra("contactSubject");
		selectedReason = (ContactReason) getIntent().getSerializableExtra("contactReason");
		ArrayList<ContactReason> selectedSubjectType = selectedReason.getReasons();
		spinner = (Spinner) findViewById(R.id.contact_reasons1_spinner);
		
		ImageView step1 = (ImageView) findViewById(R.id.contact_step_1);
		step1.setImageResource(R.drawable.progress_line_done);
		ImageView step2 = (ImageView) findViewById(R.id.contact_step_2);
		step2.setImageResource(R.drawable.progress_line_done);
		ImageView step3 = (ImageView) findViewById(R.id.contact_step_3);
		step3.setImageResource(R.drawable.progress_line_current);
		
		TextView title = (TextView) findViewById(R.id.contact_reasons_title_desc_TextView);
		title.setText(getString(R.string.contact_type_title));
		
		Button nextBtn = (Button) findViewById(R.id.contact_reasons1_button);
		
		//populate contents of the arraylist to the spinner
		SpinnerAdapter mySpinnerAdapter = new ContactReasonsAdapter(selectedSubjectType, Contact3.this);
		spinner.setAdapter(mySpinnerAdapter);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	
		    	selectedType = (ContactReason) spinner.getItemAtPosition(position);
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
				Log.d("Contact Reason Activity","Next Button Clicked!");
				if(selectedType != null){
					//start next activity
					Intent contactIntent = new Intent(v.getContext(),ContactMessage.class);
					contactIntent.putExtra("contactSubject", selectedSubject);
					contactIntent.putExtra("contactReason", selectedReason);
					contactIntent.putExtra("contactType", selectedType);
					startActivity(contactIntent);
				}
				else{
					//show error
				}
			}
			
		});
	}
}
