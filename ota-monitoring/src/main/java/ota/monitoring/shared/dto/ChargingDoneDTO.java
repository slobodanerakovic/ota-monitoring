package ota.monitoring.shared.dto;

import java.util.Date;

public class ChargingDoneDTO {

	private GeoLocationDTO geoLocationDTO;
	private Date date;
	private Integer baterryChargedPercentage;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getBaterryChargedPercentage() {
		return baterryChargedPercentage;
	}

	public void setBaterryChargedPercentage(Integer baterryChargedPercentage) {
		this.baterryChargedPercentage = baterryChargedPercentage;
	}

	public GeoLocationDTO getGeoLocationDTO() {
		return geoLocationDTO;
	}

	public void setGeoLocationDTO(GeoLocationDTO geoLocationDTO) {
		this.geoLocationDTO = geoLocationDTO;
	}

	@Override
	public String toString() {
		return "ChargingDoneDTO [geoLocationDTO=" + geoLocationDTO + ", date=" + date + ", baterryChargedPercentage="
				+ baterryChargedPercentage + "]";
	}

}
