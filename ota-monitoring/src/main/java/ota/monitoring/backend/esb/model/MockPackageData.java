package ota.monitoring.backend.esb.model;

import ota.monitoring.shared.enums.PackageInstallmentType;

public class MockPackageData {

	public MockPackageData(PackageInstallmentType type, String packageName, String featureName, Double version,
			byte[] payload, String description) {
		this.type = type;
		this.packageName = packageName;
		this.featureName = featureName;
		this.version = version;
		this.payload = payload;
		this.description = description;
	}

	private PackageInstallmentType type;
	private String packageName;
	private String featureName;
	private String description;
	private Double version;
	private byte[] payload;

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

	public PackageInstallmentType getType() {
		return type;
	}

	public void setType(PackageInstallmentType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
