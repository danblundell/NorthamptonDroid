package uk.gov.northampton.droid.lib;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.util.Log;

import uk.gov.northampton.droid.Confirmation;
import uk.gov.northampton.droid.ConfirmationHandler;
import uk.gov.northampton.droid.ContactReason;
import uk.gov.northampton.droid.ContactReasonHandler;
import uk.gov.northampton.droid.Property;
import uk.gov.northampton.droid.PropertyHandler;
import uk.gov.northampton.droid.ReportProblem;
import uk.gov.northampton.droid.ReportProblemHandler;
import uk.gov.northampton.droid.SocialEntry;
import uk.gov.northampton.droid.SocialEntryHandler;

public class XmlParser {

	private XMLReader initializeReader() throws ParserConfigurationException, SAXException{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		//create a parser
		SAXParser parser = factory.newSAXParser();
		//create the reader / scanner
		XMLReader xmlReader = parser.getXMLReader();
		return xmlReader;
	}

	public ArrayList<SocialEntry> parseSocialFeedResponse(String xml){
		try{
			
			XMLReader xmlReader = initializeReader();
			SocialEntryHandler socialHandler = new SocialEntryHandler();

			//assign the handler
			xmlReader.setContentHandler(socialHandler);
			//perform the sync parse
			xmlReader.parse(new InputSource(new StringReader(xml)));
			Log.d("XML PARSE","PARSING");
			return socialHandler.retrieveSocialFeed();
		}catch(Exception e){
			Log.d("XML PARSE","PARSING FAILED");
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Property> parseBinCollectionsResponse(String xml){
		try{
			
			XMLReader xmlReader = initializeReader();
			PropertyHandler propertyHandler = new PropertyHandler();

			//assign the handler
			xmlReader.setContentHandler(propertyHandler);
			//perform the sync parse
			xmlReader.parse(new InputSource(new StringReader(xml)));
			Log.d("XML PARSE","PARSING");
			return propertyHandler.retrievePropertyList();
		}catch(Exception e){
			Log.d("XML PARSE","PARSING FAILED");
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<ContactReason> parseContactReasons(String xml){
		try{
			
			XMLReader xmlReader = initializeReader();
			ContactReasonHandler contactReasonHandler = new ContactReasonHandler();

			//assign the handler
			xmlReader.setContentHandler(contactReasonHandler);
			//perform the sync parse
			xmlReader.parse(new InputSource(new StringReader(xml)));
			Log.d("XML PARSE","PARSING");
			return contactReasonHandler.retrieveContactReasons();
		}catch(Exception e){
			Log.d("XML PARSE","PARSING FAILED");
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<ReportProblem> parseProblemList(String xml){
		try{
			
			XMLReader xmlReader = initializeReader();
			ReportProblemHandler reportProblemHandler = new ReportProblemHandler();

			//assign the handler
			xmlReader.setContentHandler(reportProblemHandler);
			//perform the sync parse
			xmlReader.parse(new InputSource(new StringReader(xml)));
			Log.d("XML PARSE","PARSING");
			return reportProblemHandler.retrieveProblemReasons();
		}catch(Exception e){
			Log.d("XML PARSE","PARSING FAILED");
			e.printStackTrace();
			return null;
		}
	}
	
	public Confirmation parseConfirmationXml(String xml){
		try{
			
			XMLReader xmlReader = initializeReader();
			ConfirmationHandler confirmationHandler = new ConfirmationHandler();

			//assign the handler
			xmlReader.setContentHandler(confirmationHandler);
			//perform the sync parse
			xmlReader.parse(new InputSource(new StringReader(xml)));
			Log.d("XML PARSE","PARSING");
			return confirmationHandler.retrieveConfirmation();
		}catch(Exception e){
			Log.d("XML PARSE","PARSING FAILED");
			e.printStackTrace();
			return null;
		}
	}
}
