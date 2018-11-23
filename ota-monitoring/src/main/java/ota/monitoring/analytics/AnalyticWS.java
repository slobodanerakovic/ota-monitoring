package ota.monitoring.analytics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;

import ota.monitoring.analytics.dto.VehicleBriefInfoDTO;
import ota.monitoring.analytics.dto.VehicleDetailsDTO;

@RestController
@RequestMapping("/analytics")
public class AnalyticWS {

	@Autowired
	private AnalyticManager analyticManager;

	@GetMapping(value = "/fleet/list", produces = { "application/json" })
	public List<String> getFleetList() {

		return analyticManager.getFleetList();
	}

	@GetMapping(value = "/vehicle/list/{fleetId}", produces = { "application/json" })
	public List<VehicleBriefInfoDTO> getVehicleList(@PathVariable String fleetId) {
		Preconditions.checkArgument(fleetId != null);
		return analyticManager.getVehicleList(fleetId);
	}

	@GetMapping(value = "/vehicle/details/{vehicleId}", produces = { "application/json" })
	public VehicleDetailsDTO getVehicleDetails(@PathVariable String vehicleId) {
		Preconditions.checkArgument(vehicleId != null);
		return analyticManager.getVehicleDetails(Long.parseLong(vehicleId));
	}

}
