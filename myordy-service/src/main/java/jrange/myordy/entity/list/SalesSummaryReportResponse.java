package jrange.myordy.entity.list;

import java.io.Serializable;
import java.util.ArrayList;

public final class SalesSummaryReportResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private final ArrayList<SalesSummaryReportListItem> items = new ArrayList<SalesSummaryReportListItem>();

	public ArrayList<SalesSummaryReportListItem> getItems() {
		return items;
	}

	public static final class SalesSummaryReportListItem implements Serializable {

		private static final long serialVersionUID = 1L;

		private Integer menuId;
	    private Long orderTotalCount;
	    private Long orderTotalAmount;
	    private Long orderTotalDiscountAmount;

	    public Integer getMenuId() {
			return menuId;
		}
		public SalesSummaryReportListItem setMenuId(final Integer menuId) {
			this.menuId = menuId;
			return this;
		}
		public Long getOrderTotalCount() {
			return orderTotalCount;
		}
		public SalesSummaryReportListItem setOrderTotalCount(final Long orderTotalCount) {
			this.orderTotalCount = orderTotalCount;
			return this;
		}
		public Long getOrderTotalAmount() {
			return orderTotalAmount;
		}
		public SalesSummaryReportListItem setOrderTotalAmount(final Long orderTotalAmount) {
			this.orderTotalAmount = orderTotalAmount;
			return this;
		}
		public Long getOrderTotalDiscountAmount() {
			return orderTotalDiscountAmount;
		}
		public SalesSummaryReportListItem setOrderTotalDiscountAmount(final Long orderTotalDiscountAmount) {
			this.orderTotalDiscountAmount = orderTotalDiscountAmount;
			return this;
		}
	}

}
