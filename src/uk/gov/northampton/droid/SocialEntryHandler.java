package uk.gov.northampton.droid;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class SocialEntryHandler extends DefaultHandler{

		private StringBuffer buffer = new StringBuffer();

		private ArrayList<SocialEntry> socialEntryList;
		private SocialEntry se;

		@Override
		public void startElement(String namespaceURI, String localName,
				String qName, Attributes atts) throws SAXException {

			buffer.setLength(0);

			if (localName.equals("socialfeed")) {
				socialEntryList = new ArrayList<SocialEntry>();
			}
			else if (localName.equals("socialentry")) {
				se = new SocialEntry();
			}

		}

		@Override
		public void endElement(String uri, String localName, String qName)throws SAXException {

			if (localName.equals("socialentry")) {
				socialEntryList.add(se);
			}
			else if (localName.equals("date")) {
				se.date = getTimeDifference(buffer.toString());
			}
			else if (localName.equals("type")) {
				se.type = getSource(buffer.toString());
			}
			else if (localName.equals("typedesc")) {
				se.typeDesc = buffer.toString();
			}
			else if (localName.equals("heading")) {
				String decoded = "";
				try {
					decoded = new String(buffer.toString().getBytes(),"UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				se.text = decoded;
			}
			else if (localName.equals("url")) {
				String url = buffer.toString();
				se.setUrl(url);
			}	
		}

		@Override
		public void characters(char[] ch, int start, int length) {
			buffer.append(ch, start, length);
		}

		public ArrayList<SocialEntry> retrieveSocialFeed() {
			return socialEntryList;
		}
		
		private String getSource(String source){
			
			if(source.equalsIgnoreCase("tweet")){
				source = "Twitter";
			}
			
			return source;
		}
		
		private String getTimeDifference(String date){
			
			String diff = date;
			SimpleDateFormat dateFormatIn = new SimpleDateFormat("yyyyMMddHHmm");
			Calendar calNow = Calendar.getInstance();
			Calendar calIn = Calendar.getInstance();
			Date dateIn;
			
			try {
				dateIn = dateFormatIn.parse(date);
				calIn.setTime(dateIn);
				long dateDiff = calNow.getTimeInMillis() - calIn.getTimeInMillis();
				//difference to days
				Long daysDiff = dateDiff / (24 * 60 * 60 * 1000);
				//difference to hours
				Long hoursDiff = dateDiff / (60 * 60 * 1000);
				
				if(daysDiff > 0){
					switch (daysDiff.intValue()){
					case 1: diff = "1 day ago";
							break;
					default: diff = daysDiff.intValue() + " days ago";
							break;
					}
				}
				else{
					switch(hoursDiff.intValue()){
					case 0: diff = "just now";
							break;
					case 1: diff = "1 hour ago";
							break;
					default: diff = hoursDiff.intValue() + " hours ago";
							break;
					}
				}
				
			} catch (ParseException e) {
				Log.d("date parsed", "failed");
			}
			
			return diff;	
			
		}

}
