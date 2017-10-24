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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="theuseraddress")
public final class TheUserAddress implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="the_user_address_id")
    private Integer theUserAddressId;

    @Column(name="address", nullable = false)
	private String address;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suburb_id", nullable = true, updatable = false)
    private Suburb suburb;

    @Transient
    private Integer suburbId;

    @Column(name="geocode_latitude", nullable = true)
    private String geocodeLatitude;

    @Column(name="geocode_longitude", nullable = true)
    private String geocodeLongitude;

    @Column(name="mapRef", nullable = true)
    private String mapRef;

    @Column(name="suburb_name", nullable = true)
    private String suburbName;

    @Column(name="state_code", nullable = true)
    private String stateCode;

    @Column(name="country_code", nullable = true)
    private String countryCode;

    @Column(name="postcode", nullable = true)
    private String postcode;

    @Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

	public Integer getTheUserAddressId() {
		return theUserAddressId;
	}

	public void setTheUserAddressId(final Integer theUserAddressId) {
		this.theUserAddressId = theUserAddressId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(final String address) {
		this.address = address;
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

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(final EntityStatus status) {
		this.status = status;
	}

	public String getMapRef() {
		return mapRef;
	}

	public void setMapRef(final String mapRef) {
		this.mapRef = mapRef;
	}

	public Suburb getSuburb() {
		return suburb;
	}

	public void setSuburb(final Suburb suburb) {
		this.suburb = suburb;
	}

	public String getSuburbName() {
		return suburbName;
	}

	public void setSuburbName(final String suburbName) {
		this.suburbName = suburbName;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(final String stateCode) {
		this.stateCode = stateCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(final String countryCode) {
		this.countryCode = countryCode;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(final String postcode) {
		this.postcode = postcode;
	}

	public Integer getSuburbId() {
		return suburbId;
	}

	public void setSuburbId(final Integer suburbId) {
		this.suburbId = suburbId;
	}

}
