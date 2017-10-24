package jrange.myordy.entity;

import java.io.Serializable;
import java.util.List;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Where;

import jrange.myordy.util.MenuItemUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(
	name="menu",
	uniqueConstraints=@UniqueConstraint(columnNames={"business_id", "code"})
)
public final class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="menu_id")
    private Integer menuId;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false, updatable = false)
    private Business business;

//	@JsonIgnore
//	@ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "shop_id", nullable = false, updatable = false)
//    private Shop shop;

	@Column(name="code", nullable = false)
	private String code;

	@JsonIgnore
	@OneToMany(mappedBy="menu", fetch = FetchType.EAGER)
	private Set<MenuItem> menuItemList;

	@OneToMany(mappedBy="menu", fetch = FetchType.EAGER)
	private Set<MenuItemCombo> menuItemComboList;

	@OneToOne(fetch = FetchType.EAGER, mappedBy = "menu", cascade = {CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE})
	private MenuOrdyConfig menuOrdyConfig;

	@OneToMany(mappedBy="menu", fetch = FetchType.EAGER)
	private Set<MenuDiscountCoupon> menuDiscountCouponList;

	@Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(final Integer menuId) {
		this.menuId = menuId;
	}

//	public Shop getShop() {
//		return shop;
//	}
//
//	public void setShop(final Shop shop) {
//		this.shop = shop;
//	}

	public List<MenuItem> getMenuItems() {
		return MenuItemUtil.sortMenuItemList(menuItemList);
	}

	public Set<MenuItem> getMenuItemList() {
		return menuItemList;
	}

	public void setMenuItemList(final Set<MenuItem> menuItemList) {
		this.menuItemList = menuItemList;
	}

	public EntityStatus getStatus() {
		return status;
	}

	public Menu setStatus(final EntityStatus status) {
		this.status = status;
		return this;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(final Business business) {
		this.business = business;
	}

	public Set<MenuItemCombo> getMenuItemComboList() {
		return menuItemComboList;
	}

	public void setMenuItemComboList(final Set<MenuItemCombo> menuItemComboList) {
		this.menuItemComboList = menuItemComboList;
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public MenuOrdyConfig getMenuOrdyConfig() {
		return menuOrdyConfig;
	}

	public void setMenuOrdyConfig(final MenuOrdyConfig menuOrdyConfig) {
		this.menuOrdyConfig = menuOrdyConfig;
	}

	public Set<MenuDiscountCoupon> getMenuDiscountCouponList() {
		return menuDiscountCouponList;
	}

	public void setMenuDiscountCouponList(final Set<MenuDiscountCoupon> menuDiscountCouponList) {
		this.menuDiscountCouponList = menuDiscountCouponList;
	}

}
