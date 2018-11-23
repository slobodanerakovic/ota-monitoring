package ota.monitoring.vehicle.model;

public class Sensor {

	public Sensor(String name, String description) {
		this.name = name;
		this.description = description;
	}

	private String name;
	private String description;
	private byte[] data;

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

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
