package uk.gov.northampton.droid.fragments;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;

import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.ReportProblem;
import uk.gov.northampton.droid.ReportProblemAdapter;
import uk.gov.northampton.droid.fragments.ReportLocation;
import uk.gov.northampton.droid.lib.ReportProblemsRetriever;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class Report extends SherlockFragment implements OnItemSelectedListener{
	
	private ReportProblemsRetriever rpr = new ReportProblemsRetriever();
	private ArrayList<ReportProblem> reportProblemList;
	private ReportProblem rp;
	private Spinner spinner;
	private Button selectBtn;
	
	private int selectedProblem = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	public void getProblemList(){
		if(reportProblemList != null){
			updateUI();
		}
		else{
			RetrieveProblemListTask rpt = new RetrieveProblemListTask();
			rpt.execute();
		}
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.report_1_type, container, false);
		findAllViewsById(view);
		getProblemList();
		setAllListeners();
        return view;
    }
	
	public void findAllViewsById(View v){
		spinner = (Spinner) v.findViewById(R.id.report_reasons_spinner);
		selectBtn = (Button) v.findViewById(R.id.report_reasons_button);
	}
	
	public void setAllListeners(){
		selectBtn.setOnClickListener(selectButtonListener);
	}
	
	private OnClickListener selectButtonListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			if(rp != null){
				Intent rptIntent = new Intent(v.getContext(),ReportLocation.class);
				rptIntent.putExtra("type", rp);
				startActivity(rptIntent);
			}else{
				//error
			}
		}
	};

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		selectedProblem = pos;
		rp = (ReportProblem) parent.getItemAtPosition(this.selectedProblem);
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
			reportProblemList = result;
			updateUI();
		}
	}
	
	private void updateUI(){
		SpinnerAdapter mySpinnerAdapter = new ReportProblemAdapter(reportProblemList, getActivity());
		spinner.setAdapter(mySpinnerAdapter);
		spinner.setOnItemSelectedListener(this);
	}	
}
