package ca.owenpeterson.sysinfoviewer.models;

import java.util.List;

/**
 * Created by owen on 6/13/15.
 */
public class Adapter {
    private String name;
    private String type;
    private List<Temperature> temperatures;

    public Adapter(String name, String type, List<Temperature> temperatures) {
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
    public List<Temperature> getTemperatures() {
        return temperatures;
    }
    public void setTemperatures(List<Temperature> temperatures) {
        this.temperatures = temperatures;
    }
}
