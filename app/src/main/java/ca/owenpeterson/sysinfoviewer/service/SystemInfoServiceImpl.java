package ca.owenpeterson.sysinfoviewer.service;

import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import ca.owenpeterson.sysinfoviewer.models.Sensors;
import ca.owenpeterson.sysinfoviewer.utils.ServerUrlStorage;

public class SystemInfoServiceImpl implements SystemInfoService {
		
	private static String RESOURCE_URL = ServerUrlStorage.getServerURL();
	private RestTemplate restTemplate;
	
	public Sensors getSystemSensorInfo() {
		restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		URI resourceLocation = null;
		Sensors sensors = null;
		
		try {
			resourceLocation = new URI(RESOURCE_URL);
			
		} catch (URISyntaxException ex) {
			Log.e(this.getClass().getName(), ex.getMessage());
		}
		
		if (resourceLocation != null) {
			try {
				sensors = restTemplate.getForObject(resourceLocation, Sensors.class);
			} catch (RestClientException rex) {
				Log.e(this.getClass().getName(), "Caught Rest Client Exception. This is likely due to network connectivity problems. \n" + rex.getMessage());
			}
		}
		
		return sensors;
	}
}
