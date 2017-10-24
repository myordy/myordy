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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(
	name="menudiscountcoupon",
	uniqueConstraints=@UniqueConstraint(columnNames={"menu_id", "coupon_code"})
)
public final class MenuDiscountCoupon implements Serializable {

	// 6 pack or 1 bottle or with food pizza pasta burger calzone .... zestypizza
	// pizzeriadeoz and viggie customers separata list

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="menu_discount_coupon_id")
    private Integer menuDiscountCouponId;

	@Column(name="coupon_code", nullable = true)
	private String couponCode;

	@Column(name="description", nullable = true)
	private String description;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false, updatable = false)
    private Menu menu;

	@Column(name="ordy_cashback_formula", nullable = true, columnDefinition="TEXT")
	private String ordyCashbackFormula; // setCashbackOnMinimumFinalTotal(3000, 500)

/*

	onMenuDiscountCouponSelect : function (posUI) {
		var coupon = findCouponByCode(couponCode); // TODO
		if (coupon) {
			var setCashbackOnMinimumFinalTotal = function (minimumFinalTotalAmount, cashbackAmount)	{
				posUI.cartUI.cart.cashback = 0;
				var finalTotalAmount = posUI.cartUI.getFinalTotalAmount(posUI, posUI.cartUI.cart);
				if (finalTotalAmount >= minimumFinalTotalAmount) {
					posUI.cartUI.cart.cashback = cashbackAmount;
				}
			};
			if (coupon.ordyCashbackFormula) {
				eval(
					'result = ' + coupon.ordyCashbackFormula + ';'
				);
			}
		}
	}


insert into menudiscountcoupon (coupon_code,description,ordy_cashback_formula,status,menu_id) values ('abc123','super duper coupon','setCashbackOnMinimumFinalTotal(3000, 500)','ACTIVE',1);

*/

	@JsonIgnore
	@OneToMany(mappedBy="menuDiscountCoupon", fetch = FetchType.LAZY, cascade = {CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE})
	private Set<Ordy> ordyList;
	
    @Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

    public Integer getMenuDiscountCouponId() {
		return menuDiscountCouponId;
	}

	public void setMenuDiscountCouponId(final Integer menuDiscountCouponId) {
		this.menuDiscountCouponId = menuDiscountCouponId;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(final String couponCode) {
		this.couponCode = couponCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(final Menu menu) {
		this.menu = menu;
	}

	public String getOrdyCashbackFormula() {
		return ordyCashbackFormula;
	}

	public void setOrdyCashbackFormula(final String ordyCashbackFormula) {
		this.ordyCashbackFormula = ordyCashbackFormula;
	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(final EntityStatus status) {
		this.status = status;
	}

	public Set<Ordy> getOrdyList() {
		return ordyList;
	}

	public void setOrdyList(final Set<Ordy> ordyList) {
		this.ordyList = ordyList;
	}

}

