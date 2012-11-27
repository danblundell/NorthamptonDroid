package uk.gov.northampton.droid.fragments;

import com.actionbarsherlock.app.SherlockActivity;

import uk.gov.northampton.droid.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Services extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setText(R.string.tab_services);
	}
	
}
