package ota.monitoring.vehicle.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehiclePackage {

	private List<Feature> packageFeatures = new ArrayList<>();
	private String name;
	private String description;
	private Double version;

	public List<Feature> getPackageFeatures() {
		return packageFeatures;
	}

	public void setPackageFeatures(List<Feature> packageFeatures) {
		this.packageFeatures = packageFeatures;
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

	public Optional<Feature> getFeature(String featureName) {
		return packageFeatures.stream().filter(f -> f.getName().equals(featureName)).findFirst();
	}

	public void addNewFeature(Feature feature) {
		packageFeatures.add(feature);
	}

	public Double getVersion() {
		return version;
	}

	public void setVersion(Double version) {
		this.version = version;
	}

}
