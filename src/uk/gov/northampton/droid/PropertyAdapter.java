package uk.gov.northampton.droid;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class PropertyAdapter extends BaseAdapter implements SpinnerAdapter {

	private ArrayList<Property> propertyList;
	private Activity context;

	public PropertyAdapter(Activity context, ArrayList<Property> propertyList){
		super();
		this.context = context;
		this.propertyList = propertyList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View view = convertView;
		if(view == null){
			LayoutInflater vi =	(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.spinner_single_text, null);
		}

		Property p = propertyList.get(position);
//		if(position == 0){
//			view.setBackgroundResource(R.drawable.round_corners_top);
//		}
//		else if(position == (socialDataItems.size() - 1)){
//			view.setBackgroundResource(R.drawable.round_corners_bottom);
//		}
//		else{
//			view.setBackgroundColor(context.getResources().getColor(R.color.actionBarText));
//		}
		
		if(p != null){
			
			//message
			TextView messageTextView = (TextView) view.findViewById(R.id.full_line_text);
			messageTextView.setText(p.getAddress());

		}
		return view;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View view = (View) View.inflate(context, R.layout.spinner_single_text_padded,null);
		TextView text = (TextView) view.findViewById(R.id.full_line_text_padded);
		text.setText(propertyList.get(position).getAddress());
		return view;
	}

	@Override
	public int getCount() {
		return propertyList.size();
	}

	@Override
	public Object getItem(int position) {
		return propertyList.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

}
