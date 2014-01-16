package uk.gov.northampton.droid.lib;


import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

public class TimePickerDialogFragment extends DialogFragment {
	
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
		Context context = getActivity();
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        
        final TimePicker timePicker = new TimePicker(context);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        
        return new AlertDialog.Builder(context)
        .setTitle("Set Reminder Time")
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                onTimeSet(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                dialog.dismiss();
            }
        })
        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            	dialog.dismiss();
            }
            
        })
        .setView(timePicker)
        .show();        
    }

	public void onTimeSet(int hourOfDay, int minute) {
		dListener.onSetTimeDialog(hourOfDay, minute);
    }

}
