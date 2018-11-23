package ota.monitoring.backend.esb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ota.monitoring.backend.NoImplementedOperation;
import ota.monitoring.backend.db.model.Vehicle;
import ota.monitoring.backend.db.model.VehicleGeoLocation;
import ota.monitoring.backend.db.repository.VehicleGeoLocationRepository;
import ota.monitoring.backend.db.repository.VehicleRepository;
import ota.monitoring.backend.esb.events.PackageReleaseEvent;
import ota.monitoring.backend.esb.jms.JmsSender;
import ota.monitoring.shared.dto.ChargingDoneDTO;
import ota.monitoring.shared.dto.GeoLocationDTO;

@Service
public class VehicleNotificationService {

	@Autowired
	private JmsSender sender;

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private VehicleGeoLocationRepository vehicleGeoLocationRepository;

	public void vehicleFleetNotification(final List<Vehicle> fleet, final PackageReleaseEvent event) {
		// for each in fleet do notification
		fleet.forEach(v -> {
			event.setVehicleId(v.getVehicleId());
			sender.notifyVehicle(event);
		});
	}

	public void updateVehicleGeolocation(GeoLocationDTO geoDTO) {
		String sql = "SELECT v FROM Vehicle v WHERE v.vehicleId = :vehicleId";
		Vehicle vehicle = vehicleRepository.query(sql, Vehicle.class).setParameter("vehicleId", geoDTO.getVehicleId())
				.getSingleResult();

		VehicleGeoLocation vgl = new VehicleGeoLocation();
		vgl.setVehicle(vehicle);
		vgl.setLatitude(geoDTO.getLatitude());
		vgl.setLongitude(geoDTO.getLongitude());

		vehicleGeoLocationRepository.persist(vgl);
	}

	@NoImplementedOperation
	public void updateVehicleChargingInfo(ChargingDoneDTO chargeDTO) {
		/**
		 * Update charging data related to specific Vehicle, with all related
		 * informations like: <br />
		 * Geolocation (place of charging) Date/time of charging Battery status (how
		 * much battery is charged)
		 */

	}
}
