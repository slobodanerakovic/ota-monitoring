package ota.monitoring.vehicle.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ota.monitoring.vehicle.service.VehicleManager;

@Component
public class GeolocationUpdateJob {

	@Autowired
	private VehicleManager vehicleManager;

	// @Scheduled(cron = "0 0 0 1 * ?")
	public void sendGeolocationNotification() {

		vehicleManager.updateVechileGeolocation();
	}
}
