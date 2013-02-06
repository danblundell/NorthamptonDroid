package uk.gov.northampton.droid.fragments;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;

import uk.gov.northampton.droid.Confirmation;
import uk.gov.northampton.droid.MainActivity;
import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.lib.PostCodeDialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ContactConfirmation extends SherlockActivity {
	
	private TextView callNumber;
	private TextView callDescription;
	private TextView callDueDate;
	private Button doneButton;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_3_confirmation);
		ActionBar ab = getSupportActionBar();
		ab.setTitle(R.string.conf_details_title);
		
		Log.d("Confirmation Activity","Created");
		
		findAllViewsById();
		doneButton.setOnClickListener(doneButtonListener);
		
		Confirmation conf = (Confirmation) getIntent().getSerializableExtra("result");
		
		if(conf.isSuccess()){
			callNumber.setText(conf.getCallNumber());
			callDueDate.setText(conf.getSla());
		}
	}
	
	private void findAllViewsById(){
		callNumber = (TextView) this.findViewById(R.id.contact_conf_case_ref_TextView);
		callDescription = (TextView) this.findViewById(R.id.contact_conf_description_TextView);
		callDueDate = (TextView) this.findViewById(R.id.contact_conf_due_date_TextView);
		doneButton = (Button) this.findViewById(R.id.contact_done_button);
	}
	
	private OnClickListener doneButtonListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
		}
		
	};
}
