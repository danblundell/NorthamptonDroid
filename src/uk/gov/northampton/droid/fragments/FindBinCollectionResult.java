package uk.gov.northampton.droid.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.gov.northampton.droid.MainActivity;
import uk.gov.northampton.droid.Property;
import uk.gov.northampton.droid.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class FindBinCollectionResult extends SherlockFragmentActivity {

	private static final String COLLECTION_ADDRESS = "COLLECTION_ADDRESS";
	private Property collectionAddress;
	private Button doneBtn;
	private TextView binTypeTextView;
	private TextView binDayTextView;
	private TextView binTimeTextView;
	private TextView recyclingDetailsTextView;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.find_it_4_bin_collection_confirmation);
		findAllViewsById();
		setListeners();
		collectionAddress = (Property) getIntent().getSerializableExtra(COLLECTION_ADDRESS);
		updateUIFromIntent();
	}

	private void findAllViewsById(){
		binDayTextView = (TextView) findViewById(R.id.findit_dynamic_top_TextView);
		binTypeTextView = (TextView) findViewById(R.id.findit_dynamic_bottom_TextView);
		binTimeTextView = (TextView) findViewById(R.id.findit_dynamic_collection_time_TextView);
		recyclingDetailsTextView = (TextView) findViewById(R.id.findit_collection_reminder_TextView);
		doneBtn = (Button) findViewById(R.id.finditDoneButton);
	}

	private void setListeners(){
		doneBtn.setOnClickListener(doneButtonListener);
	}

	private void updateUIFromIntent(){

		String binCollectionType = getBinCollectionType(collectionAddress.getBinCollectionType());
		String binCollectionTime = getBinCollectionTime(collectionAddress.getBinCollectionDate());
		binDayTextView.setText(collectionAddress.getBinCollectionDay());
		if(binCollectionType != null){
			binTypeTextView.setText(binCollectionType);
		}
		if(binCollectionTime != null){
			binTimeTextView.setText(binCollectionTime);
		}
		setRecyclingText(collectionAddress.getBinCollectionType());
	}

	private String getBinCollectionType(String s){
		if(s.compareTo(getString(R.string.bin_collection_black)) == 0){
			return getString(R.string.bin_collection_black_out);
		}
		else if(s.compareTo(getString(R.string.bin_collection_brown)) == 0){
			return getString(R.string.bin_collection_brown_out);
		}
		else if(s.compareTo(getString(R.string.bin_collection_bags)) == 0){
			return getString(R.string.bin_collection_bags_out);
		}
		return null;
	}
	
	private void setRecyclingText(String s){
		if(s.compareTo(getString(R.string.bin_collection_black)) == 0 || s.compareTo(getString(R.string.bin_collection_brown)) == 0){
			recyclingDetailsTextView.setText(getString(R.string.findit_bin_collection_wheelie_text) + getString(R.string.findit_bin_collection_recycling_text));
		}
	}

	private String getBinCollectionTime(String dateString){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmm");
		Date nextCollection;
		try {
			nextCollection = sdf.parse(dateString);
			Calendar cal = Calendar.getInstance();
			cal.setTime(nextCollection);
			
			StringBuffer sb = new StringBuffer();
			sb.append(cal.get(Calendar.HOUR));
			sb.append(":");
			sb.append(cal.get(Calendar.MINUTE));
			if(cal.get(Calendar.AM_PM)==0){
				sb.append("am");
			}
			else{
				sb.append("pm");
			}
			return sb.toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	private OnClickListener doneButtonListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
		}

	};

}
