package uk.gov.northampton.droid.lib;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HttpRetriever {

	private CustomHttpClient client;

	public String retrieve(String url, Context context){
		HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT) 
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        
        client = new CustomHttpClient(httpParameters, context);
        
		HttpGet getRequest = new HttpGet(url);
		
		try{
			HttpResponse getResponse = client.execute(getRequest);
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
			e.printStackTrace();
			getRequest.abort();
		}
		return null;
	}

	public InputStream retrieveStream(String url){
		HttpGet getRequest = new HttpGet(url);
		try{
			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();

			if(statusCode != HttpStatus.SC_OK){
				return null;
			}

			HttpEntity getResponseEntity = getResponse.getEntity();
			return getResponseEntity.getContent();

		}
		catch(IOException e){
			getRequest.abort();
		}
		return null;
	}

	public Bitmap retrieveBitmap(String url) throws Exception{
		InputStream inputStream = null;
		try{
			inputStream = this.retrieveStream(url);
			final Bitmap bitmap = BitmapFactory.decodeStream(new FlushedInputStream(inputStream));
			return bitmap;
		}finally{
			Utils.closeStreamQuietly(inputStream);
		}
	}
}
