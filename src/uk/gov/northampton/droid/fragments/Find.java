package uk.gov.northampton.droid.fragments;

import com.actionbarsherlock.app.SherlockFragment;

import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.lib.PostCodeDialogFragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Find extends SherlockFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("Find Activity","Created");
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.find_it_main, container, false);
        
        Button nextBtn = (Button) v.findViewById(R.id.findit_button);
        nextBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Find Fragment","Button Clicked!");
				showPostCodeOptions();
			}
			
		});
        
        return v;
    }
	
	public void showPostCodeOptions() {
	    DialogFragment newFragment = new PostCodeDialogFragment();
	    newFragment.show(getActivity().getSupportFragmentManager(), "postCode");
	}
}
