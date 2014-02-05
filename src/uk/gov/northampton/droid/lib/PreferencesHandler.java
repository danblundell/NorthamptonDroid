package uk.gov.northampton.droid.lib;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferencesHandler {

	Context context;
	SharedPreferences sharedPrefs;
	Map<String, Object> prefs = new HashMap<String, Object>();
	Editor editor;
	int total;

	public PreferencesHandler() {
	}

	public PreferencesHandler(Context context, int total) {
		this.context = context;
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = sharedPrefs.edit();
	}

	public void setContext(Context context) {
		this.context = context;
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public void setTotal(int total) {
		this.total = total;
	}

	private boolean isFull() {
		return (prefs.size() >= total) ? true : false;
	}

	public void addPreference(String key, int value) {
		prefs.put(key, value);
		if(isFull()) {
			savePreferences();
		}
	}
	public void addPreference(String key, String value) {
		prefs.put(key, value);
		if(isFull()) {
			savePreferences();
		}
	}
	public void addPreference(String key, Long value) {
		prefs.put(key, value);

		if(isFull()) {

			savePreferences();
		}
	}
	public void addPreference(String key, boolean value) {
		prefs.put(key, value);
		if(isFull()) {
			savePreferences();
		}
	}

	public void savePreferences() {
		Iterator<Entry<String, Object>> i = prefs.entrySet().iterator();

		editor = sharedPrefs.edit();

		while (i.hasNext()) {
			Entry<String, Object> me = i.next();

			if(me.getValue() instanceof String) {
				editor.putString((String) me.getKey(), String.valueOf(me.getValue()));
			}
			else if(me.getValue() instanceof Long) {
				editor.putLong((String) me.getKey(),(Long) me.getValue());
			}
			else if(me.getValue() instanceof Integer) {
				editor.putInt((String) me.getKey(), (Integer) me.getValue());
			}
			else if(me.getValue() instanceof Boolean) {
				editor.putBoolean((String) me.getKey(), (Boolean) me.getValue());
			}
			i.remove();
		}

		editor.commit();		

	}

}
