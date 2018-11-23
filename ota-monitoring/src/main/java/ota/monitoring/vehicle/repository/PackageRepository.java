package ota.monitoring.vehicle.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import ota.monitoring.vehicle.model.VehiclePackage;

@Repository
public class PackageRepository {
	private final Map<String, VehiclePackage> vehiclePackages = new HashMap<>();

	public VehiclePackage getPackage(String packageName) {
		return vehiclePackages.get(packageName);
	}

	public void addPackage(VehiclePackage newPackage) {
		vehiclePackages.put(newPackage.getName(), newPackage);
	}
}
