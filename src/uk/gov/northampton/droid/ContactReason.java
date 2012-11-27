package uk.gov.northampton.droid;

import java.io.Serializable;
import java.util.ArrayList;

public class ContactReason implements Serializable{
	public String eDesc;
	public String iDesc;
	public ArrayList<ContactReason> reasons;
	
	public ArrayList<ContactReason> getReasons() {
		return reasons;
	}

	public void setReasons(ArrayList<ContactReason> reasons) {
		this.reasons = reasons;
	}


	public String geteDesc() {
		return eDesc;
	}

	public void seteDesc(String eDesc) {
		this.eDesc = eDesc;
	}

	public String getiDesc() {
		return iDesc;
	}

	public void setiDesc(String iDesc) {
		this.iDesc = iDesc;
	}

	@Override
	public String toString() {
		return this.eDesc;
	}
	
	
	
}
