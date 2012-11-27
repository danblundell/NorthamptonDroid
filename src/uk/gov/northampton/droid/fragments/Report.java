package uk.gov.northampton.droid.fragments;

import com.actionbarsherlock.app.SherlockFragment;

import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.fragments.ReportLocation;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Report extends SherlockFragment implements OnItemSelectedListener {
	
	String reportType;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("Report Activity","Created");
		
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.report_job_type, container, false);
		
		Spinner spinner = (Spinner) view.findViewById(R.id.report_reasons_spinner);
		
		Button selectBtn = (Button) view.findViewById(R.id.report_reasons_button);
		
		selectBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Report Fragment","Button Clicked!");
				Intent rptIntent = new Intent(v.getContext(),ReportLocation.class);
				rptIntent.putExtra("type", reportType);
				startActivity(rptIntent);
			}
			
		}
		);
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
		        R.array.planets_array, android.R.layout.simple_spinner_item);
		
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		reportType = (String) spinner.getItemAtPosition(0);
        return view;
    }

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("Report Tab", "Paused!!");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("Report Tab", "Resumed!!");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d("Report Tab", "Started!!");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d("Report Tab", "Stopped!!");
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		reportType = (String) parent.getItemAtPosition(pos);
		Log.d("Item Selected",reportType);
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
