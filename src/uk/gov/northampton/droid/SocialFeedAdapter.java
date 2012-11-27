package uk.gov.northampton.droid;

import java.util.ArrayList;

import uk.gov.northampton.droid.lib.HttpRetriever;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SocialFeedAdapter extends ArrayAdapter<SocialEntry> {

	private ArrayList<SocialEntry> socialDataItems;
	private Activity context;

	public SocialFeedAdapter(Activity context, int textViewResourceId, ArrayList<SocialEntry> socialDataItems){
		super(context, textViewResourceId, socialDataItems);
		this.context = context;
		this.socialDataItems = socialDataItems;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View view = convertView;
		if(view == null){
			LayoutInflater vi =	(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.social_feed_row, null);
		}

		SocialEntry se = socialDataItems.get(position);

		if(se != null){

			//message
			TextView messageTextView = (TextView) view.findViewById(R.id.message_text_view);
			messageTextView.setText(se.getText());

			//source
			TextView sourceTextView = (TextView) view.findViewById(R.id.source_text_view);
			sourceTextView.setText(se.getDate()+", via " + se.getType());

			//icon
			ImageView thumbImageView = (ImageView) view.findViewById(R.id.social_thumb_icon);
			thumbImageView.setBackgroundResource(R.drawable.ic_launcher);
		}
		return view;
	}

}
