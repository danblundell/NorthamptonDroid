package uk.gov.northampton.droid;

import java.io.Serializable;

public class Property implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public String binCollectionDate;
	public String binCollectionType;
	public String binCollectionDay;
	public String address;
	
	public Property(){
	}
	
	
	public String getBinCollectionDate() {
		return binCollectionDate;
	}


	public void setBinCollectionDate(String binCollectionDate) {
		this.binCollectionDate = binCollectionDate;
	}


	public String getBinCollectionType() {
		return binCollectionType;
	}


	public void setBinCollectionType(String binCollectionType) {
		this.binCollectionType = binCollectionType;
	}


	public String getBinCollectionDay() {
		return binCollectionDay;
	}


	public void setBinCollectionDay(String binCollectionDay) {
		this.binCollectionDay = binCollectionDay;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public Property(String address, String collectionDate, String collectionType, String collectionDay){
		this.address = address;
		this.binCollectionDate = collectionDate;
		this.binCollectionDay = collectionDay;
		this.binCollectionType = collectionType;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.address);
		sb.append(" | next collection: ");
		sb.append(this.binCollectionDate);
		return sb.toString();
	}
	
	
	
}
