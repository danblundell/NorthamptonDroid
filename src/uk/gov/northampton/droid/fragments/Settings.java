package uk.gov.northampton.droid.fragments;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.lib.EditTextDialogFragment;
import uk.gov.northampton.droid.lib.EditTextDialogFragment.EditTextDialogListener;
import uk.gov.northampton.droid.lib.PostCodeDialogFragment;
import uk.gov.northampton.droid.lib.PostCodeDialogFragment.PostCodeDialogListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class Settings extends SherlockFragmentActivity implements PostCodeDialogListener, EditTextDialogListener {

	private LinearLayout nameLayout;
	private LinearLayout emailLayout;
	private LinearLayout phoneLayout;
	private LinearLayout postCodeLayout;
	private TextView nameTitle;
	private TextView emailTitle;
	private TextView phoneTitle;
	private TextView postCodeTitle;
	private TextView name;
	private TextView email;
	private TextView phone;
	private TextView postCode;

	private static final String NBC_DEVICE_ID = "NBC_DEVICE_ID";
	public static final String USER_PREFERENCE = "USER_PREFERENCE";
	public static final String NBC_NAME = "NBC_NAME";
	public static final String NBC_EMAIL = "NBC_EMAIL";
	public static final String NBC_TEL = "NBC_TEL";
	public static final String NBC_POST_CODE = "NBC_POST_CODE";
	public static final String NBC_REFUSE_REMINDER = "NBC_REFUSE_REMINDER";
	public static final String NBC_REFUSE_REMINDER_BLACK = "NBC_REFUSE_REMINDER_BLACK";
	public static final String NBC_REFUSE_REMINDER_BROWN = "NBC_REFUSE_REMINDER_BROWN";

	SharedPreferences sharedPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		ActionBar ab = getSupportActionBar();
		ab.setTitle(R.string.settings_title);

		findAllViewsById();
		populateViews();



		Context context = getApplicationContext();
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		UpdateUIFromPreferences();

	}

	private void findAllViewsById(){
		nameLayout = (LinearLayout) this.findViewById(R.id.settings_name_LinearLayout);
		emailLayout = (LinearLayout) this.findViewById(R.id.settings_email_LinearLayout);
		phoneLayout = (LinearLayout) this.findViewById(R.id.settings_telephone_LinearLayout);
		postCodeLayout = (LinearLayout) this.findViewById(R.id.settings_postCode_LinearLayout);
		nameTitle = (TextView) this.findViewById(R.id.settings_name_TextView);
		emailTitle = (TextView) this.findViewById(R.id.settings_email_TextView);
		phoneTitle = (TextView) this.findViewById(R.id.settings_telephone_TextView);
		postCodeTitle = (TextView) this.findViewById(R.id.settings_postCode_TextView);
		name = (TextView) this.findViewById(R.id.settings_name_set_TextView);
		email = (TextView) this.findViewById(R.id.settings_email_set_TextView);
		phone = (TextView) this.findViewById(R.id.settings_telephone_set_TextView);
		postCode = (TextView) this.findViewById(R.id.settings_postCode_set_TextView);
	}

	private void populateViews() {
		nameTitle.setTag(R.id.nbc_setting_title);
		
		name.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		name.setTag(R.id.nbc_setting_value);
		nameLayout.setOnClickListener(editTextDialogListener);
		
		emailTitle.setTag(R.id.nbc_setting_title);
		email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		email.setTag(R.id.nbc_setting_value);
		emailLayout.setOnClickListener(editTextDialogListener);

		phoneTitle.setTag(R.id.nbc_setting_title);
		phone.setInputType(InputType.TYPE_CLASS_PHONE);
		phone.setTag(R.id.nbc_setting_value);
		phoneLayout.setOnClickListener(editTextDialogListener);

		postCodeLayout.setOnClickListener(editPostCodeListener);
	}

	private void UpdateUIFromPreferences() {

		String namePref = sharedPrefs.getString(NBC_NAME, getString(R.string.settings_name_add));
		String emailPref = sharedPrefs.getString(NBC_EMAIL, getString(R.string.settings_email_add));
		String phonePref = sharedPrefs.getString(NBC_TEL, getString(R.string.settings_telephone_add));
		String postCodePref = sharedPrefs.getString(NBC_POST_CODE, getString(R.string.settings_postcode_add));
		String deviceId = sharedPrefs.getString(NBC_DEVICE_ID, "no device id");

		Log.d("NAME PREF",namePref);
		Log.d("EMAIL PREF",emailPref);
		Log.d("PHONE PREF",phonePref);
		Log.d("PC PREF",postCodePref);
		Log.d("DEVICE ID",deviceId);

		name.setText(namePref);
		email.setText(emailPref);
		phone.setText(phonePref);
		postCode.setText(postCodePref);
	}

	public OnClickListener editTextDialogListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			TextView title = (TextView) v.findViewWithTag(R.id.nbc_setting_title);
			TextView value = (TextView) v.findViewWithTag(R.id.nbc_setting_value);
			String titleText = title.getText().toString(); 
			//EditTextDialogFragment(Title for the dialog, prompt,view id to return to, edit text input type);
			DialogFragment editTextFragment = new EditTextDialogFragment(titleText,value.getId(),value.getInputType());
			editTextFragment.show(getSupportFragmentManager(), "editText");
		}

	};

	public OnClickListener editPostCodeListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			showPostCodeOptions();
		}

	};

	public void showPostCodeOptions() {
		DialogFragment newFragment = new PostCodeDialogFragment();
		newFragment.show(getSupportFragmentManager(), "postCode");
	}

	public void saveChanges(){
		savePreferences();
		Settings.this.setResult(RESULT_OK);
		finish();
	}

	public void cancelChanges(){
		Settings.this.setResult(RESULT_CANCELED);
		finish();
	}

	private void savePreferences() {
		String namePref = name.getText().toString();
		String emailPref = email.getText().toString();
		String phonePref = phone.getText().toString();
		String postCodePref = postCode.getText().toString();

		Editor editor = sharedPrefs.edit();
		editor.putString(NBC_NAME, namePref);
		editor.putString(NBC_EMAIL, emailPref);
		editor.putString(NBC_TEL, phonePref);
		editor.putString(NBC_POST_CODE, postCodePref);
		editor.commit();
	}
	
	public static void saveStringPreference(Context context, String key, String val) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putString(key, val);
		editor.commit();
	}
	
	public static void createDeviceId(Context context, String accountId) {
		Settings.saveStringPreference(context, NBC_DEVICE_ID, accountId);
		
//		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy",Locale.UK);
//		Calendar cal = Calendar.getInstance();
//		Date time = cal.getTime();
//		String timeStr = sdf.format(time);
//		String input = accountId + timeStr;
//		MessageDigest md;
//		try {
//			md = MessageDigest.getInstance("MD5");
//			byte[] hashed = md.digest(input.getBytes("UTF-8"));
//			Log.d("DEVICE ID", "Saved Device ID: "+new String(hashed));
//			
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

	public static String getDeviceIdKey() {
		return NBC_DEVICE_ID;
	}

	@Override
	public void onFinishPostCodeDialog(String postCode) {
		if(postCode != null){
			this.postCode.setText(postCode);
			savePreferences();
		}		
	}

	@Override
	public void onFinishEditTextDialog(int opt, String string,int viewId) {
		if(opt == this.RESULT_OK){
			TextView vTextView = (TextView) this.findViewById(viewId);
			vTextView.setText(string);
			savePreferences();
		}

	}

}
