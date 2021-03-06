package uk.gov.northampton.droid.lib;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class ReportHttpSender {

	private DefaultHttpClient client = new DefaultHttpClient();
	private String dataSource;
	private String includesImage = "false";
	private String deviceID;
	private String problemNumber;
	private String lat;
	private String lng;
	private String desc;
	private String location;
	private String email;
	private String phone;
	private byte[] imageData;
	
	public ReportHttpSender(){
		super();
	}
	
	public ReportHttpSender(String dataSource, String deviceID, String problemNumber, double lat, double lng, String desc, String location, String email, String phone, byte[] imageData){
		super();
		this.dataSource = dataSource;
		this.deviceID = deviceID;
		this.problemNumber = problemNumber;
		this.lat = String.valueOf(lat);
		this.lng = String.valueOf(lng);
		this.desc = desc;
		this.location = location;
		this.email = email;
		this.phone = phone;
		this.imageData = imageData;
	}
	
	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public DefaultHttpClient getClient() {
		return client;
	}

	public void setClient(DefaultHttpClient client) {
		this.client = client;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getIncludesImage() {
		return includesImage;
	}

	public void setIncludesImage(String includesImage) {
		this.includesImage = includesImage;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getProblemNumber() {
		return problemNumber;
	}

	public void setProblemNumber(String problemNumber) {
		this.problemNumber = problemNumber;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String send(String url){
		HttpParams params = new BasicHttpParams();
		int timeout = 60000;
		HttpConnectionParams.setConnectionTimeout(params, timeout);
		client.setParams(params);
		HttpPost postRequest = new HttpPost(url);
		
		if(this.imageData != null){
			HttpEntity imageBytes = new ByteArrayEntity(this.imageData);
			this.setIncludesImage("true");
			postRequest.setEntity(imageBytes);
			
		}
		
		postRequest.addHeader("dataSource",this.dataSource);
		postRequest.addHeader("includesImage",this.includesImage);
		postRequest.addHeader("DeviceID",this.deviceID);
		postRequest.addHeader("ProblemNumber",this.problemNumber);
		postRequest.addHeader("ProblemLatitude",this.lat);
		postRequest.addHeader("ProblemLongitude",this.lng);
		postRequest.addHeader("ProblemDescription",this.desc);
		postRequest.addHeader("ProblemLocation",this.location);
		postRequest.addHeader("ProblemEmail",this.email);
		postRequest.addHeader("ProblemPhone",this.phone); 
		
		
		try{
			HttpResponse getResponse = client.execute(postRequest);
			
			final int statusCode = getResponse.getStatusLine().getStatusCode();
			if(statusCode != HttpStatus.SC_OK){
				return null;
			}

			HttpEntity getResponseEntity = 	getResponse.getEntity();

			if(getResponseEntity != null){
				return EntityUtils.toString(getResponseEntity);
			}
		}
		catch(IOException e){
			postRequest.abort();
		}
		return null;
	}
}
