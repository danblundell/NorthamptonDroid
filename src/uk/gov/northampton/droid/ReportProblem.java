package uk.gov.northampton.droid;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

public class ReportProblem implements Serializable, Comparable<ReportProblem>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String pDesc;
	private int pNum;
	private double pLat;
	private double pLng;
	private String pDetails;
	private String pLocation;
	private String pAbsoluteImagePath;
	
	
	public String getpLocation() {
		return pLocation;
	}

	public void setpLocation(String pLocation) {
		this.pLocation = pLocation;
	}

	public double getpLat() {
		return pLat;
	}

	public void setpLat(double pLat) {
		this.pLat = pLat;
	}

	public double getpLng() {
		return pLng;
	}

	public void setpLng(double pLng) {
		this.pLng = pLng;
	}

	public String getpAbsoluteImagePath() {
		return pAbsoluteImagePath;
	}

	public void setpAbsoluteImagePath(String pAbsoluteImagePath) {
		this.pAbsoluteImagePath = pAbsoluteImagePath;
	}

	public String getpDetails() {
		return pDetails;
	}

	public void setpDetails(String pDetails) {
		this.pDetails = pDetails;
	}

	public String getpDesc() {
		return pDesc;
	}

	public void setpDesc(String pDesc) {
		this.pDesc = pDesc;
	}

	public int getpNum() {
		return pNum;
	}

	public void setpNum(int pNum) {
		this.pNum = pNum;
	}

	@Override
	public String toString() {
		return this.pDesc;
	}
	
	public int compareTo(ReportProblem r) {
		final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;
		
	    if(this == r)return EQUAL;
		if(this.getpDesc().compareToIgnoreCase(r.getpDesc()) < 0){
			return BEFORE;
		}
		if(this.getpDesc().compareToIgnoreCase(r.getpDesc()) > 0){
			return AFTER;
		}
		return EQUAL;
	}
	
}
