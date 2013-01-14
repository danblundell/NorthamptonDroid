package uk.gov.northampton.droid;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;

import uk.gov.northampton.droid.lib.HttpRetriever;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class ContactReasonsAdapter extends BaseAdapter implements SpinnerAdapter {

	private ArrayList<ContactReason> contactDataItems;
	private Activity context;

	public ContactReasonsAdapter(ArrayList<ContactReason> content,Activity activity){
		super();
		this.context = activity;
		this.contactDataItems = content;
	}
	
	public int getCount(){
		return contactDataItems.size();
	}
	
	public ContactReason getItem(int position){
		return contactDataItems.get(position);
	}
	
	public long getItemId(int position){
		return position;
		
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		
		final LayoutInflater inflater = context.getLayoutInflater();
		final View spinnerEntry = inflater.inflate(R.layout.spinner_single_text, null);
		final TextView rowText = (TextView) spinnerEntry.findViewById(R.id.full_line_text);
		final ContactReason currentEntry = contactDataItems.get(position);
		rowText.setText(currentEntry.geteDesc());
		return spinnerEntry;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View view = (View) View.inflate(context, R.layout.spinner_single_text_padded,null);
		TextView text = (TextView) view.findViewById(R.id.full_line_text_padded);
		text.setText(contactDataItems.get(position).geteDesc());
		return view;
	}



}
