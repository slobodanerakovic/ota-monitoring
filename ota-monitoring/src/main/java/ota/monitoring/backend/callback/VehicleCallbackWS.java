package ota.monitoring.backend.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ota.monitoring.backend.esb.PackageReleaseService;
import ota.monitoring.backend.esb.VehicleNotificationService;
import ota.monitoring.shared.dto.ChargingDoneDTO;
import ota.monitoring.shared.dto.GeoLocationDTO;
import ota.monitoring.shared.dto.NotificationReleaseResultDTO;

@RestController
@RequestMapping("/notification")
public class VehicleCallbackWS {
	private static final Logger LOG = LoggerFactory.getLogger(VehicleCallbackWS.class);

	@Autowired
	private PackageReleaseService packageReleaseService;

	@Autowired
	private VehicleNotificationService vehicleNotificationService;

	@PostMapping(value = "release/status", consumes = { "application/json" })
	public void releaseStatus(@RequestBody NotificationReleaseResultDTO resultDto) {
		packageReleaseService.handleReleaseCallback(resultDto);
	}

	@PostMapping(value = "charging/done", consumes = { "application/json" })
	public void releaseStatus(@RequestBody ChargingDoneDTO chargeDTO) {
		vehicleNotificationService.updateVehicleChargingInfo(chargeDTO);
	}

	@PostMapping(value = "geolocation", consumes = { "application/json" })
	public void geolocation(@RequestBody GeoLocationDTO geoDTO) {
		vehicleNotificationService.updateVehicleGeolocation(geoDTO);
	}
}
