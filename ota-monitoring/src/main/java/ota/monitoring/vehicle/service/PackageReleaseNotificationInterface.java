package ota.monitoring.vehicle.service;

import ota.monitoring.backend.esb.events.PackageReleaseEvent;

public interface PackageReleaseNotificationInterface {

	void onDownloadFailure(PackageReleaseEvent releaseEvent);

	void onDownloadSuccess(PackageReleaseEvent releaseEvent);

	void onInstallationFailure(PackageReleaseEvent releaseEvent);

	void onInstallationSuccess(PackageReleaseEvent releaseEvent);

}
