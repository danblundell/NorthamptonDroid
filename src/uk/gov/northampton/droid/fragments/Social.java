package uk.gov.northampton.droid.fragments;

import java.util.ArrayList;
import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.SocialEntry;
import uk.gov.northampton.droid.SocialFeedAdapter;
import uk.gov.northampton.droid.lib.CustomWebViewActivity;
import uk.gov.northampton.droid.lib.SocialFeedRetriever;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

@SuppressLint("ParserError")
public class Social extends ListFragment {
	
	 private ArrayList<SocialEntry> feedList = new ArrayList<SocialEntry>();
	 private SocialFeedRetriever sfr = new SocialFeedRetriever();
	 private ProgressBar pb;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String socialFeedUrl = getString(R.string.social_feed_url);
		RetrieveSocialFeedTask sf = new RetrieveSocialFeedTask();
		sf.execute(socialFeedUrl);  		
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.social, container, false);
		pb = (ProgressBar) view.findViewById(R.id.socialProgressBar);
        return view;		
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
			String url = getString(R.string.social_feed_url);
			return 	sfr.retrieveSocialFeed(url);
		}
		
		@Override
		protected void onPostExecute(final ArrayList<SocialEntry> result){
			hideProgress();
			ListAdapter myListAdapter = new SocialFeedAdapter(getActivity(),R.layout.social_feed_row, result);
			setListAdapter(myListAdapter);
		}
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
		

}
