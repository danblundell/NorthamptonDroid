package uk.gov.northampton.droid.lib;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.net.Uri;
import android.util.Log;

public class ContactHttpSender {

	private DefaultHttpClient client = new DefaultHttpClient();
	private String dataSource;
	private String deviceID;
	private String emailAddress;
	private String phoneNumber;
	private String service;
	private String team;
	private String reason;
	private String postcode;
	private String name;
	private String details;
	
	public ContactHttpSender(String dataSource, String deviceID, String emailAddress, String phoneNumber, String service, String team, String reason, String postcode, String name, String details){
		super();
		this.dataSource = dataSource;
		this.deviceID = deviceID;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.service = service;
		this.team = team;
		this.reason = reason;
		this.postcode = postcode;
		this.name = name;
		this.details = details;
	}
	
	public String send(String url){
		HttpPost postRequest = new HttpPost(url);
		postRequest.addHeader("dataSource", dataSource);
		postRequest.addHeader("deviceID", deviceID);
		postRequest.addHeader("emailAddress", emailAddress);
		postRequest.addHeader("phoneNumber", phoneNumber);
		postRequest.addHeader("service", service);
		postRequest.addHeader("team", team);
		postRequest.addHeader("reason", reason);
		postRequest.addHeader("Postcode", postcode);
		postRequest.addHeader("name", name);
		postRequest.addHeader("details", details);
		
		try{
			Log.d("HTTPSENDER",postRequest.toString());
			HttpResponse getResponse = client.execute(postRequest);
			Log.d("HTTPSENDER","HTTP Respose" + getResponse.toString());
			
			final int statusCode = getResponse.getStatusLine().getStatusCode();
			if(statusCode != HttpStatus.SC_OK){
				Log.w(getClass().getSimpleName(), "Error" + statusCode + " for Request " + url);
				return null;
			}

			HttpEntity getResponseEntity = 	getResponse.getEntity();

			if(getResponseEntity != null){
				return EntityUtils.toString(getResponseEntity);
			}
		}
		catch(IOException e){
			postRequest.abort();
			Log.w(getClass().getSimpleName(), "Error for request " + url, e);
		}
		return null;
	}
}
