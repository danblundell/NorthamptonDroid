package uk.gov.northampton.droid.lib;


import java.util.ArrayList;

import uk.gov.northampton.droid.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class PostCodeDialogFragment extends DialogFragment {
	
	public ArrayList<String> pc = new ArrayList<String>();

	public interface PostCodeDialogListener {
        public void onFinishPostCodeDialog(String postCode);
    }
	
	PostCodeDialogListener dListener;
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            dListener = (PostCodeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement PostCodeDialogListener");
        }
    } 

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		//get the postcode picker view
		View v = getActivity().getLayoutInflater().inflate(R.layout.postcode_picker, null);
		
		//set up two adapters
		ArrayAdapter<CharSequence> numbers = ArrayAdapter.createFromResource(getActivity(),
		        R.array.postcode_digits, R.layout.postcode_single_text_spinner_selected);
		ArrayAdapter<CharSequence> letters = ArrayAdapter.createFromResource(getActivity(),
		        R.array.postcode_letters, R.layout.postcode_single_text_spinner_selected);
		
		//set custom drop down views
		letters.setDropDownViewResource(R.layout.postcode_single_text_spinner);
		numbers.setDropDownViewResource(R.layout.postcode_single_text_spinner);
		
		//set each spinner content
		Spinner s1 = (Spinner) v.findViewById(R.id.postcode_1_spinner);
		Spinner s2 = (Spinner) v.findViewById(R.id.postcode_2_spinner);
		Spinner s3 = (Spinner) v.findViewById(R.id.postcode_3_spinner);
		Spinner s4 = (Spinner) v.findViewById(R.id.postcode_5_spinner);
		Spinner s5 = (Spinner) v.findViewById(R.id.postcode_6_spinner);
		Spinner s6 = (Spinner) v.findViewById(R.id.postcode_7_spinner);
		
		s1.setAdapter(letters);
		s1.setSelection(13);
		s1.setEnabled(false);
		s2.setAdapter(letters);
		s2.setSelection(13);
		s2.setEnabled(false);
		s3.setAdapter(numbers);
		s3.setSelection(1);
		s4.setAdapter(numbers);
		s4.setSelection(1);
		s5.setAdapter(letters);
		s5.setSelection(3);
		s6.setAdapter(letters);
		s6.setSelection(4);
		
		pc.add("N");
		pc.add("N");
		s3.setOnItemSelectedListener(new PostCodeSpinnerListener(2));
		s4.setOnItemSelectedListener(new PostCodeSpinnerListener(3));
		s5.setOnItemSelectedListener(new PostCodeSpinnerListener(4));
		s6.setOnItemSelectedListener(new PostCodeSpinnerListener(5));
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.postcode_dialog_title);
		builder.setView(v);
		builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int opt) {
				String postCodeOut = returnPostCode();
				dListener.onFinishPostCodeDialog(postCodeOut);
				dialog.dismiss();
			}
			
		});
		
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int opt) {
				dListener.onFinishPostCodeDialog(null);
				dialog.dismiss();
			}
			
		});
		
		return builder.create();
	}
	
	private class PostCodeSpinnerListener implements OnItemSelectedListener {
		
		int loc;
		
		public PostCodeSpinnerListener(int loc){
			this.loc = loc;
		}

		@Override
		public void onItemSelected(AdapterView<?> parentView, View v, int position,
				long id) {
			if(pc.size() > this.loc){
				pc.remove(this.loc);
			}
			pc.add(this.loc,parentView.getItemAtPosition(position).toString());	
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
		
	}
	
	private String returnPostCode(){
		StringBuilder sb = new StringBuilder();
		for (String s : this.pc)
		{
		    sb.append(s);
		}
		sb.insert(3, " ");
		return sb.toString();
	}

}
