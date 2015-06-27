package ca.owenpeterson.sysinfoviewer.models;


public class Adapter {
	private String name;
	private String type;
	private TemperatureList temperatures;
	
	public Adapter(String name, String type, TemperatureList temperatures) {
		this.name = name;
		this.type = type;
		this.temperatures = temperatures;
	}
	
	public Adapter(){}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public TemperatureList getTemperatures() {
		return temperatures;
	}
	public void setTemperatures(TemperatureList temperatures) {
		this.temperatures = temperatures;
	}
}
