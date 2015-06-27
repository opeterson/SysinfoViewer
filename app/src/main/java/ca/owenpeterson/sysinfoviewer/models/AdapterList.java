package ca.owenpeterson.sysinfoviewer.models;

import java.util.ArrayList;
import java.util.List;

public class AdapterList {
private List<Adapter> adapterList;
	
	public AdapterList(List<Adapter> adapters) {
		this.adapterList = adapters;		
	}
	
	public AdapterList(){
		this.adapterList = new ArrayList<>();
	}

	public List<Adapter> getAdapters() {
		return adapterList;
	}

	public void setAdapters(List<Adapter> adapters) {
		this.adapterList = adapters;
	}
	
	public void add(Adapter adapter) {
		this.adapterList.add(adapter);
	}
}
