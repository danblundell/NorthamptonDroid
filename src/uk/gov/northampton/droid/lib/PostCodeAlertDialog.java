package uk.gov.northampton.droid.lib;

import java.util.ArrayList;

import uk.gov.northampton.droid.R;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class PostCodeAlertDialog extends AlertDialog {
	
	private Context context;
	private final String prefix = "NN";
	private ArrayList<String> digits;
	private ArrayList<String> letters;
	

	public PostCodeAlertDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		//this.digits = digits;
		//this.letters = letters;
	}


	@Override
	public void setView(View view) {
		view = getLayoutInflater().inflate(R.layout.report_1_type, null);
		super.setView(view);
	}
	
	

}
