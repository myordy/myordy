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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import jrange.myordy.entity.menuitemoption.ExtraOptionMenuItemConfig;
import jrange.myordy.util.MenuItemUtil;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="menuitem")
public final class MenuItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="menu_item_id", insertable=false, updatable=false)
    private Integer menuItemId;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = true, updatable = false)
    private Menu menu;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_menu_item_id", nullable = true, updatable = false)
    private MenuItem parentMenuItem;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false, updatable = false)
    private Business business;

//	@JsonIgnore
    @Column(name="previous_menu_item_id", nullable = true)
    private Integer previousMenuItemId;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = true, updatable = true)
    private Product product;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = true, updatable = true)
    private Category category;

	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, mappedBy="parentMenuItem")
	private Set<MenuItem> menuItemList;

	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="menu_item_tag", joinColumns=@JoinColumn(name="menu_item_id"), inverseJoinColumns=@JoinColumn(name="tag_id")) 
	private Set<Tag> tagList;

    @Column(name="price", nullable = true)
	private Long price;

	@OneToOne (fetch = FetchType.EAGER, cascade = {CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="extra_option_menu_item_config_id", nullable=true, insertable=true, updatable=true)
    private ExtraOptionMenuItemConfig extraOptionConfig;

    @Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

	public Integer getMenuItemId() {
		return menuItemId;
	}

	public void setMenuItemId(final Integer menuItemId) {
		this.menuItemId = menuItemId;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(final Menu menu) {
		this.menu = menu;
	}

	public MenuItem getParentMenuItem() {
		return parentMenuItem;
	}

	public void setParentMenuItem(final MenuItem parentMenuItem) {
		this.parentMenuItem = parentMenuItem;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(final Product product) {
		this.product = product;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(final Category category) {
		this.category = category;
	}

	public Set<MenuItem> getMenuItemList() {
		return menuItemList;
	}

	public List<MenuItem> getMenuItems() {
		if (null != this.category) {
			return MenuItemUtil.sortMenuItemList(menuItemList);
		}
		return null;
	}

	public void setMenuItemList(final Set<MenuItem> menuItemList) {
		this.menuItemList = menuItemList;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(final Long price) {
		this.price = price;
	}

	public Integer getPreviousMenuItemId() {
		return previousMenuItemId;
	}

	public void setPreviousMenuItemId(final Integer previousMenuItemId) {
		this.previousMenuItemId = previousMenuItemId;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public Set<Tag> getTagList() {
		return tagList;
	}

	public void setTagList(final Set<Tag> tagList) {
		this.tagList = tagList;
	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(final EntityStatus status) {
		this.status = status;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(final Business business) {
		this.business = business;
	}

	public ExtraOptionMenuItemConfig getExtraOptionConfig() {
		return extraOptionConfig;
	}

	public void setExtraOptionConfig(final ExtraOptionMenuItemConfig extraOptionConfig) {
		this.extraOptionConfig = extraOptionConfig;
	}

}
