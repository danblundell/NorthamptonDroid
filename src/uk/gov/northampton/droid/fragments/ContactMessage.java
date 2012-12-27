package uk.gov.northampton.droid.fragments;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.ContactReason;
import uk.gov.northampton.droid.ContactReasonsAdapter;
import uk.gov.northampton.droid.lib.ContactHttpSender;
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
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class ContactMessage extends Activity {
	
	private Spinner spinner;
	private ContactReason selectedSubject;
	private ContactReason selectedReason;
	private ContactReason selectedType;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_message);
		
		TextView subjectTV = (TextView) findViewById(R.id.contactSubject);
		Button sendBtn = (Button) findViewById(R.id.contactSubmitButton);
		selectedSubject = (ContactReason) getIntent().getSerializableExtra("contactSubject");
		selectedReason = (ContactReason) getIntent().getSerializableExtra("contactReason");
		selectedType = (ContactReason) getIntent().getSerializableExtra("contactType");
		subjectTV.setText(selectedType.geteDesc());
		
		sendBtn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("CONTACT","Sending Message!");
				SendMessageTask sm = new SendMessageTask();
				sm.execute();
			}
			
		});
		
	}
	
	private class SendMessageTask extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... params) {
			//get httpsender and send data
			ContactHttpSender chs = new ContactHttpSender("myNBC","12345","dblundell@northampton.gov.uk","07580529666",selectedSubject.getiDesc(),selectedReason.getiDesc(),selectedType.getiDesc(),"NN1 1DE","Dan Blundell","Test message");
			String result = chs.send("http://172.17.11.167:8080/mycouncil/CreateContact");
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
			}
			//otherwise, error.
		}
	}
}
