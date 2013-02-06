package uk.gov.northampton.droid.fragments;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragmentActivity;

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
import android.util.Log;
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
			showPostCodeOptions();
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
			showPostCodeOptions();
		}		
	}
	
	private void showPostCodeOptions() {
	    DialogFragment newFragment = new PostCodeDialogFragment();
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
			Log.d("BIN FINDER", "Getting Bin Collections");
			BinCollectionsRetriever bcr = new BinCollectionsRetriever();
			String url = getString(R.string.mycouncil_url) + getString(R.string.bin_collections_url) + Uri.encode(postCodeEditText.getText().toString());
			return 	bcr.retrieveBinCollections(url);
		}
		
		@Override
		protected void onPostExecute(final ArrayList<Property> result){
			//run intent to next activity
			setSupportProgressBarIndeterminateVisibility(false);
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
		}
	}


	
}
