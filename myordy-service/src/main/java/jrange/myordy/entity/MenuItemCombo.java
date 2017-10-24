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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="menuitemcombo")
public final class MenuItemCombo implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="menu_item_combo_id", insertable=false, updatable=false)
    private Integer menuItemComboId;

	@Column(name="code", nullable = false)
	private String code;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false, updatable = false)
    private Menu menu;

    @Column(name="price_formula", nullable = true, columnDefinition="TEXT")
	private String priceFormula;

    @Column(name="price_formula_extra_options", nullable = true)
	private String priceFormulaExtraOptions;

    @OneToMany(mappedBy="menuItemCombo", fetch = FetchType.EAGER, cascade = {CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE})
	private Set<MenuItemComboOption> comboOptionList;

	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="menu_item_combo_tag", joinColumns=@JoinColumn(name="menu_item_combo_id"), inverseJoinColumns=@JoinColumn(name="tag_id")) 
	private Set<Tag> tagList;

	@Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

	public Integer getMenuItemComboId() {
		return menuItemComboId;
	}

	public void setMenuItemComboId(final Integer menuItemComboId) {
		this.menuItemComboId = menuItemComboId;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(final Menu menu) {
		this.menu = menu;
	}

	public String getPriceFormula() {
		return priceFormula;
	}

	public void setPriceFormula(final String priceFormula) {
		this.priceFormula = priceFormula;
	}

	public Set<MenuItemComboOption> getComboOptionList() {
		return comboOptionList;
	}

	public void setComboOptionList(final Set<MenuItemComboOption> comboOptionList) {
		this.comboOptionList = comboOptionList;
	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(final EntityStatus status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public Set<Tag> getTagList() {
		return tagList;
	}

	public void setTagList(final Set<Tag> tagList) {
		this.tagList = tagList;
	}

	public String getPriceFormulaExtraOptions() {
		return priceFormulaExtraOptions;
	}

	public void setPriceFormulaExtraOptions(final String priceFormulaExtraOptions) {
		this.priceFormulaExtraOptions = priceFormulaExtraOptions;
	}

}
