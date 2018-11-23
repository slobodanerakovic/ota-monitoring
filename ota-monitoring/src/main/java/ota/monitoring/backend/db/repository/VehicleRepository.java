package ota.monitoring.backend.db.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import ota.monitoring.backend.db.model.Vehicle;

/**
 * @author Slobodan Erakovic
 */
@Repository
public class VehicleRepository extends DatabaseRepository<Vehicle> {

	public List<Vehicle> getVehicleOfFleet(String fleetId) {
		String sql = "SELECT v FROM Vehicle v WHERE v.fleetId = :fleetId";
		List<Vehicle> fleetToUpdate = query(sql, Vehicle.class).setParameter("fleetId", fleetId).getResultList();

		return fleetToUpdate;
	}

}
