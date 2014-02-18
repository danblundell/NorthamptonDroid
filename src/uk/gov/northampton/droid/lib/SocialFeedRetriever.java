package uk.gov.northampton.droid.lib;

import java.util.ArrayList;

import android.content.Context;
import uk.gov.northampton.droid.SocialEntry;

public class SocialFeedRetriever {
	
	private HttpRetriever httpRetriever = new HttpRetriever();
	private XmlParser xmlParser = new XmlParser();
	
	public ArrayList<SocialEntry> retrieveSocialFeed(String url, Context context) {
		
		String response = httpRetriever.retrieve(url, context);
		return xmlParser.parseSocialFeedResponse(response);
	}
	
	

}
