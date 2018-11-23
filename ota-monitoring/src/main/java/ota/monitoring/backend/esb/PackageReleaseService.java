package ota.monitoring.backend.esb;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ota.monitoring.backend.db.model.Feature;
import ota.monitoring.backend.db.model.Vehicle;
import ota.monitoring.backend.db.model.VehiclePackage;
import ota.monitoring.backend.db.repository.FeatureRepository;
import ota.monitoring.backend.db.repository.VehiclePackageRepository;
import ota.monitoring.backend.db.repository.VehicleRepository;
import ota.monitoring.backend.esb.events.PackageReleaseEvent;
import ota.monitoring.backend.esb.model.MockPackageData;
import ota.monitoring.shared.dto.NotificationReleaseResultDTO;
import ota.monitoring.shared.enums.PackageAction;

@Service
public class PackageReleaseService {
	private static final Logger LOG = LoggerFactory.getLogger(PackageReleaseService.class);

	@Autowired
	private VehicleNotificationService vehicleNotificationService;

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private FeatureRepository featureRepository;

	@Autowired
	private VehiclePackageRepository vehiclePackageRepository;

	public void prepareDataForRelease(MockPackageData rawErpData, String fleetId) {
		List<Vehicle> fleet = vehicleRepository.getVehicleOfFleet(fleetId);
		LOG.info("************ F L E E T : {} **************** DATA={}", fleetId, fleet);
		PackageReleaseEvent event = PackageReleaseEvent.newInstance(rawErpData.getType(), rawErpData.getPackageName(),
				rawErpData.getFeatureName(), rawErpData.getVersion(), rawErpData.getPayload(),
				rawErpData.getDescription());

		LOG.info("Package release event prepared!");
		vehicleNotificationService.vehicleFleetNotification(fleet, event);
	}

	@Transactional
	public void handleReleaseCallback(NotificationReleaseResultDTO resultDto) {
		try {
			LOG.info("Handle release notification callback for dto={}", resultDto);

			Vehicle vehicle = vehicleRepository
					.query("SELECT v FROM Vehicle v WHERE v.vehicleId = :vehicleId", Vehicle.class)
					.setParameter("vehicleId", resultDto.getVehicleId()).getSingleResult();

			String sql = "SELECT vp FROM VehiclePackage vp WHERE vp.vehicle = :vehicle AND packageName = :name AND packageVersion = :version";

			VehiclePackage existingVPackage = vehiclePackageRepository.query(sql, VehiclePackage.class)
					.setParameter("vehicle", vehicle).setParameter("name", resultDto.getPackageName())
					.setParameter("version", resultDto.getVersion()).getSingleResult();

			if (existingVPackage != null) {
				// Already exists - Means Download recorded - Update with installment status

				manageReleaseAction(existingVPackage, resultDto);
				existingVPackage.setModificationDate(new Date());
				LOG.info("VehiclePackage successfully updated with installement !");
			} else {
				// Create a new one
				VehiclePackage vPackage = new VehiclePackage();
				vPackage.setVehicle(vehicle);
				vPackage.setPackageName(resultDto.getPackageName());

				Feature feature = new Feature();
				feature.setName(resultDto.getFeatureName());
				feature.setDescription(resultDto.getDescription());
				featureRepository.persist(feature);

				vPackage.addFeature(feature);
				vPackage.setPackageVersion(resultDto.getVersion());
				manageReleaseAction(vPackage, resultDto);

				vehiclePackageRepository.persist(vPackage);
				LOG.info("VehiclePackage successfully created !");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void manageReleaseAction(VehiclePackage vPackage, NotificationReleaseResultDTO resultDto) {
		if (resultDto.getAction() == PackageAction.INSTALL) {
			boolean successStatus = resultDto.getStatus().equals("success") ? true : false;
			vPackage.setSuccessInstallment(successStatus);
		} else {
			boolean successStatus = resultDto.getStatus().equals("success") ? true : false;
			vPackage.setSuccessDownload(successStatus);

			if (!successStatus)
				vPackage.setSuccessInstallment(false);
		}
	}
}
