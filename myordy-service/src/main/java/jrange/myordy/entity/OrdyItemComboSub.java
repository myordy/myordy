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
@Table(name="ordyitemcombosub")
public class OrdyItemComboSub implements Serializable {

	private static final long serialVersionUID = 1L;

    @GeneratedValue
    @Id
    @Column(name="ordy_item_combo_sub_id")
    private Integer subOrdyItemComboSubId;

    @Column(name="qty")
    private Integer qty;

    @Column(name="note", nullable = true)
    private String note;

    @Column(name="menu_item_combo_id", nullable = false, updatable = false)
    private Integer menuItemComboId;

    @OneToMany(mappedBy="ordyItemComboSub", fetch = FetchType.LAZY, cascade = {CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE})
    private Set<OrdyItem> orderItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordy_item_combo_id", nullable = false, updatable = false)
    private OrdyItemCombo ordyItemCombo;

    @Column(name="status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EntityStatus status;

	public Integer getSubOrdyItemComboSubId() {
		return subOrdyItemComboSubId;
	}

	public void setSubOrdyItemComboSubId(final Integer subOrdyItemComboSubId) {
		this.subOrdyItemComboSubId = subOrdyItemComboSubId;
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

	public OrdyItemCombo getOrdyItemCombo() {
		return ordyItemCombo;
	}

	public void setOrdyItemCombo(final OrdyItemCombo ordyItemCombo) {
		this.ordyItemCombo = ordyItemCombo;
	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(final EntityStatus status) {
		this.status = status;
	}

}
