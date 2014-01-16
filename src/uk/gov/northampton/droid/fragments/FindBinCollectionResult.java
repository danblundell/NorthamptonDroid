package uk.gov.northampton.droid.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import uk.gov.northampton.droid.CalendarEvent;
import uk.gov.northampton.droid.CalendarReminder;
import uk.gov.northampton.droid.MainActivity;
import uk.gov.northampton.droid.Property;
import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.lib.TimePickerDialogFragment;
import android.annotation.TargetApi;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class FindBinCollectionResult extends SherlockFragmentActivity implements TimePickerDialogFragment.TimePickerTimeSetDialogListener {

	private static final String COLLECTION_ADDRESS = "COLLECTION_ADDRESS";
	private Property collectionAddress;
	private Button doneBtn;
	private Button reminderBtn;
	private TextView binTypeTextView;
	private TextView binDayTextView;
	private TextView binTimeTextView;
	private TextView recyclingDetailsTextView;
	
	// Reminder functionality
	private String accountId;
	private long calId = 0;
	private long eventIdBlack = 0;
	private long eventIdBrown = 0;
	private long eventIdBags = 0;
	private static final int REMINDER_TOKEN = 2002;
	private static final int GET_CALENDAR_TOKEN = 2003;
	private static final String[] EVENT_PROJECTION = new String[] {Calendars._ID, Calendars.ACCOUNT_NAME, Calendars.CALENDAR_DISPLAY_NAME, Calendars.OWNER_ACCOUNT };
	private static final int PROJECTION_ID_INDEX = 0;
	private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
	private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
	private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
	
	private static final String DEBUG_KEY  = "REFUSE RESULT";
	
	private Context context;
	private SharedPreferences sharedPrefs;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.find_it_4_bin_collection_confirmation);
		findAllViewsById();
		
		context = getApplicationContext();
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		setListeners();
		collectionAddress = (Property) getIntent().getSerializableExtra(COLLECTION_ADDRESS);
		updateUIFromIntent();
		
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			updateFromPreferences();
			checkForCalendar();
			
		}
		
	}

	private void findAllViewsById(){
		binDayTextView = (TextView) findViewById(R.id.findit_dynamic_top_TextView);
		binTypeTextView = (TextView) findViewById(R.id.findit_dynamic_bottom_TextView);
		binTimeTextView = (TextView) findViewById(R.id.findit_dynamic_collection_time_TextView);
		recyclingDetailsTextView = (TextView) findViewById(R.id.findit_collection_reminder_TextView);
		doneBtn = (Button) findViewById(R.id.finditDoneButton);
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			reminderBtn = (Button) findViewById(R.id.finditReminderButton);
		}
	}

	private void setListeners(){
		doneBtn.setOnClickListener(doneButtonListener);
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			reminderBtn.setOnClickListener(reminderButtonListener);
		}
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
	
	private void updateFromPreferences(){
		// get email and phone number from preferences
		// get any existing event id's
		eventIdBlack = sharedPrefs.getLong(Settings.NBC_REFUSE_REMINDER_BLACK, 0);
		eventIdBrown = sharedPrefs.getLong(Settings.NBC_REFUSE_REMINDER_BROWN, 0);
		eventIdBags = sharedPrefs.getLong(Settings.NBC_REFUSE_REMINDER, 0);
		calId = sharedPrefs.getLong(Settings.NBC_CAL_ID, 0);		
		
		Log.d(DEBUG_KEY, "Black: "+eventIdBlack);
		Log.d(DEBUG_KEY, "Brown: "+eventIdBrown);
		Log.d(DEBUG_KEY, "Bags: "+eventIdBags);
	}
	
	private void checkForCalendar() {
		Log.d(DEBUG_KEY, "Calendar ID: "+calId);
		if(calId == 0) {
			Log.d(DEBUG_KEY, "Getting Account ID");
			accountId = sharedPrefs.getString(Settings.NBC_ACCOUNT_ID, null);
			Log.d(DEBUG_KEY, "Account ID: "+accountId);
			// search for a calendar for that account Id
			if(accountId != null) {
				Log.d(DEBUG_KEY, "Getting Calendar ID from account id");
				getUserCalendarId(accountId, accountId);
			}
			else {
				Log.d(DEBUG_KEY, "NO CAL ID or ACCOUNT ID - Hiding Reminder Button");
				hideUIElement(reminderBtn.getId());
			}
		}
	}
	
	private void hideUIElement(int id) {
		View v = findViewById(id);
		v.setVisibility(View.GONE);
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
		@Override
		public void onClick(View v) {
			// launch dialog fragment to set time for the reminder
			showTimePickerDialog();
			
		}
	};
	
	private boolean hasAlternateCollections(String s){
		return (s.compareTo(getString(R.string.bin_collection_black)) == 0 || s.compareTo(getString(R.string.bin_collection_brown)) == 0) ? true : false;
	}
	
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void getUserCalendarId(String accountId, String owner) {
		ContentResolver cr = getContentResolver();
		
		
		if(Build.VERSION.SDK_INT >= 14) {
			Uri uri = Calendars.CONTENT_URI;
			UserCalendar uc = new UserCalendar(cr);
			String selection = "(("+
					Calendars.ACCOUNT_NAME + "=?) AND ("+
					Calendars.ACCOUNT_TYPE + "=?) AND ("+
					Calendars.OWNER_ACCOUNT + "=?))";
			
			// TODO need to get these arguments based on the device account
			String[] selectionArgs = new String[] {accountId,getString(R.string.account_filter),owner}; 
			
			Object cookie = new Object();
			uc.startQuery(GET_CALENDAR_TOKEN, cookie, uri, EVENT_PROJECTION, selection, selectionArgs, null);
		}
	}
	
	private class UserCalendar extends AsyncQueryHandler {

		public UserCalendar(ContentResolver cr) {
			super(cr);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			// TODO Auto-generated method stub
			//super.onQueryComplete(token, cookie, cursor);
			
			if(token == GET_CALENDAR_TOKEN) {
				// set cal id for the user
				Log.d("USERCALENDAR","QUERIED");
				Log.d("COOKIE",cookie.toString());
				String calName = "";
				if(cursor.moveToFirst()) {
					calId = cursor.getLong(PROJECTION_ID_INDEX);
					calName = cursor.getString(PROJECTION_DISPLAY_NAME_INDEX);
					Log.d("CURSOR NAME", cursor.getString(PROJECTION_ACCOUNT_NAME_INDEX));
					Settings.saveLongPreference(getApplicationContext(), Settings.NBC_CAL_ID, calId);
				}
				Log.d("CALENDAR","Cal name: " +calName);
				Log.d("USERCALENDAR","LOOPED");
				cursor.close();
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
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private class RefuseReminderTask extends AsyncTask<CalendarEvent,Void,String> {

		@Override
		protected String doInBackground(CalendarEvent... params) {
			String eventId = "";
			CalendarEvent ce = params[0];
			
			Calendar start = Calendar.getInstance();
			start.setTimeInMillis(ce.getStartMillis());
			Log.d("TIME", start.getTime().toString());
			
			ContentValues cv = ce.getContent(); // gets the config options for the event from the Calendar Event object
			ContentResolver cr = getContentResolver();
			
			if(ce.isUpdate()) {
				// remove any reminders on the event
				cv.put(Events.HAS_ALARM, false);
				
				Log.d(DEBUG_KEY, "UPDATING EVENT: " + String.valueOf(ce.getEventId()));
				// update event
				Uri updateUri = ContentUris.withAppendedId(Events.CONTENT_URI, ce.getEventId());
				int rows = getContentResolver().update(updateUri, cv, null, null);
				Log.i("EVENT UPDATE", "Rows updated: " + rows); 
				eventId = String.valueOf(ce.getEventId());
			}
			else {
				Log.d(DEBUG_KEY, "NEW EVENT");
				// new event
				Uri uri = cr.insert(Events.CONTENT_URI, cv); // inserts the event
				Log.d("EVENT",cv.toString());
				eventId = uri.getLastPathSegment(); // gets the event id
			}
			
			long eventIdLong = Long.parseLong(eventId);
			
			
			if(ce.getTag() == getString(R.string.bin_collection_black)) {
				saveEventId(Settings.NBC_REFUSE_REMINDER_BLACK, eventIdLong);
			}
			if(ce.getTag() == getString(R.string.bin_collection_brown)) {
				saveEventId(Settings.NBC_REFUSE_REMINDER_BROWN, eventIdLong);
			}
			if(ce.getTag() == getString(R.string.bin_collection_bags)) {
				saveEventId(Settings.NBC_REFUSE_REMINDER, eventIdLong);
			}
			
			setReminder(eventIdLong, ce.getReminderTime(), Reminders.METHOD_DEFAULT);
			
			return eventId;
		}
		
		protected void onPostExecute(final String result){
			if(result != null) {
				Toast.makeText(getApplicationContext(), "Created Event ID" + result, Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void createRefuseEvent(Calendar eventTime, String tag, String title, String rrule, int reminderMinutes, long existingEventId) {
		
		// set start and end times for the event
		Calendar startTime = eventTime;
		Calendar endTime = Calendar.getInstance();
		endTime.setTimeInMillis(startTime.getTimeInMillis());
		endTime.set(Calendar.HOUR,endTime.get(Calendar.HOUR)+1);
		
		long startMillis = startTime.getTimeInMillis();
		long endMillis = endTime.getTimeInMillis();
		
		CalendarEvent ce = new CalendarEvent();
		ce.setCalID(calId);
		ce.setTitle(title);
		ce.setTag(tag);
		ce.setDescription("Event description text");
		ce.setTimezone(TimeZone.getDefault().getDisplayName());
		ce.setStartMillis(startMillis);
		ce.setEndMillis(endMillis);
		ce.setRrule(rrule);
		ce.setReminderTime(reminderMinutes);
		if(existingEventId > 0) {
			ce.setEventId(existingEventId);
			Log.d(DEBUG_KEY, "Existing event id added to calendarEvent object");
		}
		
		RefuseReminderTask rrt = new RefuseReminderTask();
		rrt.execute(ce);
	}
	
	public void saveEventId(String key, long eventId) {
		Context context = getApplicationContext();
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = sharedPrefs.edit();
		editor.putLong(key, eventId);
		editor.commit();
		Log.d(DEBUG_KEY, "SAVING EVENT: " + key + " as " + eventId);
	}
	
	public void showTimePickerDialog() {
	    DialogFragment newFragment = new TimePickerDialogFragment();
	    newFragment.show(getSupportFragmentManager(), "timePicker");
	}

	@Override
	public void onSetTimeDialog(int hourOfDay, int minute) {
		Log.d(DEBUG_KEY, "TIME SET");
		// updates the eventIds each time a new reminder is set
		updateFromPreferences();
		
		// kick off the set the reminder event process
		setReminderEvents(hourOfDay, minute);
	}
	
	public void setReminderEvents(int hourOfDay, int minute) {
		
		Calendar wk1 = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmm", Locale.UK);
		
		try {
			Date collectionDate = sdf.parse(collectionAddress.getBinCollectionDate());
			wk1.setTime(collectionDate);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		// get the reminder period in minutes
		int reminderMinutes = getReminderMinutes(wk1, hourOfDay, minute);
		Log.d("REMINDER","REMINDER SET FOR "+reminderMinutes+" before 6:30");
		
		// check if the collection is alternate or weekly
		boolean alternate = hasAlternateCollections(collectionAddress.getBinCollectionType());
		
		if(alternate) {
			// set a second event offset by a week
			Calendar wk2 = Calendar.getInstance();
			wk2.setTimeInMillis(wk1.getTimeInMillis());
			wk2.add(Calendar.DAY_OF_MONTH, 7);
			String rrule = getString(R.string.event_rrule_fortnightly);
			
			// TODO go and check if there has already been a reminder set for this collection type
			
			// create the events
			if(collectionAddress.getBinCollectionType().compareTo(getString(R.string.bin_collection_black)) == 0) {
				// black bin collection
				createRefuseEvent(wk1, collectionAddress.getBinCollectionType(), getString(R.string.event_title_blackbin), rrule, reminderMinutes, eventIdBlack);
				createRefuseEvent(wk2, getString(R.string.bin_collection_brown), getString(R.string.event_title_brownbin), rrule, reminderMinutes, eventIdBrown);
			}
			else {
				// brown bin collection
				createRefuseEvent(wk1, collectionAddress.getBinCollectionType(), getString(R.string.event_title_brownbin), rrule, reminderMinutes, eventIdBrown);
				createRefuseEvent(wk2, getString(R.string.bin_collection_black), getString(R.string.event_title_blackbin), rrule, reminderMinutes, eventIdBlack);
			}
		} else {
			// set bags events
			String rrule = getString(R.string.event_rrule_weekly);
			createRefuseEvent(wk1, getString(R.string.bin_collection_bags), getString(R.string.event_title_bags), rrule, reminderMinutes, eventIdBags);
		}

	}
	
	/*
	 * Works out how many minutes are between the new collection date and the requested reminder time
	 * e.g 6:30am and a 6:15am reminder time, the function will return 15
	 * if the reminder time is set for after midday, the function will calculate the reminder for day before
	 */
	public int getReminderMinutes(Calendar collectionDate,int reminderHourOfDay, int reminderMin) {

		// set the reminder date to the same as the collection date
		Calendar reminderDate = Calendar.getInstance();
		reminderDate.setTimeInMillis(collectionDate.getTimeInMillis());
		
		if(reminderHourOfDay > 12) {
			reminderDate.add(Calendar.DAY_OF_MONTH, -1);
		}
		
		reminderDate.set(Calendar.HOUR_OF_DAY, reminderHourOfDay);
		reminderDate.set(Calendar.MINUTE, reminderMin);
		
		return (int) ((collectionDate.getTimeInMillis() - reminderDate.getTimeInMillis()) / (1000*60));

	}
	
	/*
	 * Adds a reminder to a given event id
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public void setReminder(long eventId, int minutes, int method) {
		CalendarReminder reminder = new CalendarReminder(eventId, minutes, method);
		ContentResolver cr = getContentResolver();
		cr.insert(Reminders.CONTENT_URI, reminder.getContent());
	}	

}
