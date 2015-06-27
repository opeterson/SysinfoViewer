package ca.owenpeterson.sysinfoviewer.models;

import java.util.ArrayList;
import java.util.List;

public class TemperatureList {
private List<Temperature> temperatureList;
	
	public TemperatureList(List<Temperature> temperatureList){
		this.temperatureList = temperatureList;
	}
	
	public TemperatureList(){
		this.temperatureList = new ArrayList<>();
	}

	public List<Temperature> getTemperatures() {
		return temperatureList;
	}

	public void setTemperatures(List<Temperature> temperatureList) {
		this.temperatureList = temperatureList;
	}
	
	public void add(Temperature temperature) {
		this.temperatureList.add(temperature);
	}
	
	public void remove(Temperature temperature) {
		this.temperatureList.remove(temperature);
	}
}
