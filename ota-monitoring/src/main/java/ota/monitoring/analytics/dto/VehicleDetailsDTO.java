package ota.monitoring.analytics.dto;

import java.util.List;

public class VehicleDetailsDTO {

	List<VehiclePackageInfoDTO> vehiclePackageDTOs;
	List<GeoLocationInfoDTO> geoLocationInfoDTOs;
	OwnerInfoDTO ownerDTO;

	public List<VehiclePackageInfoDTO> getVehiclePackageDTOs() {
		return vehiclePackageDTOs;
	}

	public void setVehiclePackageDTOs(List<VehiclePackageInfoDTO> vehiclePackageDTOs) {
		this.vehiclePackageDTOs = vehiclePackageDTOs;
	}

	public List<GeoLocationInfoDTO> getGeoLocationInfoDTOs() {
		return geoLocationInfoDTOs;
	}

	public void setGeoLocationInfoDTOs(List<GeoLocationInfoDTO> geoLocationInfoDTOs) {
		this.geoLocationInfoDTOs = geoLocationInfoDTOs;
	}

	public OwnerInfoDTO getOwnerDTO() {
		return ownerDTO;
	}

	public void setOwnerDTO(OwnerInfoDTO ownerDTO) {
		this.ownerDTO = ownerDTO;
	}

	@Override
	public String toString() {
		return "VehicleDetailsDTO [vPackageDTO=" + vehiclePackageDTOs + ", geoDTO=" + geoLocationInfoDTOs
				+ ", ownerDTO=" + ownerDTO + "]";
	}
}
