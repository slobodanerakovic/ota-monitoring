package ota.monitoring.vehicle.service;

import java.util.Date;

import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ota.monitoring.shared.dto.ChargingDoneDTO;
import ota.monitoring.shared.dto.GeoLocationDTO;
import ota.monitoring.vehicle.http.HttpConnection;
import ota.monitoring.vehicle.http.HttpConnectionUtil;

@Service
public class VehicleManager {

	@Autowired
	private HttpConnection http;

	@Autowired
	private VehicleUtils utils;

	@Value("${backend.notification.geolocation-url}")
	private String geolocationURL;

	@Value("${backend.notification.charge-done-url}")
	private String chargeDoneURL;

	private String vehicleId;

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public ChargingDoneDTO getChargingDoneDTO() {
		/**
		 * Retrieved location from internet geolocation service (since Vehicle will be
		 * connected to 3g network). Here I am just using some dummy data. <br />
		 * Battery
		 */
		ChargingDoneDTO chargeDTO = new ChargingDoneDTO();
		chargeDTO.setGeoLocationDTO(utils.getCurrentGeoLocationDTO(getVehicleId()));
		chargeDTO.setDate(new Date());
		chargeDTO.setBaterryChargedPercentage(utils.getBateryChargeInPercent());

		return chargeDTO;
	}

	public void updateChargingData() {

		ChargingDoneDTO dto = getChargingDoneDTO();
		ObjectMapper mapper = new ObjectMapper();

		String json = "";
		try {
			json = mapper.writeValueAsString(dto);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		StringEntity entity = HttpConnectionUtil.buildJSONEntity(json);

		boolean successNotification = http.post(chargeDoneURL, entity);

		if (!successNotification)
			utils.reschedule("ChargingListener", chargeDoneURL, entity);
	}

	public void updateVechileGeolocation() {

		GeoLocationDTO dto = utils.getCurrentGeoLocationDTO(getVehicleId());
		ObjectMapper mapper = new ObjectMapper();

		String json = "";
		try {
			json = mapper.writeValueAsString(dto);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		StringEntity entity = HttpConnectionUtil.buildJSONEntity(json);

		boolean successNotification = http.post(geolocationURL, entity);

		if (!successNotification)
			utils.reschedule("NotificationEventManager", geolocationURL, entity);
	}
}
