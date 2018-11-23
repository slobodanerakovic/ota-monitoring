package ota.monitoring.vehicle.service;

import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ota.monitoring.backend.esb.events.PackageReleaseEvent;
import ota.monitoring.shared.dto.NotificationReleaseResultDTO;
import ota.monitoring.shared.enums.PackageAction;
import ota.monitoring.vehicle.http.HttpConnection;
import ota.monitoring.vehicle.http.HttpConnectionUtil;

@Service
public class NotificationEventManager {
	private static final Logger LOG = LoggerFactory.getLogger(NotificationEventManager.class);

	@Autowired
	private HttpConnection http;

	@Autowired
	private VehicleUtils utils;

	@Value("${backend.notification.release-status-url}")
	private String releaseNotificationURL;

	public void publishEvent(PackageAction action, String eventStatus, PackageReleaseEvent releaseEvent) {

		NotificationReleaseResultDTO dto = generateNotificationReleaseResultDTO(eventStatus, action, releaseEvent);
		ObjectMapper mapper = new ObjectMapper();

		String json = "";
		try {
			json = mapper.writeValueAsString(dto);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		StringEntity entity = HttpConnectionUtil.buildJSONEntity(json);

		LOG.info("Sending package release event to callback");
		boolean successNotification = http.post(releaseNotificationURL, entity);

		if (!successNotification)
			utils.reschedule("NotificationEventManager", releaseNotificationURL, entity);
	}

	private NotificationReleaseResultDTO generateNotificationReleaseResultDTO(String status, PackageAction action,
			PackageReleaseEvent event) {
		NotificationReleaseResultDTO dto = new NotificationReleaseResultDTO();
		String vehicleId = event.getVehicleId();
		dto.setStatus(status);
		dto.setVehicleId(vehicleId);
		dto.setAction(action);
		dto.setPackageName(event.getPackageName());
		dto.setFeatureName(event.getFeatureName());
		dto.setDescription(event.getDescription());
		dto.setVersion(event.getVersion());

		return dto;
	}
}