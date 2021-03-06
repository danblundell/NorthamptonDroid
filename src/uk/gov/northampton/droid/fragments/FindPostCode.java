package uk.gov.northampton.droid.fragments;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import uk.gov.northampton.droid.Property;
import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.lib.BinCollectionsRetriever;
import uk.gov.northampton.droid.lib.PostCodeDialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FindPostCode extends SherlockFragmentActivity implements PostCodeDialogFragment.PostCodeDialogListener {

	private Button nextBtn;
	private EditText postCodeEditText;

	private static final String PROPERTIES_LIST = "PROPERTIES_LIST";
	private static final String COLLECTION_ADDRESS = "COLLECTION_ADDRESS";
	private static final String POSTCODE = "POSTCODE";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setSupportProgressBarIndeterminateVisibility(false);
		setContentView(R.layout.find_it_2_postcode);
		findAllViewsById();
		updateUIFromPreferences();
		nextBtn.setOnClickListener(nextButtonClickListener);
		postCodeEditText.setOnClickListener(editPostCodeClickListener);

	}

	private OnClickListener nextButtonClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			if(postCodeEditText.getText().length() > 0){
				// May return null if a EasyTracker has not yet been initialized with a
				// property ID.
				EasyTracker easyTracker = EasyTracker.getInstance(getApplicationContext());

				if(easyTracker != null) {
					easyTracker.send(MapBuilder
							.createEvent(getString(R.string.ga_event_category_find),     // Event category (required)
									getString(R.string.ga_event_transaction),  // Event action (required)
									getString(R.string.ga_event_find_step2),   // Event label
									null)            // Event value
									.build()
							); 
				}

				setSupportProgressBarIndeterminateVisibility(true);
				RetrieveBinCollectionsTask rbc = new RetrieveBinCollectionsTask();
				rbc.execute();
			}
			else{
				Toast.makeText(getApplicationContext(), "Please enter a Post Code", Toast.LENGTH_SHORT).show();
			}

		}
	};

	private OnClickListener editPostCodeClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			if(postCodeEditText.getText().toString().length() > 0) {
				showPostCodeOptions(postCodeEditText.getText().toString());
			}
			else {
				showPostCodeOptions(null);
			}

		}
	};

	private void findAllViewsById(){
		postCodeEditText = (EditText) findViewById(R.id.findit_post_code_preview_EditText);
		nextBtn = (Button) findViewById(R.id.findit_button);
	}

	private void updateUIFromPreferences(){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String pcStr = sharedPrefs.getString(Settings.NBC_POST_CODE, "");
		if(pcStr.length() > 0){
			postCodeEditText.setText(pcStr);
		}
		else{
			showPostCodeOptions(null);
		}		
	}

	private void showPostCodeOptions(String pc) {
		DialogFragment newFragment = new PostCodeDialogFragment();

		if(pc != null){
			Bundle args = new Bundle();
			args.putString(POSTCODE, pc);
			newFragment.setArguments(args);
		}

		newFragment.show(getSupportFragmentManager(), "postCode");
	}

	@Override
	public void onFinishPostCodeDialog(String postCode) {
		if(postCode != null){
			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
			Editor editor = sharedPrefs.edit();
			editor.putString(Settings.NBC_POST_CODE, postCode);
			editor.commit();
			updateUIFromPreferences();
		}
	}

	private class RetrieveBinCollectionsTask extends AsyncTask<String, Void, ArrayList<Property>>{

		@Override
		protected ArrayList<Property> doInBackground(String... params) {
			BinCollectionsRetriever bcr = new BinCollectionsRetriever();
			String url = getString(R.string.mycouncil_url) + getString(R.string.bin_collections_url) + Uri.encode(postCodeEditText.getText().toString());
			return 	bcr.retrieveBinCollections(url, getApplicationContext());
		}

		@Override
		protected void onPostExecute(final ArrayList<Property> result){
			//run intent to next activity
			setSupportProgressBarIndeterminateVisibility(false);
			try {
				if(result.size() > 0){
					if(result.size() > 1){
						Intent i = new Intent(getApplicationContext(),FindBinCollectionPropertyList.class);
						i.putExtra(PROPERTIES_LIST, result);
						startActivity(i);
					}
					else{
						Intent i = new Intent(getApplicationContext(),FindBinCollectionResult.class);
						i.putExtra(COLLECTION_ADDRESS, (Property) result.get(0));
						startActivity(i);
					}
				}
				else{
					Toast.makeText(getApplicationContext(), "Please check your Post Code", Toast.LENGTH_SHORT).show();
				}
			} catch (NullPointerException e) {
				Toast.makeText(getApplicationContext(), "Please check your Post Code", Toast.LENGTH_SHORT).show();
			}
		}
	}

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
