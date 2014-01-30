package uk.gov.northampton.droid.fragments;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import uk.gov.northampton.droid.Confirmation;
import uk.gov.northampton.droid.MainActivity;
import uk.gov.northampton.droid.R;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ReportConfirmation extends SherlockActivity {
	
	private TextView callNumber;
	private TextView callDueDate;
	private Button doneButton;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_5_confirmation);
		ActionBar ab = getSupportActionBar();
		ab.setTitle(R.string.conf_details_title);
		
		
		findAllViewsById();
		
		Confirmation conf = (Confirmation) getIntent().getSerializableExtra("result");
		
		if(conf.isSuccess()){
			callNumber.setText(conf.getCallNumber());
			callDueDate.setText(conf.getSla());
		}
		doneButton.setOnClickListener(doneButtonListener);
	}
	
	private void findAllViewsById(){
		callNumber = (TextView) this.findViewById(R.id.conf_case_ref_TextView);
		callDueDate = (TextView) this.findViewById(R.id.conf_due_date_TextView);
		doneButton = (Button) this.findViewById(R.id.reportDoneButton);
	}
	
	private OnClickListener doneButtonListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			
			// May return null if a EasyTracker has not yet been initialized with a
			// property ID.
			EasyTracker easyTracker = EasyTracker.getInstance(getApplicationContext());
			
			if(easyTracker != null) {
				easyTracker.send(MapBuilder
						.createEvent(getString(R.string.ga_event_category_report),     // Event category (required)
								getString(R.string.ga_event_transaction),  // Event action (required)
								getString(R.string.ga_event_report_step4),   // Event label
								null)            // Event value
								.build()
						); 
			}
			
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
		}
		
	};
	
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
}
