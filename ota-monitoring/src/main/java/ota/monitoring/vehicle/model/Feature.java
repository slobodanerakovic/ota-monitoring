package ota.monitoring.vehicle.model;

import java.util.ArrayList;
import java.util.List;

public class Feature {

	private List<Sensor> featureSensors = new ArrayList<>();
	private String name;
	private String description;
	private byte[] data;

	public List<Sensor> getFeatureSensors() {
		return featureSensors;
	}

	public void setFeatureSensors(List<Sensor> featureSensors) {
		this.featureSensors = featureSensors;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void addSensor(Sensor sensor) {
		featureSensors.add(sensor);
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
