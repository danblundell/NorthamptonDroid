package uk.gov.northampton.droid;

import java.io.Serializable;

import android.content.ContentValues;
import android.provider.CalendarContract.Reminders;

public class CalendarReminder implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long eventId;
	private int minutes;
	private int method = Reminders.METHOD_ALERT;
	private ContentValues content = new ContentValues();
	
	public CalendarReminder() {
		// TODO Auto-generated constructor stub
	}


	public CalendarReminder(long eventId, int minutes, int method) {
		super();
		this.eventId = eventId;
		this.minutes = minutes;
		this.method = method;
	}


	public long getEventId() {
		return eventId;
	}


	public void setEventId(long eventId) {
		this.eventId = eventId;
	}


	public int getMinutes() {
		return minutes;
	}


	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}


	public int getMethod() {
		return method;
	}


	public void setMethod(int method) {
		this.method = method;
	}


	public ContentValues getContent() {
		content.put(Reminders.EVENT_ID, eventId);
		content.put(Reminders.MINUTES, minutes);
		content.put(Reminders.METHOD, method);
		
		return content;
	}
	
	

}
