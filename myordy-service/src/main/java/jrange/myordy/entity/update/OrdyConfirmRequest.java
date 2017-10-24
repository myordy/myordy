package jrange.myordy.entity.update;

import java.io.Serializable;

public final class OrdyConfirmRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer shopId;
	private Integer ordyId;

	public Integer getShopId() {
		return shopId;
	}
	public void setShopId(final Integer shopId) {
		this.shopId = shopId;
	}
	public Integer getOrdyId() {
		return ordyId;
	}
	public void setOrdyId(final Integer ordyId) {
		this.ordyId = ordyId;
	}

}
