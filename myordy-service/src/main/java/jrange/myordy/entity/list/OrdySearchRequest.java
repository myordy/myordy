package jrange.myordy.entity.list;

import java.io.Serializable;
import java.util.Date;

import jrange.myordy.entity.OrdyStatus;

public final class OrdySearchRequest extends SearchRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date orderTimeFrom;
	private Date orderTimeTo;
	private Integer shopId;
	private Integer customerId;
	private String deviceSessionId;
	private Integer customerNumber;
	private OrdyStatus ordyStatus;
	private Boolean orderByLatestOrderFirst;

	public Date getOrderTimeFrom() {
		return orderTimeFrom;
	}
	public void setOrderTimeFrom(final Date orderTimeFrom) {
		this.orderTimeFrom = orderTimeFrom;
	}
	public Date getOrderTimeTo() {
		return orderTimeTo;
	}
	public void setOrderTimeTo(final Date orderTimeTo) {
		this.orderTimeTo = orderTimeTo;
	}
	public Integer getShopId() {
		return shopId;
	}
	public void setShopId(final Integer shopId) {
		this.shopId = shopId;
	}
	public String getDeviceSessionId() {
		return deviceSessionId;
	}
	public void setDeviceSessionId(final String deviceSessionId) {
		this.deviceSessionId = deviceSessionId;
	}
	public OrdyStatus getOrdyStatus() {
		return ordyStatus;
	}
	public void setOrdyStatus(final OrdyStatus ordyStatus) {
		this.ordyStatus = ordyStatus;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(final Integer customerId) {
		this.customerId = customerId;
	}
	public Integer getCustomerNumber() {
		return customerNumber;
	}
	public void setCustomerNumber(final Integer customerNumber) {
		this.customerNumber = customerNumber;
	}
	public Boolean getOrderByLatestOrderFirst() {
		return orderByLatestOrderFirst;
	}
	public void setOrderByLatestOrderFirst(final Boolean orderByLatestOrderFirst) {
		this.orderByLatestOrderFirst = orderByLatestOrderFirst;
	}

}
