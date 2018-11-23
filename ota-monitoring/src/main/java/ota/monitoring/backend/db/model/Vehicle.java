package ota.monitoring.backend.db.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import ota.monitoring.backend.utils.Constants;

@Entity
@Access(AccessType.FIELD)
@Table(name = "vehicle", schema = Constants.SCHEME)
public class Vehicle extends AbstractEntity {

	@Id
	@NotNull
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(generator = "VEHICLE_ENTITY_ID_GENERATOR")
	@GenericGenerator(name = "VEHICLE_ENTITY_ID_GENERATOR", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "monitoring.seq_vehicle"),
			@Parameter(name = "optimizer", value = "hilo"), @Parameter(name = "increment_size", value = "1") })
	private long id;

	@NotNull
	@Column(name = "vehicle_id", nullable = false, updatable = false)
	private String vehicleId;

	@NotNull
	@Column(name = "model", nullable = false, updatable = false)
	private String model;

	@NotNull
	@Column(name = "fleet_id", nullable = false, updatable = false)
	private String fleetId;
	//
	// @ManyToMany(fetch = FetchType.LAZY)
	// @JoinTable(name = "vehicle_geolocation", schema = Constants.SCHEME,
	// joinColumns = @JoinColumn(name = "vehicle_id"), inverseJoinColumns =
	// @JoinColumn(name = "geolocation_id"))
	// private Set<VehicleGeoLocation> geoLocations = Sets.newHashSet();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getFleetId() {
		return fleetId;
	}

	public void setFleetId(String fleetId) {
		this.fleetId = fleetId;
	}
	//
	// public Set<VehicleGeoLocation> getGeoLocations() {
	// return geoLocations;
	// }
	//
	// public void setGeoLocations(Set<VehicleGeoLocation> geoLocations) {
	// this.geoLocations = geoLocations;
	// }
	//
	// public void addGeoLocation(VehicleGeoLocation geoLocation) {
	// geoLocations.add(geoLocation);
	// }

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}
