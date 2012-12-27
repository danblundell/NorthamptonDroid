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

public class ReportProblemHandler extends DefaultHandler{

		private StringBuffer buffer = new StringBuffer();

		private ArrayList<ReportProblem> problemList;
		private ReportProblem problem;

		@Override
		public void startElement(String namespaceURI, String localName,
				String qName, Attributes atts) throws SAXException {

			buffer.setLength(0);

			if (localName.equals("problems")) {
				problemList = new ArrayList<ReportProblem>();
			}
			else if (localName.equals("problem")) {
				problem = new ReportProblem();
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)throws SAXException {

			if (localName.equals("problem")) {
				problemList.add(problem);
			}
			else if (localName.equals("problem-description")) {
				problem.setpDesc(buffer.toString());
			}
			else if (localName.equals("problem-number")) {
				int pNum = Integer.parseInt(buffer.toString());
				problem.setpNum(pNum);
			}
		}

		@Override
		public void characters(char[] ch, int start, int length) {
			buffer.append(ch, start, length);
		}

		public ArrayList<ReportProblem> retrieveProblemReasons() {
			Log.d("PL PARSED","Problem List: " + problemList.size());
			return problemList;
		}

}
