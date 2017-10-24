package jrange.myordy.form;

import java.io.Serializable;

public class ShopForm implements Serializable {
    private static final long serialVersionUID = 1L;

    private String shopId;
    private String shopName;

    public String getShopId() {
		return shopId;
	}
	public void setShopId(final String shopId) {
		this.shopId = shopId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(final String shopName) {
		this.shopName = shopName;
	}

}
