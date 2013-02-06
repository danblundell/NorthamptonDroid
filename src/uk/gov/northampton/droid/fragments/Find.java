package uk.gov.northampton.droid.fragments;

import java.util.ArrayList;
import com.actionbarsherlock.app.SherlockFragment;

import uk.gov.northampton.droid.Property;
import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.lib.BinCollectionsRetriever;
import uk.gov.northampton.droid.lib.PostCodeDialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.util.Log;
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
	private Button editDetailBtn;
	private TextView editDetailTextView;
	private int selectedItem;
	
	private static final String PROPERTIES_LIST = "PROPERTIES_LIST";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("Find Activity","Created");
		
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
			Log.d("Find Fragment","Button Clicked!");
			Intent i = new Intent(getActivity(), FindPostCode.class);
			startActivity(i);
		}
	};
	
	private void findAllViewsById(View v){
		spinner = (Spinner) v.findViewById(R.id.findit_reasons_spinner);
		nextBtn = (Button) v.findViewById(R.id.findit_button);
	}
	
//	private void updateUIFromPreferences(){
//		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//		String pcStr = sharedPrefs.getString(Settings.NBC_POST_CODE, getString(R.string.postcode_dialog_title));
//		editDetailTextView.setText(pcStr);
//		if(pcStr.equalsIgnoreCase(getString(R.string.postcode_dialog_title))){
//			editDetailBtn.setText("Add");
//		}
//		
//	}
	
//	private void showPostCodeOptions() {
//	    DialogFragment newFragment = new PostCodeDialogFragment();
//	    newFragment.show(getActivity().getSupportFragmentManager(), "postCode");
//	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
		selectedItem = pos;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
//	private class RetrieveBinCollectionsTask extends AsyncTask<String, Void, ArrayList<Property>>{
//
//		@Override
//		protected ArrayList<Property> doInBackground(String... params) {
//			Log.d("BIN FINDER", "Getting Bin Collections");
//			BinCollectionsRetriever bcr = new BinCollectionsRetriever();
//			String url = getString(R.string.mycouncil_url) + getString(R.string.bin_collections_url) + "NN48QZ";
//			return 	bcr.retrieveBinCollections(url);
//		}
//		
//		@Override
//		protected void onPostExecute(final ArrayList<Property> result){
//			//run intent to next activity
//			Log.i("BIN RESULTS","done");
//			if(result != null){
//				Intent i = new Intent(getActivity(), FindBinCollectionPropertyList.class);
//				i.putExtra(PROPERTIES_LIST, result);
//				startActivity(i);
//			}
//		}
//	}


	
}
