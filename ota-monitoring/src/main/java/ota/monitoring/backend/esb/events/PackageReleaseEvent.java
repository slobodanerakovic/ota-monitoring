package ota.monitoring.backend.esb.events;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ota.monitoring.shared.enums.PackageInstallmentType;

public class PackageReleaseEvent implements Serializable {

	private PackageInstallmentType type;
	private String packageName;
	private String featureName;
	private String description;
	private Double version;
	private byte[] payload;
	private String vehicleId;

	public PackageReleaseEvent(PackageInstallmentType type, String packageName, String featureName, Double version,
			byte[] payload, String description) {
		this.type = type;
		this.packageName = packageName;
		this.featureName = featureName;
		this.version = version;
		this.payload = payload;
		this.description = description;
	}

	@JsonCreator
	public static PackageReleaseEvent newInstance(@JsonProperty("type") final PackageInstallmentType type,
			@JsonProperty("package") final String packageName, @JsonProperty("feature") final String featureName,
			@JsonProperty("version") final Double version, @JsonProperty("payload") final byte[] payload,
			@JsonProperty("description") final String description) {

		return new PackageReleaseEvent(type, packageName, featureName, version, payload, description);
	}

	public PackageInstallmentType getType() {
		return type;
	}

	public void setType(PackageInstallmentType type) {
		this.type = type;
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

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
