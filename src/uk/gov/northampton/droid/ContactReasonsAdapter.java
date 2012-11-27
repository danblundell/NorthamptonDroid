package uk.gov.northampton.droid;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;

import uk.gov.northampton.droid.lib.HttpRetriever;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class ContactReasonsAdapter extends ArrayAdapter<ContactReason> implements SpinnerAdapter {

	private ArrayList<ContactReason> contactDataItems;
	private Activity context;

	public ContactReasonsAdapter(Activity context, int textViewResourceId, ArrayList<ContactReason> contactDataItems){
		super(context, textViewResourceId, contactDataItems);
		this.context = context;
		this.contactDataItems = contactDataItems;
	}
}
