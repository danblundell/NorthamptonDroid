package uk.gov.northampton.droid.lib;


import uk.gov.northampton.droid.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;

public class EditTextDialogFragment extends DialogFragment {
	
	private String title;
	private int viewId;
	private String out;
	private int inputType;

	public EditTextDialogFragment(){
		super();
		Bundle bundle = this.getArguments();
		
		if(bundle != null) {
			this.title = bundle.getString("title");
			this.viewId = bundle.getInt("viewId");
			this.inputType = bundle.getInt("inputType");
		}
		
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getViewId() {
		return viewId;
	}

	public void setViewId(int viewId) {
		this.viewId = viewId;
	}
	
	public void setInputType(int inputType) {
		this.inputType = inputType;
	}
	
	public int getInputType() {
		return inputType;
	}

	public interface EditTextDialogListener {
        public void onFinishEditTextDialog(int option, String string, int viewId);
    }
	
	EditTextDialogListener dListener;
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            dListener = (EditTextDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement EditTextDialogListener");
        }
    }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View v = getActivity().getLayoutInflater().inflate(R.layout.settings_option_row, null);
		final EditText editText = (EditText) v.findViewById(R.id.settings_opt_input_EditText);
		editText.setInputType(this.inputType);
		editText.setFocusable(true);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(this.getTitle());
		builder.setView(v);
		
		
		builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int opt) {
				out = editText.getText().toString();
				if(out.length() == 0){
					opt = DialogInterface.BUTTON_NEGATIVE;
				}
				dListener.onFinishEditTextDialog(opt, out, viewId);
				dialog.dismiss();
			}
			
		});
		
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int opt) {
				dListener.onFinishEditTextDialog(opt, null, viewId);
				dialog.dismiss();
			}
			
		});
		
		return builder.create();
	}

}
