package uk.gov.northampton.droid.fragments;

import java.io.InputStream;
import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;

import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.ReportProblem;
import uk.gov.northampton.droid.ReportProblemAdapter;
import uk.gov.northampton.droid.fragments.ReportLocation;
import uk.gov.northampton.droid.lib.ReportProblemsRetriever;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class Report extends SherlockFragment implements OnItemSelectedListener {
	
	String reportType;
	private ReportProblemsRetriever rpr = new ReportProblemsRetriever();
	private ReportProblem rp;
	private Spinner spinner;
	
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
		spinner = (Spinner) view.findViewById(R.id.report_reasons_spinner);
		Button selectBtn = (Button) view.findViewById(R.id.report_reasons_button);
		
		selectBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Report Fragment","Button Clicked!");
				Intent rptIntent = new Intent(v.getContext(),ReportLocation.class);
				rptIntent.putExtra("type", rp);
				//rptIntent.putExtra("type", reportType);
				startActivity(rptIntent);
			}
			
		});
		
		RetrieveProblemListTask sf = new RetrieveProblemListTask();
		sf.execute();
		
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
		rp = (ReportProblem) parent.getItemAtPosition(pos);
		reportType = rp.getpDesc();
		Log.d("Item Selected",reportType);
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private class RetrieveProblemListTask extends AsyncTask<InputStream, Void, ArrayList<ReportProblem>>{

		@Override
		protected ArrayList<ReportProblem> doInBackground(InputStream... params) {
			//get xml file and process to array list of objects
			Resources res = getResources();
			InputStream is = res.openRawResource(R.raw.problems);
			return 	rpr.retrieveProblemList(is);
		}
		
		@Override
		protected void onPostExecute(final ArrayList<ReportProblem> result){
			//populate contents of the arraylist to the spinner
			SpinnerAdapter mySpinnerAdapter = new ReportProblemAdapter(result, getActivity());
			spinner.setAdapter(mySpinnerAdapter);
			spinner.setOnItemSelectedListener(Report.this);
		}
	}
	
	
}
