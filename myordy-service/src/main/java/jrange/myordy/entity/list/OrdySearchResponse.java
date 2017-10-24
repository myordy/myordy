package jrange.myordy.entity.list;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Column;

import jrange.myordy.entity.OrdyStatus;

public final class OrdySearchResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long searchStartTime;
	private Long totalItems;
	private final ArrayList<OrdySearchListItem> items = new ArrayList<OrdySearchListItem>();

	public Long getSearchStartTime() {
		return searchStartTime;
	}
	public void setSearchStartTime(final Long searchStartTime) {
		this.searchStartTime = searchStartTime;
	}
	public Long getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(final Long totalItems) {
		this.totalItems = totalItems;
	}
	public ArrayList<OrdySearchListItem> getItems() {
		return items;
	}


	public static final class OrdySearchListItem implements Serializable {
		private static final long serialVersionUID = 1L;

		private Integer ordyId;
	    private String customerName;
	    private Date orderTime;
	    private Long amount;
	    private Integer menuId;
	    private Integer deliveryCharge;
	    private Float fixedAmount;
	    private Float discountPercent;
	    private Long cashback;
		private OrdyStatus ordyStatus;

	    public Integer getOrdyId() {
			return ordyId;
		}
		public void setOrdyId(final Integer ordyId) {
			this.ordyId = ordyId;
		}
		public String getCustomerName() {
			return customerName;
		}
		public void setCustomerName(final String customerName) {
			this.customerName = customerName;
		}
		public Date getOrderTime() {
			return orderTime;
		}
		public void setOrderTime(final Date orderTime) {
			this.orderTime = orderTime;
		}
		public Long getAmount() {
			return amount;
		}
		public void setAmount(final Long amount) {
			this.amount = amount;
		}
		public Integer getMenuId() {
			return menuId;
		}
		public void setMenuId(final Integer menuId) {
			this.menuId = menuId;
		}
		public Integer getDeliveryCharge() {
			return deliveryCharge;
		}
		public void setDeliveryCharge(final Integer deliveryCharge) {
			this.deliveryCharge = deliveryCharge;
		}
		public Float getFixedAmount() {
			return fixedAmount;
		}
		public void setFixedAmount(final Float fixedAmount) {
			this.fixedAmount = fixedAmount;
		}
		public Float getDiscountPercent() {
			return discountPercent;
		}
		public void setDiscountPercent(final Float discountPercent) {
			this.discountPercent = discountPercent;
		}
		public OrdyStatus getOrdyStatus() {
			return ordyStatus;
		}
		public void setOrdyStatus(final OrdyStatus ordyStatus) {
			this.ordyStatus = ordyStatus;
		}
		public Long getCashback() {
			return cashback;
		}
		public void setCashback(final Long cashback) {
			this.cashback = cashback;
		}
	}

}
