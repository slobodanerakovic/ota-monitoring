package ota.monitoring.analytics.dto;

public class VehicleBriefInfoDTO {

	private long id;
	private String model;
	private String vehicleId;
	private String feetId;
	private String purchaseDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getFeetId() {
		return feetId;
	}

	public void setFeetId(String feetId) {
		this.feetId = feetId;
	}

	public String getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

}
