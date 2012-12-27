package uk.gov.northampton.droid.fragments;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import uk.gov.northampton.droid.ContactReason;
import uk.gov.northampton.droid.ContactReasonsAdapter;
import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.lib.ContactReasonsRetriever;
import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class Contact extends Activity {
	
	private ContactReasonsRetriever crr = new ContactReasonsRetriever();
	private Spinner spinner;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_select_reason);
		spinner = (Spinner) findViewById(R.id.contact_reasons1_spinner);
		RetrieveContactReasonsTask sf = new RetrieveContactReasonsTask();
		sf.execute();
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	
		    	ContactReason selectedItem = (ContactReason) spinner.getItemAtPosition(position);
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
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
