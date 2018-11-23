package ota.monitoring.analytics;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import ota.monitoring.analytics.dto.GeoLocationInfoDTO;
import ota.monitoring.analytics.dto.VehicleBriefInfoDTO;
import ota.monitoring.analytics.dto.VehicleDetailsDTO;
import ota.monitoring.analytics.dto.VehiclePackageInfoDTO;
import ota.monitoring.backend.db.model.Feature;
import ota.monitoring.backend.db.model.Vehicle;
import ota.monitoring.backend.db.model.VehicleGeoLocation;
import ota.monitoring.backend.db.model.VehiclePackage;
import ota.monitoring.backend.db.repository.VehicleGeoLocationRepository;
import ota.monitoring.backend.db.repository.VehiclePackageRepository;
import ota.monitoring.backend.db.repository.VehicleRepository;

@Service
public class AnalyticManager {
	private static final Logger LOG = LoggerFactory.getLogger(AnalyticManager.class);
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private VehiclePackageRepository vehiclePackageRepository;

	@Autowired
	private VehicleGeoLocationRepository geoLocationRepository;

	public List<String> getFleetList() {
		LOG.info("Retrieving fleet list");
		String sql = "SELECT DISTINCT v.fleetId FROM Vehicle v";
		List<String> fleets = vehicleRepository.query(sql, String.class).getResultList();

		return fleets;
	}

	public List<VehicleBriefInfoDTO> getVehicleList(String fleetId) {
		LOG.info("Retrieving vehicle brief info dto list for fleetId={}", fleetId);

		String sql = "SELECT v FROM Vehicle v WHERE v.fleetId = :fleetId";
		List<Vehicle> vehicles = vehicleRepository.query(sql, Vehicle.class).setParameter("fleetId", fleetId)
				.getResultList();

		List<VehicleBriefInfoDTO> shortList = Lists.newArrayList();
		vehicles.forEach(v -> {
			VehicleBriefInfoDTO dto = new VehicleBriefInfoDTO();
			dto.setId(v.getId());
			dto.setModel(v.getModel());
			dto.setVehicleId(v.getVehicleId());
			dto.setFeetId(v.getFleetId());
			dto.setPurchaseDate(FORMATTER.format(v.getCreationDate()));

			shortList.add(dto);
		});

		return shortList;
	}

	@Transactional
	public VehicleDetailsDTO getVehicleDetails(Long vehicleId) {
		LOG.info("Start Vehicle details construction for vechileId={}", vehicleId);

		String sql = "SELECT v FROM Vehicle v WHERE v.id = :id";
		Vehicle vehicle = vehicleRepository.query(sql, Vehicle.class).setParameter("id", vehicleId).getSingleResult();

		VehicleDetailsDTO detailsDTO = new VehicleDetailsDTO();

		List<VehiclePackageInfoDTO> vpiDTOs = Lists.newArrayList();

		List<VehiclePackage> vehiclePackages = vehiclePackageRepository.getVehiclePackages(vehicle);
		vehiclePackages.forEach(vp -> {
			VehiclePackageInfoDTO dto = new VehiclePackageInfoDTO();

			dto.setDownloadDate(FORMATTER.format(vp.getCreationDate()));
			dto.setInstallmentDate(FORMATTER.format(vp.getModificationDate()));
			dto.setPackageName(vp.getPackageName());
			dto.setPackageVersion(vp.getPackageVersion());
			dto.setSuccessDownload(vp.getSuccessDownload() ? "SUCCESS" : "FAILED");
			dto.setSuccessInstallment(vp.getSuccessInstallment() ? "SUCCESS" : "FAILED");

			Set<Feature> features = vp.getFeatures();
			StringBuilder b = new StringBuilder();

			features.forEach(f -> {
				b.append(f.getName()).append(",");
			});
			dto.setFeatureList(b.toString());
			vpiDTOs.add(dto);
		});

		detailsDTO.setVehiclePackageDTOs(doSorting(vpiDTOs));

		List<GeoLocationInfoDTO> geoInfoDTOs = Lists.newArrayList();
		List<VehicleGeoLocation> geolocations = geoLocationRepository.getLast24HoursVehicleGeolocations(vehicle);

		geolocations.forEach(gl -> {
			GeoLocationInfoDTO dto = new GeoLocationInfoDTO();

			dto.setLatitude(gl.getLatitude());
			dto.setLongitude(gl.getLongitude());

			geoInfoDTOs.add(dto);
		});

		detailsDTO.setGeoLocationInfoDTOs(geoInfoDTOs);
		LOG.info("FINISHED Vehicle details construction for vechileId={}", vehicleId);

		return detailsDTO;
	}

	private List<VehiclePackageInfoDTO> doSorting(List<VehiclePackageInfoDTO> vpiDTOs) {

		Comparator<VehiclePackageInfoDTO> comparator = Comparator.comparing(VehiclePackageInfoDTO::getPackageName);
		comparator = comparator.thenComparing(Comparator.comparing(VehiclePackageInfoDTO::getPackageVersion));
		List<VehiclePackageInfoDTO> sortedPackages = vpiDTOs.stream().sorted(comparator).collect(Collectors.toList());

		return sortedPackages;
	}

}
