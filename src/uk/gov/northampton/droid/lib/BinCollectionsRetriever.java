package uk.gov.northampton.droid.lib;

import java.util.ArrayList;

import android.content.Context;

import uk.gov.northampton.droid.Property;

public class BinCollectionsRetriever {
	
	private HttpRetriever httpRetriever = new HttpRetriever();
	private XmlParser xmlParser = new XmlParser();
	
	public ArrayList<Property> retrieveBinCollections(String url, Context context) {
		
		String response = httpRetriever.retrieve(url, context);
		return xmlParser.parseBinCollectionsResponse(response);
	}
	
	

}
