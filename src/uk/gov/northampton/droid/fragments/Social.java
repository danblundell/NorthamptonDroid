package uk.gov.northampton.droid.fragments;

import java.util.ArrayList;

import com.actionbarsherlock.ActionBarSherlock.OnCreateOptionsMenuListener;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.SocialEntry;
import uk.gov.northampton.droid.SocialFeedAdapter;
import uk.gov.northampton.droid.lib.CustomWebViewActivity;
import uk.gov.northampton.droid.lib.SocialFeedRetriever;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

@SuppressLint("ParserError")
public class Social extends SherlockListFragment {
	
	 private ArrayList<SocialEntry> feedList = new ArrayList<SocialEntry>();
	 private SocialFeedRetriever sfr = new SocialFeedRetriever();
	 private ProgressBar pb;
	 private ActionBar ab;
	 
	 private static final String SOCIAL_FEED_LIST_KEY = "SOCIAL_FEED_LIST_KEY";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		if(savedInstanceState != null){
			feedList = (ArrayList<SocialEntry>) savedInstanceState.getSerializable(SOCIAL_FEED_LIST_KEY);
			Log.d("SOCIAL FEED","Using saved instance");
			populateSocialFeedList();
		}
		else{
			Log.d("SOCIAL NEW","Using new instance");
			retrieveSocialFeed();
		}
		
		ab = getSherlockActivity().getSupportActionBar();
	}
	
	public void retrieveSocialFeed(){
		String socialFeedUrl = getString(R.string.social_feed_url);
		RetrieveSocialFeedTask sf = new RetrieveSocialFeedTask();
		sf.execute(socialFeedUrl);
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.social, container, false);
		findAllViewsById(view);
        return view;		
    }
	
	public void findAllViewsById(View v){
		pb = (ProgressBar) v.findViewById(R.id.socialProgressBar);
	}
	
	@Override
	 public void onListItemClick(ListView l, View v, int position, long id) {
		SocialEntry current = (SocialEntry) getListView().getItemAtPosition(position);
		String uriString = current.getUrl();
		Intent seIntent = new Intent(l.getContext(), CustomWebViewActivity.class);
		seIntent.putExtra("url", uriString);
		startActivity(seIntent);
	}
	
	private class RetrieveSocialFeedTask extends AsyncTask<String, Void, ArrayList<SocialEntry>>{

		@Override
		protected ArrayList<SocialEntry> doInBackground(String... params) {
			Log.d("SOCIAL FEED", "Getting Social Feed");
			String url = getString(R.string.social_feed_url);
			return 	sfr.retrieveSocialFeed(url);
		}
		
		@Override
		protected void onPostExecute(final ArrayList<SocialEntry> result){
			hideProgress();
			feedList = result;
			populateSocialFeedList();
		}
	}
	
	private void populateSocialFeedList(){
		ListAdapter myListAdapter = new SocialFeedAdapter(getActivity(),R.layout.social_feed_row, this.feedList);
		setListAdapter(myListAdapter);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		hideProgress();
		Log.d("Social Feed", "Resumed!!");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d("Social Feed", "Stopped!!");
	}
	
	public void hideProgress(){
		if(pb.getVisibility() == View.VISIBLE){
			pb.setVisibility(View.GONE);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(SOCIAL_FEED_LIST_KEY, feedList);
		super.onSaveInstanceState(outState);
		
	}	

}
