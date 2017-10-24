package jrange.myordy.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="menuordyconfig")
public final class MenuOrdyConfig implements Serializable {

    private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="menu_ordy_config_id", insertable=false, updatable=false)
    private Integer menuOrdyConfigId;

    @Column(name="min_ordy_amount", nullable = true)
    private Integer minOrdyAmount;

    @Column(name="discount_percent_online_ordy", nullable = true)
    private Float discountPercentOnlineOrdy;

    @Column(name="discount_percent_first_ordy", nullable = true)
    private Float discountPercentFirstOrdy;

    @Column(name="print_copies_count", nullable = true)
    private Integer printCopiesCount;

    @Column(name="default_delivery_mins", nullable = true)
    private Integer defaultDeliveryMins;

    @OneToMany(mappedBy="menuOrdyConfig", fetch = FetchType.EAGER, cascade = {CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE})
	private Set<MenuOrdyServiceSuburb> shopSuburbConfigList;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false, updatable = false)
    private Menu menu;

	@Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

	public Integer getMenuOrdyConfigId() {
		return menuOrdyConfigId;
	}

	public void setMenuOrdyConfigId(final Integer menuOrdyConfigId) {
		this.menuOrdyConfigId = menuOrdyConfigId;
	}

	public Integer getMinOrdyAmount() {
		return minOrdyAmount;
	}

	public void setMinOrdyAmount(final Integer minOrdyAmount) {
		this.minOrdyAmount = minOrdyAmount;
	}

	public Set<MenuOrdyServiceSuburb> getShopSuburbConfigList() {
		return shopSuburbConfigList;
	}

	public void setShopSuburbConfigList(
			final Set<MenuOrdyServiceSuburb> shopSuburbConfigList) {
		this.shopSuburbConfigList = shopSuburbConfigList;
	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(final EntityStatus status) {
		this.status = status;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(final Menu menu) {
		this.menu = menu;
	}

	public Float getDiscountPercentOnlineOrdy() {
		return discountPercentOnlineOrdy;
	}

	public void setDiscountPercentOnlineOrdy(final Float discountPercentOnlineOrdy) {
		this.discountPercentOnlineOrdy = discountPercentOnlineOrdy;
	}

	public Float getDiscountPercentFirstOrdy() {
		return discountPercentFirstOrdy;
	}

	public void setDiscountPercentFirstOrdy(final Float discountPercentFirstOrdy) {
		this.discountPercentFirstOrdy = discountPercentFirstOrdy;
	}

	public Integer getPrintCopiesCount() {
		return printCopiesCount;
	}

	public void setPrintCopiesCount(final Integer printCopiesCount) {
		this.printCopiesCount = printCopiesCount;
	}

	public Integer getDefaultDeliveryMins() {
		return defaultDeliveryMins;
	}

	public void setDefaultDeliveryMins(final Integer defaultDeliveryMins) {
		this.defaultDeliveryMins = defaultDeliveryMins;
	}

}
