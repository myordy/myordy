package jrange.myordy.entity.list;

import java.io.Serializable;

public final class CustomerSearchRequest extends SearchRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String phone;
	private String address;
	private Integer shopId;
	private Integer customerNumber;

	public String getName() {
		return name;
	}
	public void setName(final String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(final String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(final String address) {
		this.address = address;
	}
	public Integer getShopId() {
		return shopId;
	}
	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
	public Integer getCustomerNumber() {
		return customerNumber;
	}
	public void setCustomerNumber(final Integer customerNumber) {
		this.customerNumber = customerNumber;
	}
}
