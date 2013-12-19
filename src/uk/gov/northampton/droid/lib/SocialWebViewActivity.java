package uk.gov.northampton.droid.lib;

import uk.gov.northampton.droid.R;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;

public class SocialWebViewActivity extends SherlockFragment {

	private WebView wv;
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.web_view, container, false);
		loadSocialWidget(view);
        return view;
        
    }
	
	public void loadSocialWidget(View v) {
		
		wv = (WebView) v.findViewById(R.id.webview);
		wv.setWebViewClient(new WebViewClientNoRedirect());
		String wvContent = "<html><body>" +
				"<a class=\"twitter-timeline\" href=\"https://twitter.com/NorthamptonBC/northampton-2\" data-widget-id=\"398493306922864640\" data0>" +
				"Tweets from @NorthamptonBC/northampton-2</a>" +
				"<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+\"://platform.twitter.com/widgets.js\";fjs.parentNode.insertBefore(js,fjs);}}(document,\"script\",\"twitter-wjs\");</script>" +
				"</body></html>";
		
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setDomStorageEnabled(true);
		wv.loadDataWithBaseURL(getString(R.string.social_base_url), wvContent, "text/html", null, null);
		
	}
	
	private class WebViewClientNoRedirect extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            return false;
	    }
	    
	    @Override
	    public void onPageFinished(WebView view, String url) {
	    	//setSupportProgressBarIndeterminateVisibility(false);
	    	super.onPageFinished(view, url);
	    }
	    
	}

}
