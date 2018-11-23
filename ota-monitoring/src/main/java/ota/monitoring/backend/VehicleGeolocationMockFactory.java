package ota.monitoring.backend;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import ota.monitoring.backend.db.model.Vehicle;
import ota.monitoring.backend.db.model.VehicleGeoLocation;
import ota.monitoring.backend.db.repository.VehicleGeoLocationRepository;
import ota.monitoring.backend.db.repository.VehicleRepository;

@Component
public class VehicleGeolocationMockFactory {
	private static final Logger LOG = LoggerFactory.getLogger(VehicleGeolocationMockFactory.class);

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private VehicleGeoLocationRepository vehicleGeoRepository;

	private Random r = new Random();

	@EventListener(ApplicationReadyEvent.class)
	public void initVehicleGeolocationRepository() {
		LOG.info("Initiate vechile geolocation data...");
		List<Vehicle> vehicles = vehicleRepository.query("SELECT v FROM Vehicle v", Vehicle.class).getResultList();

		vehicles.forEach(v -> {
			int counter = 0;

			while (counter < 24) {
				VehicleGeoLocation vg = new VehicleGeoLocation();
				String country = v.getFleetId().split("-")[0];
				vg.setCountry(country);
				vg.setVehicle(v);

				if (country.equals("germany")) {
					calculateGermanyLatLong(vg);
				} else {
					calculateSwissLatLong(vg);
				}
				vehicleGeoRepository.persist(vg);
				++counter;
			}
		});
		LOG.info("Vechile geolocation initialization finished !");

	}

	void calculateGermanyLatLong(VehicleGeoLocation vg) {
		String lat = "52." + String.format("%.6g%n", r.nextDouble()).substring(2);
		String lon = "13." + String.format("%.6g%n", r.nextDouble()).substring(2);

		vg.setLatitude(Double.parseDouble(lat));
		vg.setLongitude(Double.parseDouble(lon));
	}

	void calculateSwissLatLong(VehicleGeoLocation vg) {
		String lat = "46.7" + String.format("%.5g%n", r.nextDouble()).substring(2);
		String lon = "8." + String.format("%.6g%n", r.nextDouble()).substring(2);

		vg.setLatitude(Double.parseDouble(lat));
		vg.setLongitude(Double.parseDouble(lon));
	}

}
