package jrange.myordy.v1.shop.restaurant;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name="5_shop_customer_restaurant")
public final class ShopCustomerRestaurant implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final byte STATUS_DELETED = 0;
    public static final byte STATUS_ACTIVE = 1;

	@GeneratedValue
    @Id
    @Column(name="id")
    private Long id;
	@Column(name="shop_customer_number")
    private Integer customerNumber;
	@Column(name="name")
    private String name;
	@Column(name="phone_csv")
    private String phoneCSV;
	@Column(name="address")
    private String address;
	@Column(name="suburb_name")
    private String suburbName;
	@Column(name="postcode")
    private String postcode;
	@Column(name="suburb_code")
    private String suburbCode;
	@Column(name="geocode_lat")
    private Float geocodeLat;
	@Column(name="geocode_lng")
    private Float geocodeLng;
	@Column(name="map_ref")
    private String mapRef;
	@Column(name="note")
    private String note;
	@Column(name="last_order_date")
    private Date lastOrderDate;
	@Column(name="order_count")
    private Long orderCount;
	@Column(name="user_id_fk")
    private Long userId;
	@Column(name="shop_id_fk")
    private Long shopId;
	@Column(name="status")
    private Short status;
	public Long getId() {
		return id;
	}
	public void setId(final Long id) {
		this.id = id;
	}
	public Integer getCustomerNumber() {
		return customerNumber;
	}
	public void setCustomerNumber(final Integer customerNumber) {
		this.customerNumber = customerNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(final String name) {
		this.name = name;
	}
	public String getPhoneCSV() {
		return phoneCSV;
	}
	public void setPhoneCSV(final String phoneCSV) {
		this.phoneCSV = phoneCSV;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(final String address) {
		this.address = address;
	}
	public String getSuburbName() {
		return suburbName;
	}
	public void setSuburbName(final String suburbName) {
		this.suburbName = suburbName;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(final String postcode) {
		this.postcode = postcode;
	}
	public String getSuburbCode() {
		return suburbCode;
	}
	public void setSuburbCode(final String suburbCode) {
		this.suburbCode = suburbCode;
	}
	public Float getGeocodeLat() {
		return geocodeLat;
	}
	public void setGeocodeLat(final Float geocodeLat) {
		this.geocodeLat = geocodeLat;
	}
	public Float getGeocodeLng() {
		return geocodeLng;
	}
	public void setGeocodeLng(final Float geocodeLng) {
		this.geocodeLng = geocodeLng;
	}
	public String getMapRef() {
		return mapRef;
	}
	public void setMapRef(final String mapRef) {
		this.mapRef = mapRef;
	}
	public String getNote() {
		return note;
	}
	public void setNote(final String note) {
		this.note = note;
	}
	public Date getLastOrderDate() {
		return lastOrderDate;
	}
	public void setLastOrderDate(final Date lastOrderDate) {
		this.lastOrderDate = lastOrderDate;
	}
	public Long getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(final Long orderCount) {
		this.orderCount = orderCount;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(final Long userId) {
		this.userId = userId;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(final Long shopId) {
		this.shopId = shopId;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(final Short status) {
		this.status = status;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
