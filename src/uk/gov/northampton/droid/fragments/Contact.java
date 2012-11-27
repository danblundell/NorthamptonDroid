package uk.gov.northampton.droid.fragments;

import java.io.InputStream;
import java.util.ArrayList;

import uk.gov.northampton.droid.ContactReason;
import uk.gov.northampton.droid.ContactReasonsAdapter;
import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.lib.ContactReasonsRetriever;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class Contact extends Activity {
	
	private ContactReasonsRetriever crr = new ContactReasonsRetriever();
	private Spinner spinner;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_1);
		spinner = (Spinner) findViewById(R.id.contact_reasons1_spinner);
		
		RetrieveContactReasonsTask sf = new RetrieveContactReasonsTask();
		sf.execute();  	
	}
	
	private class RetrieveContactReasonsTask extends AsyncTask<InputStream, Void, ArrayList<ContactReason>>{

		@Override
		protected ArrayList<ContactReason> doInBackground(InputStream... params) {
			InputStream is = getResources().openRawResource(R.raw.contacts);
			return 	crr.retrieveContactReasons(is);
		}
		
		@Override
		protected void onPostExecute(final ArrayList<ContactReason> result){
			SpinnerAdapter mySpinnerAdapter = new ContactReasonsAdapter(Contact.this,R.id.contact_reason_text, result);
			spinner.setAdapter(mySpinnerAdapter);
		}
	}
}
