package uk.gov.northampton.droid;

import java.util.ArrayList;

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
//		if(position == 0){
//			view.setBackgroundResource(R.drawable.round_corners_top);
//		}
//		else if(position == (socialDataItems.size() - 1)){
//			view.setBackgroundResource(R.drawable.round_corners_bottom);
//		}
//		else{
//			view.setBackgroundColor(context.getResources().getColor(R.color.actionBarText));
//		}
		
		if(se != null){
			

			//message
			TextView messageTextView = (TextView) view.findViewById(R.id.message_text_view);
			messageTextView.setText(se.getText());

			//source
			TextView sourceTextView = (TextView) view.findViewById(R.id.source_text_view);
			sourceTextView.setText(se.getDate()+", via " + se.getType());

			//icon
			ImageView thumbImageView = (ImageView) view.findViewById(R.id.social_thumb_icon);
			if(se.getTypeDesc().compareToIgnoreCase("lovenorthampton") == 0){
				thumbImageView.setImageResource(R.drawable.love_main);
			} else{
				thumbImageView.setImageResource(R.drawable.gold_crest_main);
			}
			
			
		}
		return view;
	}

}
