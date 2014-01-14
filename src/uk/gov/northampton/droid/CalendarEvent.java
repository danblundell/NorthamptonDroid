package uk.gov.northampton.droid;

import java.io.Serializable;

import android.content.ContentValues;
import android.provider.CalendarContract.Events;

public class CalendarEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long calID;
	private long startMillis;
	private long endMillis;
	private String title;
	private String description;
	private String timezone;
	private ContentValues content = new ContentValues();
	
	public CalendarEvent(long calID, long startMillis, long endMillis,
			String title, String description, String timezone) {
		super();
		this.calID = calID;
		this.startMillis = startMillis;
		this.endMillis = endMillis;
		this.title = title;
		this.description = description;
		this.timezone = timezone;
	}
	
	public CalendarEvent() {
		// TODO Auto-generated constructor stub
	}

	public long getCalID() {
		return calID;
	}
	public void setCalID(long calID) {
		this.calID = calID;
	}
	public long getStartMillis() {
		return startMillis;
	}
	public void setStartMillis(long startMillis) {
		this.startMillis = startMillis;
	}
	public long getEndMillis() {
		return endMillis;
	}
	public void setEndMillis(long endMillis) {
		this.endMillis = endMillis;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public ContentValues getContent() {
		content.put(Events.TITLE, title);
		content.put(Events.DESCRIPTION, description);
		content.put(Events.CALENDAR_ID, calID);
		content.put(Events.EVENT_TIMEZONE, timezone);
		content.put(Events.DTSTART, startMillis);
		content.put(Events.DTEND, endMillis);
		
		return content;
	}
	
	

}
