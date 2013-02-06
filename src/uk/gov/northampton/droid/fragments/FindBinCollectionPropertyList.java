package uk.gov.northampton.droid.fragments;

import java.util.ArrayList;

import uk.gov.northampton.droid.Property;
import uk.gov.northampton.droid.PropertyAdapter;
import uk.gov.northampton.droid.R;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.actionbarsherlock.app.SherlockFragmentActivity;

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
			Log.i("NEW PROP SELECTED",selectedProperty.getAddress());
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			selectedProperty = (Property) propertiesSpinner.getItemAtPosition(0);
		}
		
	};
}
