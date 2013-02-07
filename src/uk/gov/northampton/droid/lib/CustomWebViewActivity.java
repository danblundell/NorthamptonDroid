package uk.gov.northampton.droid.lib;

import uk.gov.northampton.droid.R;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;

public class CustomWebViewActivity extends SherlockFragmentActivity {

	private WebView wv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.web_view);
		setSupportProgressBarIndeterminateVisibility(true);
		Intent intent = getIntent();
		String url = intent.getStringExtra("url");
		Log.d("URL",url);
		wv = (WebView) findViewById(R.id.webview);
		Log.d("WV","Found web view");
		
		wv.setWebViewClient(new WebViewClientNoRedirect());
		Log.d("WV","Set web view client");
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setDomStorageEnabled(true);
		wv.loadUrl(url);
		Log.d("URL","Loading URL");
	}
	
	private class WebViewClientNoRedirect extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            return false;

	    }
	    
	    @Override
	    public void onPageFinished(WebView view, String url) {
	    	setSupportProgressBarIndeterminateVisibility(false);
	    	super.onPageFinished(view, url);
	    	Log.d("WV","LOADED");
	    }
	    
	}

}
