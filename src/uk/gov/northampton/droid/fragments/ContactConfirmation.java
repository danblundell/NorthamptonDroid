package uk.gov.northampton.droid.fragments;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;

import uk.gov.northampton.droid.Confirmation;
import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.lib.PostCodeDialogFragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ContactConfirmation extends SherlockActivity {
	
	private TextView callNumber;
	private TextView callDescription;
	private TextView callDueDate;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_3_confirmation);
		ActionBar ab = getSupportActionBar();
		ab.setTitle(R.string.conf_details_title);
		
		Log.d("Confirmation Activity","Created");
		
		findAllViewsById();
		
		Confirmation conf = (Confirmation) getIntent().getSerializableExtra("result");
		
		if(conf.isSuccess()){
			callNumber.setText(conf.getCallNumber());
			callDueDate.setText(conf.getSla());
		}
	}
	
	private void findAllViewsById(){
		callNumber = (TextView) this.findViewById(R.id.conf_case_ref_TextView);
		callDescription = (TextView) this.findViewById(R.id.conf_description_TextView);
		callDueDate = (TextView) this.findViewById(R.id.conf_description_bottom_TextView);
	}
}
