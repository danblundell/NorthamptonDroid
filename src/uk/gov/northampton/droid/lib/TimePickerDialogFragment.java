package uk.gov.northampton.droid.lib;


import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
	
	private boolean timeSet = false;
	
	public interface TimePickerTimeSetDialogListener {
        public void onSetTimeDialog(int hourOfDay, int minute);
    }
	
	TimePickerTimeSetDialogListener dListener;
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            dListener = (TimePickerTimeSetDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement TimePickerTimeSetDialogListener");
        }
    }



	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        TimePickerDialog timePicker = new TimePickerDialog(getActivity(), this, hour, minute, true);
        timePicker.setButton(DialogInterface.BUTTON_POSITIVE, "Set", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				timeSet = true;
				dialog.dismiss();
			}
		});
        timePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				timeSet = false;
				dialog.dismiss();
			}
		});
         return timePicker;
    }

	@Override
	 public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		if(timeSet) {
			dListener.onSetTimeDialog(hourOfDay, minute);
		}
    }

}
