package uk.gov.northampton.droid.lib;

import java.util.ArrayList;

import uk.gov.northampton.droid.Property;
import uk.gov.northampton.droid.SocialEntry;
import android.util.Log;

public class BinCollectionsRetriever {
	
	private HttpRetriever httpRetriever = new HttpRetriever();
	private XmlParser xmlParser = new XmlParser();
	
	public ArrayList<Property> retrieveBinCollections(String url) {
		
		String response = httpRetriever.retrieve(url);
		Log.d(getClass().getSimpleName(), response);
		return xmlParser.parseBinCollectionsResponse(response);
	}
	
	

}
