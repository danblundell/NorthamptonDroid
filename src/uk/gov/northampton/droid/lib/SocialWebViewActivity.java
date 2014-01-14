package uk.gov.northampton.droid.lib;

import uk.gov.northampton.droid.R;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;

public class SocialWebViewActivity extends SherlockFragment {

	public WebView wv;
	private Bundle webViewBundle;
	
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

		
		if(webViewBundle != null) {
			wv.restoreState(webViewBundle);
		}
		else {
			String wvContent = "<html><head><style>.twitter-timeline{text-decoration:none;}</style></head><body>" +
					"<style type=\"text/css\">#twitter-widget-0{width:100%;}</style>"+
					"<a class=\"twitter-timeline\" href=\"https://twitter.com/NorthamptonBC/northampton-2\" data-widget-id=\"398493306922864640\" style=\"display:block;text-align:center; color:#A52449;\" data-chrome=\"noheader nofooter transparent\" data-link-color=\"#A52449\">" +
					"Loading...</a>" +
					"<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+\"://platform.twitter.com/widgets.js\";fjs.parentNode.insertBefore(js,fjs);}}(document,\"script\",\"twitter-wjs\");</script>" +
					"</body></html>";
			
			wv.getSettings().setJavaScriptEnabled(true);
			wv.getSettings().setDomStorageEnabled(true);
			wv.loadDataWithBaseURL(getString(R.string.social_base_url), wvContent, "text/html", null, null);
		}
		
		
	}
	
	private class WebViewClientNoRedirect extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	    	Log.d("URL",url);
	    	Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	        startActivity(intent);
	    	return true;
	    }
	    
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		webViewBundle = outState;
		wv.saveState(webViewBundle);
	}

}
