package ota.monitoring.vehicle.service;

import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ota.monitoring.shared.dto.GeoLocationDTO;
import ota.monitoring.vehicle.http.HttpConnection;

@Component
public class VehicleUtils {
	private static final Logger LOG = LoggerFactory.getLogger(VehicleUtils.class);

	@Autowired
	private HttpConnection http;

	public void reschedule(String source, String url, StringEntity entity) {
		int reAttempts = 0;
		try {
			Thread.sleep(60 * 1000);
			while (reAttempts < 6) {

				boolean successNotification = http.post(url, entity);
				if (!successNotification) {
					++reAttempts;
				} else {
					LOG.info("{} - RE-SENDING package release event to callback SUCCEED after {} reAttempts", source,
							reAttempts);
					reAttempts = 0;
					break;
				}
			}
		} catch (InterruptedException e) {
		}

		if (reAttempts != 0) {
			LOG.info("{source} - RE-SENDING package release event to callback FAILED after 5 reAttempts", source);
		}
	}

	public GeoLocationDTO getCurrentGeoLocationDTO(String vehicleId) {
		/**
		 * Retrieved location from internet geolocation service (since Vehicle will be
		 * connected to 3g network). Here I am just using some dummy data
		 */
		GeoLocationDTO dto = new GeoLocationDTO();

		dto.setLatitude(52.5074);
		dto.setLongitude(13.3904);
		dto.setVehicleId(vehicleId);

		return dto;
	}

	public Integer getBateryChargeInPercent() {
		/**
		 * Find out from Vehicle system , how much of battery capacity is charged.
		 * <br />
		 * The value is expressed through percentage
		 */
		return 100;
	}
}
