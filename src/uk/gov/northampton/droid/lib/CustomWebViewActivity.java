package uk.gov.northampton.droid.lib;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import uk.gov.northampton.droid.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


public class CustomWebViewActivity extends SherlockActivity {

	private WebView wv;
	private ProgressBar pb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.web_view);
		
		// Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        final Activity activity = this;

		Intent intent = getIntent();
		String url = intent.getStringExtra("url");
		wv = (WebView) findViewById(R.id.webview);
		wv.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int progress) {
			     // Activities and WebViews measure progress with different scales.
			     // The progress meter will automatically disappear when we reach 100%
				Log.d("Loading Web Page", "Progress - " + progress);
			     activity.setProgress(progress * 100);
			   }
		});
		wv.setWebViewClient(new WebViewClientNoRedirect());
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setDomStorageEnabled(true);
		wv.loadUrl(url);
	}
	
	private class WebViewClientNoRedirect extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            return false;

	    }		
	}

}
