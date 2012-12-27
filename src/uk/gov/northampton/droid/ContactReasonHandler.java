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

public class ContactReasonHandler extends DefaultHandler{

		private StringBuffer buffer = new StringBuffer();

		private ArrayList<ContactReason> serviceAreaReasonList;
		private ArrayList<ContactReason> sectionReasonList;
		private ArrayList<ContactReason> reasonReasonList;
		private ContactReason sar;
		private ContactReason sr;
		private ContactReason rr;

		@Override
		public void startElement(String namespaceURI, String localName,
				String qName, Attributes atts) throws SAXException {

			buffer.setLength(0);

			if (localName.equals("service-areas")) {
				serviceAreaReasonList = new ArrayList<ContactReason>();
			}
			else if (localName.equals("sections")) {
				sectionReasonList = new ArrayList<ContactReason>();
			}
			else if (localName.equals("reasons")) {
				reasonReasonList = new ArrayList<ContactReason>();
			}
			else if (localName.equals("service-area")) {
				sar = new ContactReason();
			}
			else if (localName.equals("section")) {
				sr = new ContactReason();
			}
			else if (localName.equals("reason")) {
				rr = new ContactReason();
			}

		}

		@Override
		public void endElement(String uri, String localName, String qName)throws SAXException {

			if (localName.equals("service-area")) {
				serviceAreaReasonList.add(sar);
			}
			else if (localName.equals("service-area-ext")) {
				sar.seteDesc(buffer.toString());
			}
			else if (localName.equals("section-ext")) {
				sr.seteDesc(buffer.toString());
			}
			else if (localName.equals("reason-ext")) {
				rr.seteDesc(buffer.toString());
			}
			else if (localName.equals("service-area-int")) {
				sar.setiDesc(buffer.toString());
			}
			else if (localName.equals("section-int")) {
				sr.setiDesc(buffer.toString());
			}
			else if (localName.equals("reason-int")) {
				rr.setiDesc(buffer.toString());
			}
			else if (localName.equals("reason")){
				reasonReasonList.add(rr);
			}
			else if (localName.equals("reasons")){
				sr.setReasons(reasonReasonList);
			}
			else if (localName.equals("section")){
				sectionReasonList.add(sr);
			}
			else if (localName.equals("sections")){
				sar.setReasons(sectionReasonList);
			}
			
		}

		@Override
		public void characters(char[] ch, int start, int length) {
			buffer.append(ch, start, length);
		}

		public ArrayList<ContactReason> retrieveContactReasons() {
			//Log.d("PARSED",serviceAreaReasonList.get(5).getReasons().get(1).geteDesc());
			return serviceAreaReasonList;
		}

}
