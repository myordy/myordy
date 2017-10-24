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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="ordyitemcombo")
public class OrdyItemCombo implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="ordy_item_combo_id")
    private Integer ordyItemComboId;

    @Column(name="amount")
    private Integer amount;

    @Column(name="qty")
    private Integer qty;

    @Column(name="note", nullable = true)
	private String note;

    @Column(name="price_formula", nullable = true)
	private String priceFormula;

    @Column(name="menu_item_combo_id", nullable = false, updatable = false)
    private Integer menuItemComboId;

    @OneToMany(mappedBy="ordyItemCombo", fetch = FetchType.LAZY, cascade = {CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE})
	private Set<OrdyItem> orderItems;

    @OneToMany(mappedBy="ordyItemCombo", fetch = FetchType.LAZY, cascade = {CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE})
	private Set<OrdyItemComboSub> ordyItemsComboSub;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordy_id", nullable = false, updatable = false)
    private Ordy ordy;

	@Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

	public Integer getOrdyItemComboId() {
		return ordyItemComboId;
	}

	public void setOrdyItemComboId(final Integer ordyItemComboId) {
		this.ordyItemComboId = ordyItemComboId;
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

	public String getPriceFormula() {
		return priceFormula;
	}

	public void setPriceFormula(final String priceFormula) {
		this.priceFormula = priceFormula;
	}

	public Integer getMenuItemComboId() {
		return menuItemComboId;
	}

	public void setMenuItemComboId(final Integer menuItemComboId) {
		this.menuItemComboId = menuItemComboId;
	}

	public Set<OrdyItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(final Set<OrdyItem> orderItems) {
		this.orderItems = orderItems;
	}

	public Ordy getOrdy() {
		return ordy;
	}

	public void setOrdy(final Ordy ordy) {
		this.ordy = ordy;
	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(final EntityStatus status) {
		this.status = status;
	}

	public Set<OrdyItemComboSub> getOrdyItemsComboSub() {
		return ordyItemsComboSub;
	}

	public void setOrdyItemsComboSub(final Set<OrdyItemComboSub> ordyItemsComboSub) {
		this.ordyItemsComboSub = ordyItemsComboSub;
	}

}
