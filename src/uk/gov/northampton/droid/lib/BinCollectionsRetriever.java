package uk.gov.northampton.droid.lib;

import java.util.ArrayList;

import uk.gov.northampton.droid.Property;

public class BinCollectionsRetriever {
	
	private HttpRetriever httpRetriever = new HttpRetriever();
	private XmlParser xmlParser = new XmlParser();
	
	public ArrayList<Property> retrieveBinCollections(String url) {
		
		String response = httpRetriever.retrieve(url);
		return xmlParser.parseBinCollectionsResponse(response);
	}
	
	

}
