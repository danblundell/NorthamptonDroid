package uk.gov.northampton.droid.fragments;

import com.actionbarsherlock.app.SherlockFragment;

import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.lib.PostCodeDialogFragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class Find extends SherlockFragment {
	
	private Button nextBtn;
	private Spinner spinner;
	
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
        findAllViewsById(v);
        nextBtn.setOnClickListener(nextButtonClickListener);
        ArrayAdapter<CharSequence> sa = ArrayAdapter.createFromResource(getActivity(), R.array.findit_type_array, R.layout.spinner_single_textview);
        sa.setDropDownViewResource(R.layout.spinner_single_textview_padded);
        spinner.setAdapter(sa);
        return v;
	}
	
	private OnClickListener nextButtonClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			Log.d("Find Fragment","Button Clicked!");
			showPostCodeOptions();
		}
	};
	
	private void findAllViewsById(View v){
		spinner = (Spinner) v.findViewById(R.id.findit_reasons_spinner);
		nextBtn = (Button) v.findViewById(R.id.findit_button);
	}
	
	private void showPostCodeOptions() {
	    DialogFragment newFragment = new PostCodeDialogFragment();
	    newFragment.show(getActivity().getSupportFragmentManager(), "postCode");
	}
}
