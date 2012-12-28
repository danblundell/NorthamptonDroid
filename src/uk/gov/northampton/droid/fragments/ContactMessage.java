package uk.gov.northampton.droid.fragments;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.ContactReason;
import uk.gov.northampton.droid.lib.ContactHttpSender;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class ContactMessage extends SherlockActivity {
	
	private Spinner spinner;
	private ContactReason selectedSubject;
	private ContactReason selectedReason;
	private ContactReason selectedType;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_message);
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
		
		Button sendBtn = (Button) findViewById(R.id.contactSubmitButton);
		selectedSubject = (ContactReason) getIntent().getSerializableExtra("contactSubject");
		selectedReason = (ContactReason) getIntent().getSerializableExtra("contactReason");
		selectedType = (ContactReason) getIntent().getSerializableExtra("contactType");
		
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
			}
			//otherwise, error.
		}
	}
}
