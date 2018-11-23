package ota.monitoring.backend.db.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.google.common.collect.Sets;

import ota.monitoring.backend.utils.Constants;

@Entity
@Access(AccessType.FIELD)
@Table(name = "vehicle_package", schema = Constants.SCHEME)
public class VehiclePackage extends AbstractEntity {

	@Id
	@NotNull
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(generator = "VEHICLE_PACKAGE_ENTITY_ID_GENERATOR")
	@GenericGenerator(name = "VEHICLE_PACKAGE_ENTITY_ID_GENERATOR", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "monitoring.seq_vehicle_package"),
			@Parameter(name = "optimizer", value = "hilo"), @Parameter(name = "increment_size", value = "1") })
	private long id;

	@NotNull
	@Column(name = "package_name", nullable = false, updatable = false)
	private String packageName;

	@NotNull
	@Column(name = "package_version", nullable = false, updatable = false)
	private Double packageVersion;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "vehicle_id", nullable = false)
	private Vehicle vehicle;

	@Column(name = "success_download")
	private Boolean successDownload;

	@Column(name = "success_installment")
	private Boolean successInstallment;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "vehicle_package_feature", schema = Constants.SCHEME, joinColumns = @JoinColumn(name = "vehicle_package_id"), inverseJoinColumns = @JoinColumn(name = "feature_id"))
	private Set<Feature> features = Sets.newHashSet();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Boolean getSuccessDownload() {
		return successDownload;
	}

	public void setSuccessDownload(Boolean successDownload) {
		this.successDownload = successDownload;
	}

	public Boolean getSuccessInstallment() {
		return successInstallment;
	}

	public void setSuccessInstallment(Boolean successInstallment) {
		this.successInstallment = successInstallment;
	}

	public Double getPackageVersion() {
		return packageVersion;
	}

	public void setPackageVersion(Double packageVersion) {
		this.packageVersion = packageVersion;
	}

	public Set<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(Set<Feature> features) {
		this.features = features;
	}

	public void addFeature(Feature feature) {
		features.add(feature);
	}

	public void setModificationDate(Date date) {
		this.modificationDate = date;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}