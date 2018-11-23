package ota.monitoring.vehicle.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import ota.monitoring.backend.esb.events.PackageReleaseEvent;
import ota.monitoring.shared.enums.PackageAction;
import ota.monitoring.shared.enums.PackageInstallmentType;
import ota.monitoring.vehicle.model.Feature;
import ota.monitoring.vehicle.model.Sensor;
import ota.monitoring.vehicle.model.VehiclePackage;
import ota.monitoring.vehicle.repository.PackageRepository;

@Service
public class PackageManager implements PackageReleaseNotificationInterface {
	private static final Logger LOG = LoggerFactory.getLogger(PackageManager.class);

	@Autowired
	private PackageRepository packageRepository;

	@Autowired
	private NotificationEventManager notificationEventManager;

	public void handlePackageRelease(PackageReleaseEvent releaseEvent) {
		/**
		 * SIMULATION OF SUCCESS / FAILED PACKAGE DOWNLOADING
		 */
		if (releaseEvent.getPayload() != null) {
			onDownloadSuccess(releaseEvent);
		} else {
			onDownloadFailure(releaseEvent);
			return;
		}

		if (releaseEvent.getType() == PackageInstallmentType.INSTALL) {
			doPackageInstallment(releaseEvent);
		} else {
			doPackageUpdate(releaseEvent);
		}
	}

	private void doPackageInstallment(PackageReleaseEvent releaseEvent) {
		/**
		 * For anycase, we do check do we have it already installed or not. For perfect
		 * situation this should be non possible, but reality is way away of perfection
		 * (system block on vehicle, some miss-used disruption of the system, which lead
		 * to potential system inconsistent state
		 */
		VehiclePackage existingPackage = packageRepository.getPackage(releaseEvent.getPackageName());

		if (existingPackage == null) {
			VehiclePackage newPackage = prepareMockPackage(releaseEvent);
			newPackage.setVersion(releaseEvent.getVersion());
			packageRepository.addPackage(newPackage);

			installmentInProgress("new package");
			onInstallationSuccess(releaseEvent);
		} else {
			/**
			 * Failed because basic package installation already exists! There is a
			 * difference between basic package installation (which installation means), and
			 * package update for new feature
			 */
			onInstallationFailure(releaseEvent);
		}
	}

	/**
	 * Update of the package version. Package version update means installation of
	 * the new feature, or replacement of the existing one, and version change
	 */
	private void doPackageUpdate(PackageReleaseEvent event) {
		VehiclePackage existingPackage = packageRepository.getPackage(event.getPackageName());

		if (existingPackage == null) {
			onInstallationFailure(event);
		} else {
			Optional<Feature> optFeature = existingPackage.getFeature(event.getFeatureName());

			if (optFeature.isPresent()) {
				existingPackage.setVersion(event.getVersion());
				installmentInProgress("feature upgraded. package version changed");
				onInstallationSuccess(event);
			} else {
				Feature packageFeature = prepareMockPackageFeature(event);
				existingPackage.addNewFeature(packageFeature);
				installmentInProgress("new feature of package");
				onInstallationSuccess(event);
			}
		}
	}

	private VehiclePackage prepareMockPackage(PackageReleaseEvent event) {
		VehiclePackage newPackage = new VehiclePackage();
		newPackage.setName(event.getPackageName());
		newPackage.setPackageFeatures(Lists.newArrayList(prepareMockPackageFeature(event)));
		newPackage.setVersion(event.getVersion());

		return newPackage;
	}

	private Feature prepareMockPackageFeature(PackageReleaseEvent event) {
		Feature feature = new Feature();
		feature.setName(event.getFeatureName());
		feature.setDescription(event.getDescription());
		feature.setData(event.getPayload());

		feature.setFeatureSensors(
				Lists.newArrayList(new Sensor("telemetric", "Distance measuring between car and objects")));

		return feature;
	}

	private void installmentInProgress(String type) {
		LOG.info("Installing {} in progress....", type);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * NOTIFICATION EVENTS OVERRIDDEN METHODS
	 */
	public void onDownloadFailure(PackageReleaseEvent releaseEvent) {
		notificationEventManager.publishEvent(PackageAction.DOWNLOAD, "failed", releaseEvent);
	}

	public void onDownloadSuccess(PackageReleaseEvent releaseEvent) {
		notificationEventManager.publishEvent(PackageAction.DOWNLOAD, "success", releaseEvent);
	}

	public void onInstallationFailure(PackageReleaseEvent releaseEvent) {
		notificationEventManager.publishEvent(PackageAction.INSTALL, "failed", releaseEvent);
	}

	public void onInstallationSuccess(PackageReleaseEvent releaseEvent) {
		notificationEventManager.publishEvent(PackageAction.INSTALL, "success", releaseEvent);
	}

}
