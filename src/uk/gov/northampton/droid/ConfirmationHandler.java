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

public class ConfirmationHandler extends DefaultHandler{

		private StringBuffer buffer = new StringBuffer();
		private Confirmation conf;

		@Override
		public void startElement(String namespaceURI, String localName,
				String qName, Attributes atts) throws SAXException {

			buffer.setLength(0);

			if (localName.equals("response")) {
				conf = new Confirmation();
				conf.setSuccess(false);
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)throws SAXException {

			if (localName.equals("result")) {
				boolean success = false;
				if(buffer.toString().equalsIgnoreCase("success")){
					success = true;
				}
				conf.setSuccess(success);
			}
			else if (localName.equals("callNumber")) {
				conf.setCallNumber(buffer.toString());
			}
			else if (localName.equals("slaDate")) {
				conf.setSla(buffer.toString());
			}
		}

		@Override
		public void characters(char[] ch, int start, int length) {
			buffer.append(ch, start, length);
		}

		public Confirmation retrieveConfirmation() {
			if(conf.isSuccess()){
				Log.d("CONF PARSED","Confirmation Call Num: " + conf.getCallNumber());
			}else{
				Log.d("CONF FAILED","Confirmation Call Num: " + conf.isSuccess());
			}
			return conf;
		}

}
