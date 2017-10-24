package jrange.myordy.entity.list;

import java.io.Serializable;

import jrange.myordy.util.DateRange;

public final class SalesSummaryReportRequest extends SearchRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private DateRange orderDates;
	private Integer shopId;

	public DateRange getOrderDates() {
		return orderDates;
	}

	public void setOrderDates(DateRange orderDates) {
		this.orderDates = orderDates;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(final Integer shopId) {
		this.shopId = shopId;
	}

}
