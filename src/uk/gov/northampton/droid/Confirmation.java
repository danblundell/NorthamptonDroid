package uk.gov.northampton.droid;

import java.io.Serializable;

public class Confirmation implements Serializable{

	private static final long serialVersionUID = 1L;
	private boolean success;
	private String callNumber;
	private String sla;
	
	public Confirmation(){
		super();
	}
	
	public Confirmation(boolean success, String callNumber, String sla){
		this.success = success;
		this.callNumber = callNumber;
		this.sla = sla;
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getCallNumber() {
		return callNumber;
	}
	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
	}
	public String getSla() {
		return sla;
	}
	public void setSla(String sla) {
		this.sla = sla;
	}
	
	
}
