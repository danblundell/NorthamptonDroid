package uk.gov.northampton.droid.lib;


import uk.gov.northampton.droid.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class PhotoChooserDialogFragment extends DialogFragment {
	
	public interface PhotoChooserDialogListener {
        public void onFinishPhotoChooserDialog(int which);
    }
	
	PhotoChooserDialogListener dListener;
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            dListener = (PhotoChooserDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement PhotoChooserDialogListener");
        }
    }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.report_photo_dialog_title)
		.setItems(R.array.report_photo_dialog_options, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dListener.onFinishPhotoChooserDialog(which);
				dialog.dismiss();
			}
			
		});
		
		return builder.create();
	}

}
