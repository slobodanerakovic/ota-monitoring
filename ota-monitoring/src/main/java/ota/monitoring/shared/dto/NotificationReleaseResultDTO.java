package ota.monitoring.shared.dto;

import ota.monitoring.shared.enums.PackageAction;

public class NotificationReleaseResultDTO {

	private String status;// success / failed
	private String vehicleId;
	private String packageName;
	private String featureName;
	private String description;
	private Double version;
	private PackageAction action;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public Double getVersion() {
		return version;
	}

	public void setVersion(Double version) {
		this.version = version;
	}

	public PackageAction getAction() {
		return action;
	}

	public void setAction(PackageAction action) {
		this.action = action;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "NotificationReleaseResultDTO [status=" + status + ", vehicleId=" + vehicleId + ", packageName="
				+ packageName + ", featureName=" + featureName + ", description=" + description + ", version=" + version
				+ ", action=" + action + "]";
	}

}