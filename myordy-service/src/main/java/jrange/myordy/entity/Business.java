package jrange.myordy.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import jrange.myordy.entity.menuitemoption.ExtraOptionGroup;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="business")
public final class Business implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="business_id")
    private Integer businessId;

	@JsonIgnore
	@OneToMany(mappedBy="business", fetch = FetchType.LAZY, targetEntity=Shop.class)
	private Set<Shop> shopList;

	@JsonIgnore
	@OneToMany(mappedBy="business", fetch = FetchType.LAZY, targetEntity=Menu.class)
	private Set<Menu> menuList;

	@JsonIgnore
	@OneToMany(mappedBy="business", fetch = FetchType.EAGER)
	private Set<Message> messageList;

	@JsonIgnore
	@OneToMany(mappedBy="business", fetch = FetchType.LAZY)
	private Set<Product> productList;

	@JsonIgnore
	@OneToMany(mappedBy="business", fetch = FetchType.LAZY)
	private Set<Category> categoryList;

	@OneToMany(mappedBy="business", fetch = FetchType.EAGER)
	private Set<ExtraOptionGroup> extraOptionGroupList;

	public Integer getBusinessId() {
		return businessId;
	}

	public void setBusinessId(final Integer businessId) {
		this.businessId = businessId;
	}

	public Set<Shop> getShopList() {
		return shopList;
	}

	public void setShopList(final Set<Shop> shopList) {
		this.shopList = shopList;
	}

	public Set<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(final Set<Menu> menuList) {
		this.menuList = menuList;
	}

	public Set<Message> getMessageList() {
		return messageList;
	}

	public void setMessageList(Set<Message> messageList) {
		this.messageList = messageList;
	}

	public Set<Product> getProductList() {
		return productList;
	}

	public void setProductList(Set<Product> productList) {
		this.productList = productList;
	}

	public Set<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(Set<Category> categoryList) {
		this.categoryList = categoryList;
	}

	public Set<ExtraOptionGroup> getExtraOptionGroupList() {
		return extraOptionGroupList;
	}

	public void setExtraOptionGroupList(final Set<ExtraOptionGroup> extraOptionGroupList) {
		this.extraOptionGroupList = extraOptionGroupList;
	}

}
