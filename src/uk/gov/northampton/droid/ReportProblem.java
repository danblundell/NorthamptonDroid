package uk.gov.northampton.droid;

import java.io.Serializable;
import java.util.ArrayList;

public class ReportProblem implements Serializable, Comparable<ReportProblem>{
	private String pDesc;
	private int pNum;

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
