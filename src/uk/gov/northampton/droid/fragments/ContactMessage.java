package uk.gov.northampton.droid.fragments;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.ContactReason;
import uk.gov.northampton.droid.ContactReasonsAdapter;
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

public class ContactMessage extends Activity {
	
	private Spinner spinner;
	private ContactReason selectedSubject;
	private ContactReason selectedReason;
	private ContactReason selectedType;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_select_reason);
		
	}
}
