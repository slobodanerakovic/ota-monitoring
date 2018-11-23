package ota.monitoring.analytics.dto;

public class VehiclePackageInfoDTO {

	private String packageName;
	private double packageVersion;
	private String downloadDate;
	private String installmentDate;
	private String successDownload;
	private String successInstallment;
	private String featureList;

	public String getDownloadDate() {
		return downloadDate;
	}

	public void setDownloadDate(String downloadDate) {
		this.downloadDate = downloadDate;
	}

	public String getInstallmentDate() {
		return installmentDate;
	}

	public void setInstallmentDate(String installmentDate) {
		this.installmentDate = installmentDate;
	}

	public void setPackageVersion(double packageVersion) {
		this.packageVersion = packageVersion;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Double getPackageVersion() {
		return packageVersion;
	}

	public void setPackageVersion(Double packageVersion) {
		this.packageVersion = packageVersion;
	}

	public String getSuccessDownload() {
		return successDownload;
	}

	public void setSuccessDownload(String successDownload) {
		this.successDownload = successDownload;
	}

	public String getSuccessInstallment() {
		return successInstallment;
	}

	public void setSuccessInstallment(String successInstallment) {
		this.successInstallment = successInstallment;
	}

	public String getFeatureList() {
		return featureList;
	}

	public void setFeatureList(String featureList) {
		this.featureList = featureList;
	}

}
