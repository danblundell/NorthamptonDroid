package uk.gov.northampton.droid.fragments;

import java.util.ArrayList;

import uk.gov.northampton.droid.Property;
import uk.gov.northampton.droid.PropertyAdapter;
import uk.gov.northampton.droid.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

public class FindBinCollectionPropertyList extends SherlockFragmentActivity {

	private Spinner propertiesSpinner;
	private Button nextButton;
	private ArrayList<Property> properties;
	private Property selectedProperty;
	private static final String PROPERTIES_LIST = "PROPERTIES_LIST";
	private static final String COLLECTION_ADDRESS = "COLLECTION_ADDRESS";
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.find_it_3_bin_collection_property_list);
		findAllViewsById();
		properties = (ArrayList<Property>) getIntent().getSerializableExtra(PROPERTIES_LIST);
		if(properties.size() > 0){
			updateSpinnerContent(properties);
		}
		nextButton.setOnClickListener(nextButtonClickListener);
	}
	
	private void findAllViewsById(){
		propertiesSpinner = (Spinner) this.findViewById(R.id.findit_properties_spinner);
		nextButton = (Button) this.findViewById(R.id.findit_bin_collection_props_button);
	}
	
	private OnClickListener nextButtonClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// May return null if a EasyTracker has not yet been initialized with a
			// property ID.
			EasyTracker easyTracker = EasyTracker.getInstance(getApplicationContext());

			if(easyTracker != null) {
				easyTracker.send(MapBuilder
						.createEvent(getString(R.string.ga_event_category_find),     // Event category (required)
								getString(R.string.ga_event_transaction),  // Event action (required)
								getString(R.string.ga_event_find_step2b),   // Event label
								null)            // Event value
								.build()
						); 
			}
			
			Intent i = new Intent(getApplicationContext(),FindBinCollectionResult.class);
			i.putExtra(COLLECTION_ADDRESS, selectedProperty);
			startActivity(i);
		}
		
	};
	
	
	private void updateSpinnerContent(ArrayList<Property> list){
		SpinnerAdapter propertiesSpinnerAdapter = new PropertyAdapter(this, list);
		propertiesSpinner.setAdapter(propertiesSpinnerAdapter);
		propertiesSpinner.setOnItemSelectedListener(propertySelectionListener);
	}
	
	private OnItemSelectedListener propertySelectionListener = new OnItemSelectedListener(){

		@Override
		public void onItemSelected(AdapterView<?> parent, View item, int pos, long id) {
			selectedProperty = (Property) propertiesSpinner.getItemAtPosition(pos);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			selectedProperty = (Property) propertiesSpinner.getItemAtPosition(0);
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
		EasyTracker.getInstance(this).activityStop(this);
	}
}
