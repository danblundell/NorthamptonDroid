package uk.gov.northampton.droid.lib;

import uk.gov.northampton.droid.Confirmation;

public class ConfirmationRetriever {
	
	private XmlParser xmlParser = new XmlParser();
	
	public Confirmation retrieveConfirmation(String xml) {
		return xmlParser.parseConfirmationXml(xml);
	}
	
	

}
