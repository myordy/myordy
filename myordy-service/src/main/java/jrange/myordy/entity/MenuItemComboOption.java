package jrange.myordy.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="menuitemcombooption")
public final class MenuItemComboOption implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="menu_item_combo_option_id", insertable=false, updatable=false)
    private Integer menuItemComboOptionId;

    @Column(name="qty_required", nullable = false)
	private Integer qtyRequired;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false, updatable = false)
    private Tag menuItemTag;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_combo_id", nullable = false, updatable = false)
    private MenuItemCombo menuItemCombo;

    @Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

	public Integer getMenuItemComboOptionId() {
		return menuItemComboOptionId;
	}

	public void setMenuItemComboOptionId(final Integer menuItemComboOptionId) {
		this.menuItemComboOptionId = menuItemComboOptionId;
	}

	public Integer getQtyRequired() {
		return qtyRequired;
	}

	public void setQtyRequired(final Integer qtyRequired) {
		this.qtyRequired = qtyRequired;
	}

	public Tag getMenuItemTag() {
		return menuItemTag;
	}

	public void setMenuItemTag(final Tag menuItemTag) {
		this.menuItemTag = menuItemTag;
	}

	public MenuItemCombo getMenuItemCombo() {
		return menuItemCombo;
	}

	public void setMenuItemCombo(final MenuItemCombo menuItemCombo) {
		this.menuItemCombo = menuItemCombo;
	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(final EntityStatus status) {
		this.status = status;
	}

}
