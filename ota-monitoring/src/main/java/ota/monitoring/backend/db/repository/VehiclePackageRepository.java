package ota.monitoring.backend.db.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import ota.monitoring.backend.db.model.Vehicle;
import ota.monitoring.backend.db.model.VehiclePackage;

@Repository
public class VehiclePackageRepository extends DatabaseRepository<VehiclePackage> {

	public VehiclePackage getVehiclePackage(Vehicle vehicle, String packageName, Double version) {

		String sql = "SELECT vp FROM VehiclePackage vp WHERE vp.vehicle = :vehicle AND packageName = :name AND packageVersion = :version";

		VehiclePackage existingVPackage = query(sql, VehiclePackage.class).setParameter("vehicle", vehicle)
				.setParameter("name", packageName).setParameter("version", version).getSingleResult();

		return existingVPackage;
	}

	public List<VehiclePackage> getVehiclePackages(Vehicle vehicle) {
		String sql = "SELECT vp FROM VehiclePackage vp WHERE vp.vehicle = :vehicle";

		List<VehiclePackage> packagesOfVehicle = query(sql, VehiclePackage.class).setParameter("vehicle", vehicle)
				.getResultList();

		return packagesOfVehicle;
	}

}
