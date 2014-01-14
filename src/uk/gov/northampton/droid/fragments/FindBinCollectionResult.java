package uk.gov.northampton.droid.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import uk.gov.northampton.droid.CalendarEvent;
import uk.gov.northampton.droid.MainActivity;
import uk.gov.northampton.droid.Property;
import uk.gov.northampton.droid.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class FindBinCollectionResult extends SherlockFragmentActivity {

	private static final String COLLECTION_ADDRESS = "COLLECTION_ADDRESS";
	private Property collectionAddress;
	private Button doneBtn;
	private Button reminderBtn;
	private TextView binTypeTextView;
	private TextView binDayTextView;
	private TextView binTimeTextView;
	private TextView recyclingDetailsTextView;
	
	// Reminder functionality
	private static final int REMINDER_TOKEN = 2002;
	private static final int GET_CALENDAR_TOKEN = 2003;
	private static final String[] EVENT_PROJECTION = new String[] {
		Calendars._ID, // 0
		Calendars.ACCOUNT_NAME, // 1
		Calendars.CALENDAR_DISPLAY_NAME, // 2
		Calendars.OWNER_ACCOUNT // 3
	};
	private static final int PROJECTION_ID_INDEX = 0;
	private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
	private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
	private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
	

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
		reminderBtn = (Button) findViewById(R.id.finditReminderButton);
	}

	private void setListeners(){
		doneBtn.setOnClickListener(doneButtonListener);
		reminderBtn.setOnClickListener(reminderButtonListener);
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
	
	private OnClickListener reminderButtonListener = new OnClickListener(){

		@SuppressLint("NewApi") 
		@Override
		public void onClick(View v) {
			Log.d("REMINDER BUTTON", "CLICKED");
			
			ContentResolver cr = getContentResolver();
			
				if(Build.VERSION.SDK_INT >= 14) {
				Uri uri = Calendars.CONTENT_URI;
				UserCalendar uc = new UserCalendar(cr);
				String selection = "(("+
						Calendars.ACCOUNT_NAME + "=?) AND ("+
						Calendars.ACCOUNT_TYPE + "=?) AND ("+
						Calendars.OWNER_ACCOUNT + "=?))";
				String[] selectionArgs = new String[] {"dantblundell@gmail.com","com.google","dantblundell@gmail.com"}; 
				Object cookie = new Object();
				uc.startQuery(GET_CALENDAR_TOKEN, cookie, uri, EVENT_PROJECTION, selection, selectionArgs, null);
				}
		}
		


	};
	
	private class UserCalendar extends AsyncQueryHandler {

		public UserCalendar(ContentResolver cr) {
			super(cr);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			// TODO Auto-generated method stub
			//super.onQueryComplete(token, cookie, cursor);
			
			long calId = 0;
			if(token == GET_CALENDAR_TOKEN) {
				// set cal id for the user
				Log.d("USERCALENDAR","QUERIED");
				Log.d("COOKIE",cookie.toString());
				while(cursor.moveToNext()) {
					calId = cursor.getLong(PROJECTION_ID_INDEX);
					Log.d("CURSOR NAME", cursor.getString(PROJECTION_ACCOUNT_NAME_INDEX));
				}
				Log.d("USERCALENDAR","LOOPED");
				cursor.close();
				
				Calendar startTime = Calendar.getInstance();
				Calendar endTime = Calendar.getInstance();
				// TODO set start and end dates based on next collection date
				
				long startMillis = startTime.getTimeInMillis();
				long endMillis = endTime.getTimeInMillis();
				
				CalendarEvent ce = new CalendarEvent();
				ce.setCalID(calId);
				ce.setTitle("Dans Test Event");
				ce.setDescription("Event description text");
				ce.setTimezone(TimeZone.getDefault().getDisplayName());
				ce.setStartMillis(startMillis);
				ce.setEndMillis(endMillis);
				
				RefuseReminderTask rrt = new RefuseReminderTask();
				rrt.execute(ce);
			}
		}

		@Override
		public void startQuery(int token, Object cookie, Uri uri,
				String[] projection, String selection, String[] selectionArgs,
				String orderBy) {
			
			// TODO Auto-generated method stub
			super.startQuery(token, cookie, uri, projection, selection, selectionArgs,
					orderBy);
			
			
		}
		
	}
	
	private class RefuseReminderTask extends AsyncTask<CalendarEvent,Void,String> {

		@SuppressLint("NewApi")
		@Override
		protected String doInBackground(CalendarEvent... params) {
			CalendarEvent ce = params[0];
			ContentValues cv = ce.getContent();
			ContentResolver cr = getContentResolver();
			Uri uri = cr.insert(Events.CONTENT_URI, cv);
			String eventId = uri.getLastPathSegment();
			return eventId;
		}
		
		protected void onPostExecute(final String result){
			if(result != null) {
				Toast.makeText(getApplicationContext(), "Created Event ID" + result, Toast.LENGTH_SHORT).show();
			}
		}
		
	}

}
