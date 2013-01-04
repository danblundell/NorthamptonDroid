package uk.gov.northampton.droid;

import java.io.Serializable;

public class SocialEntry implements Serializable{

	private static final long serialVersionUID = 1L;
	public String date;
	public String type;
	public String typeDesc;
	public String text;
	public String url;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeDesc() {
		return typeDesc;
	}
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.text);
		sb.append(" | sent via ");
		sb.append(this.type);
		return sb.toString();
	}
	
	
	
}
