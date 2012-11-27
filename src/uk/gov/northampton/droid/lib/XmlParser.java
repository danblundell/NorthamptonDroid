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

import uk.gov.northampton.droid.ContactReason;
import uk.gov.northampton.droid.ContactReasonHandler;
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
}
