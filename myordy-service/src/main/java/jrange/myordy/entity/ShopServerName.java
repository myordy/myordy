package jrange.myordy.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="shopservername")
public final class ShopServerName implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="shop_server_name_id")
    private Integer shopServerNameId;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false, updatable = false)
    private Shop shop;

    @Column(name="server_name", nullable = false, unique = true)
    private String serverName;

	public Integer getShopServerNameId() {
		return shopServerNameId;
	}

	public void setShopServerNameId(final Integer shopServerNameId) {
		this.shopServerNameId = shopServerNameId;
	}

	public Shop getShop() {
		return shop;
	}

	public ShopServerName setShop(final Shop shop) {
		this.shop = shop;
		return this;
	}

	public String getServerName() {
		return serverName;
	}

	public ShopServerName setServerName(final String serverName) {
		this.serverName = serverName;
		return this;
	}

}
