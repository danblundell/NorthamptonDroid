package uk.gov.northampton.droid.fragments;

import com.actionbarsherlock.app.SherlockFragment;

import uk.gov.northampton.droid.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Find extends SherlockFragment {
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.find_it_postcode, container, false);
        
        return v;
    }
}
