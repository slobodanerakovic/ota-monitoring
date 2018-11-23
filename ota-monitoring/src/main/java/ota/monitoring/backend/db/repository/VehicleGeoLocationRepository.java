package ota.monitoring.backend.db.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import ota.monitoring.backend.db.model.Vehicle;
import ota.monitoring.backend.db.model.VehicleGeoLocation;

@Repository
public class VehicleGeoLocationRepository extends DatabaseRepository<VehicleGeoLocation> {

	public List<VehicleGeoLocation> getLast24HoursVehicleGeolocations(Vehicle vehicle) {
		String sql = "SELECT vg FROM VehicleGeoLocation vg WHERE vg.vehicle = :vehicle ORDER BY vg.creationDate DESC";
		List<VehicleGeoLocation> vgList = query(sql, VehicleGeoLocation.class).setParameter("vehicle", vehicle)
				.maxResults(24).getResultList();

		return vgList;
	}

}
