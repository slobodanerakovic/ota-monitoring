package ota.monitoring.vehicle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import ota.monitoring.vehicle.service.VehicleManager;

@Service
public class ChargingListener {

	@Autowired
	private VehicleManager vehicleManager;

	@JmsListener(id = "charging_done", destination = "queue.vehicle.charging.done", containerFactory = "queueListenerContainerFactory")
	public void onChargingDone() {
		/**
		 * This is actually simulation of Observer, for particular event. In this case,
		 * this observer would be notified upon vehicle battery charging is done. <br />
		 * Once done, notification about date/time and geolocation would be sent to
		 * backend system, for analytics purpose
		 */
		vehicleManager.updateChargingData();
	}

}
