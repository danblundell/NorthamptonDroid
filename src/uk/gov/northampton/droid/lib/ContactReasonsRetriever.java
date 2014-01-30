package uk.gov.northampton.droid.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import uk.gov.northampton.droid.ContactReason;

public class ContactReasonsRetriever {
	
	private XmlParser xmlParser = new XmlParser();
	
	public ArrayList<ContactReason> retrieveContactReasons(InputStream is) {
		
		
		BufferedReader r = new BufferedReader(new InputStreamReader(is));
		StringBuilder xml = new StringBuilder();
		String line;
		try {
			while ((line = r.readLine()) != null) {
			    xml.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xmlParser.parseContactReasons(xml.toString());
	}
	
	

}
