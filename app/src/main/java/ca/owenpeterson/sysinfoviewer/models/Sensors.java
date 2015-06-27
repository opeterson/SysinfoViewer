package ca.owenpeterson.sysinfoviewer.models;


public class Sensors {
	private String requestDate;
	private AdapterList adapters;
		
	public Sensors(AdapterList adapters, String requestDate) {
		this.adapters = adapters;
		this.requestDate = requestDate;
	}
	
	public Sensors(){}
		
	public String getRequestDate() {
		return requestDate;
	}

	public AdapterList getAdapters() {
		return adapters;
	}

	public void setAdapters(AdapterList adapters) {
		this.adapters = adapters;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
}
