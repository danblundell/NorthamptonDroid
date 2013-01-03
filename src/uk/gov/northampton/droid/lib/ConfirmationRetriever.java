package uk.gov.northampton.droid.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import uk.gov.northampton.droid.Confirmation;
import uk.gov.northampton.droid.ContactReason;
import uk.gov.northampton.droid.R;
import uk.gov.northampton.droid.ReportProblem;
import android.content.res.Resources;
import android.util.Log;

public class ConfirmationRetriever {
	
	private XmlParser xmlParser = new XmlParser();
	
	public Confirmation retrieveConfirmation(String xml) {
		return xmlParser.parseConfirmationXml(xml);
	}
	
	

}
