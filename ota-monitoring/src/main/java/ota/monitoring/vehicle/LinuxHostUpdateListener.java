package ota.monitoring.vehicle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import ota.monitoring.backend.esb.events.PackageReleaseEvent;
import ota.monitoring.vehicle.service.PackageManager;
import ota.monitoring.vehicle.service.VehicleManager;

@Service
public class LinuxHostUpdateListener {

	@Autowired
	private PackageManager packageManager;

	@Autowired
	private VehicleManager vehicleManager;

	@JmsListener(id = "package_release_event", destination = "topic.vehicle.package.release.destination", containerFactory = "topicListenerContainerFactory")
	public void onPackageReleaseEvent(PackageReleaseEvent releaseEvent) {
		/**
		 * I am setting here vehicleId (so this would be app running on vehicle) because
		 * we will need valid vehicleId, in order to send geolocation data to server
		 */
		vehicleManager.setVehicleId(releaseEvent.getVehicleId());

		packageManager.handlePackageRelease(releaseEvent);
	}
}
