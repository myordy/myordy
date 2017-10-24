package jrange.myordy.entity;

import java.io.Serializable;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="ordyitem")
public class OrdyItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="ordy_item_id")
    private Integer ordyItemId;

////	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordy_id", nullable = true, updatable = false)
    private Ordy ordy;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordy_item_combo_id", nullable = true, updatable = false)
    private OrdyItemCombo ordyItemCombo;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordy_item_combo_sub_id", nullable = true, updatable = false)
    private OrdyItemComboSub ordyItemComboSub;

	@OneToOne (fetch = FetchType.EAGER, cascade={CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE})
	private OrdyItemExtraOption extraOptions;

//	@JsonIgnore
//	@ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "menu_item_id", nullable = false, updatable = false)
//    private MenuItem menuItem;
    @Column(name="menu_item_id", nullable = false, updatable = false)
    private Integer menuItemId;

    @Column(name="price")
    private Integer price;

    @Column(name="amount")
    private Integer amount;

    @Column(name="qty")
    private Integer qty;

    @Column(name="note", nullable = true)
	private String note;

    @Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

	public Integer getOrdyItemId() {
		return ordyItemId;
	}

	public void setOrdyItemId(final Integer ordyItemId) {
		this.ordyItemId = ordyItemId;
	}
	
	public Ordy getOrdy() {
		return ordy;
	}

	public void setOrdy(final Ordy ordy) {
		this.ordy = ordy;
	}

//	public MenuItem getMenuItem() {
//		return menuItem;
//	}
//
//	public void setMenuItem(MenuItem menuItem) {
//		this.menuItem = menuItem;
//	}

	public Integer getMenuItemId() {
		return menuItemId;
	}

	public void setMenuItemId(final Integer menuItemId) {
		this.menuItemId = menuItemId;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(final Integer price) {
		this.price = price;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(final Integer amount) {
		this.amount = amount;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(final Integer qty) {
		this.qty = qty;
	}

	public String getNote() {
		return note;
	}

	public void setNote(final String note) {
		this.note = note;
	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(final EntityStatus status) {
		this.status = status;
	}

	public OrdyItemExtraOption getExtraOptions() {
		return extraOptions;
	}

	public void setExtraOptions(final OrdyItemExtraOption extraOptions) {
		this.extraOptions = extraOptions;
	}

	public OrdyItemCombo getOrdyItemCombo() {
		return ordyItemCombo;
	}

	public void setOrdyItemCombo(final OrdyItemCombo ordyItemCombo) {
		this.ordyItemCombo = ordyItemCombo;
	}

	public OrdyItemComboSub getOrdyItemComboSub() {
		return ordyItemComboSub;
	}

	public void setOrdyItemComboSub(final OrdyItemComboSub ordyItemComboSub) {
		this.ordyItemComboSub = ordyItemComboSub;
	}

}
