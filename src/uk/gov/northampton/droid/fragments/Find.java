package uk.gov.northampton.droid.fragments;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import uk.gov.northampton.droid.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class Find extends SherlockFragment implements OnItemSelectedListener {
	
	private Button nextBtn;
	private Spinner spinner;
	private int selectedItem;
	
	//private static final String PROPERTIES_LIST = "PROPERTIES_LIST";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.find_it_1_main, container, false);
        findAllViewsById(v);
        //updateUIFromPreferences();
        nextBtn.setOnClickListener(nextButtonClickListener);
        //editDetailBtn.setOnClickListener(editDetailButtonClickListener);
        ArrayAdapter<CharSequence> sa = ArrayAdapter.createFromResource(getActivity(), R.array.findit_type_array, R.layout.spinner_single_textview);
        sa.setDropDownViewResource(R.layout.spinner_single_textview_padded);
        spinner.setAdapter(sa);
        return v;
	}
	
	private OnClickListener nextButtonClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			// May return null if a EasyTracker has not yet been initialized with a
			// property ID.
			EasyTracker easyTracker = EasyTracker.getInstance(getActivity().getApplicationContext());

			if(easyTracker != null) {
				easyTracker.send(MapBuilder
						.createEvent(getString(R.string.ga_event_category_find),     // Event category (required)
								getString(R.string.ga_event_transaction),  // Event action (required)
								getString(R.string.ga_event_find_step1),   // Event label
								null)            // Event value
								.build()
						); 
			}
			
			Intent i = new Intent(getActivity(), FindPostCode.class);
			startActivity(i);
		}
	};
	
	private void findAllViewsById(View v){
		spinner = (Spinner) v.findViewById(R.id.findit_reasons_spinner);
		nextBtn = (Button) v.findViewById(R.id.findit_button);
	}
	

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
		selectedItem = pos;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
