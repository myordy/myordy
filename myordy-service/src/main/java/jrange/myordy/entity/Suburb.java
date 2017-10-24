package jrange.myordy.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="suburb")
public final class Suburb implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="suburb_id")
    private Integer suburbId;

	@Column(name="postcode", nullable = false)
    private String postcode;

	@Column(name="code", nullable = false, unique = true)
    private String code;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id", nullable = false, updatable = false)
    private State state;

    @Column(name="geocode_latitude", nullable = true)
    private String geocodeLatitude;

    @Column(name="geocode_longitude", nullable = true)
    private String geocodeLongitude;

    @Transient
    private String name;

    @Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

	public Integer getSuburbId() {
		return suburbId;
	}

	public void setSuburbId(final Integer suburbId) {
		this.suburbId = suburbId;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(final String postcode) {
		this.postcode = postcode;
	}

	public State getState() {
		return state;
	}

	public void setState(final State state) {
		this.state = state;
	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(final EntityStatus status) {
		this.status = status;
	}

	public String getGeocodeLatitude() {
		return geocodeLatitude;
	}

	public void setGeocodeLatitude(final String geocodeLatitude) {
		this.geocodeLatitude = geocodeLatitude;
	}

	public String getGeocodeLongitude() {
		return geocodeLongitude;
	}

	public void setGeocodeLongitude(final String geocodeLongitude) {
		this.geocodeLongitude = geocodeLongitude;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

}
